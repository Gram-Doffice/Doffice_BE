package gram11.doffice.global.s3;

import gram11.doffice.global.s3.exception.BadFileExtensionException;
import gram11.doffice.global.s3.exception.EmptyFileException;
import gram11.doffice.global.s3.exception.FailUploadImageException;
import gram11.doffice.global.s3.exception.WrongFileUrlException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploadService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.exp-time}")
    private String s3Exp;

    public String verifyFile(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) throw EmptyFileException.EXCEPTION;

        final Set<String> allowedExtensions = Set.of("jpg", "jpeg", "png", "gif", "webp");

        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase(Locale.getDefault());

        if (!allowedExtensions.contains(ext)) throw BadFileExtensionException.EXCEPTION;

        return ext;
    }

    public String upload(MultipartFile file, String path) {
        String ext = verifyFile(file);

        String randomName = UUID.randomUUID().toString();
        String fileKey = path + randomName + "." + ext;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return fileKey;
        } catch (Exception e) {
            throw new FailUploadImageException(e);
        }
    }

    public String returnImageUrl(String fileKey) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }

    public void delete(String fileUrl) {
        try {
            String fileKey = fileUrl.substring(fileUrl.lastIndexOf(".com") + 1);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            if ("NoSuchKey".equals(e.awsErrorDetails().errorCode())) {
                return;
            }
            log.error("S3 파일 삭제 실패: {}", e.getMessage());
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}", e.getMessage());
        }
    }

    public String extractFileKey(String url) {
        if (url == null || url.isBlank()) {
            return "";
        }
        try {
            String pathOnly = url.split("\\?")[0];
            int index = pathOnly.indexOf(".com/");

            if (index != -1) {
                return pathOnly.substring(index + 1);
            }
            return pathOnly;
        } catch (Exception e) {
            log.warn("wrong image url: {}", url);
            throw WrongFileUrlException.EXCEPTION;
        }
    }
}
