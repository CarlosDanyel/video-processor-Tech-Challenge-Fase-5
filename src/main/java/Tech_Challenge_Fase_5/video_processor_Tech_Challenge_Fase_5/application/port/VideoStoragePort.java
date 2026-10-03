package Tech_Challenge_Fase_5.video_processor_Tech_Challenge_Fase_5.application.port;
import java.nio.file.Path;
public interface VideoStoragePort {
    void download(String key, Path destination);
    void upload(String key, Path source);
}
