package techchallenge.fiapx.processor.adapter;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
@Configuration
public class StorageConfig {
    @Bean S3Client s3(@Value("${storage.endpoint}") String endpoint,
                      @Value("${storage.access-key}") String accessKey,
                      @Value("${storage.secret-key}") String secretKey) {
        return S3Client.builder().endpointOverride(URI.create(endpoint)).region(Region.US_EAST_1)
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .forcePathStyle(true).build();
    }
}
