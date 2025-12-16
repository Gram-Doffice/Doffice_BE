package gram11.doffice.global.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import gram11.doffice.global.s3.exception.BadFileExtensionException;
import gram11.doffice.global.s3.exception.EmptyFileException;
import gram11.doffice.global.s3.exception.FailUploadImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.exp-time}")
    private String s3Exp;

    public String verifyFile(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) throw EmptyFileException.EXCEPTION;

        final Set<String> allowedExtensions = Set.of("jpg", "jpeg", "png", "gif");

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
            InputStream inputStream = file.getInputStream();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket,
                    fileKey,
                    inputStream,
                    metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3Client.putObject(putObjectRequest);
            return amazonS3Client.getUrl(bucket, fileKey).toString();

        } catch (Exception e) {
            throw new FailUploadImageException(e);
        }
    }

    public void delete(String fileName, String path) {
        try {
            amazonS3Client.deleteObject(bucket, path + fileName);
        } catch (AmazonServiceException e) {
            if ("NoSuchKey".equals(e.getErrorCode())) {
                return;
            }
            throw new FailUploadImageException(e);

        } catch (Exception e) {
            throw new FailUploadImageException(e);
        }
    }
}
