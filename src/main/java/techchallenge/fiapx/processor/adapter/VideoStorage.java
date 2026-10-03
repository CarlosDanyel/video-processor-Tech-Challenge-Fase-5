package techchallenge.fiapx.processor.adapter;
import java.nio.file.Path;
import techchallenge.fiapx.processor.application.port.VideoStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
@Component
public class VideoStorage implements VideoStoragePort {
    private final S3Client s3;
    private final String bucket;
    public VideoStorage(S3Client s3, @Value("${storage.bucket}") String bucket) { this.s3 = s3; this.bucket = bucket; }
    public void download(String key, Path destination) {
        s3.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build(), destination);
    }
    public void upload(String key, Path source) {
        s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).contentType("application/zip").build(), RequestBody.fromFile(source));
    }
}
