package gram11.doffice.global.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum S3BucketFolder {
    LOST("lost/");

    private final String path;
}