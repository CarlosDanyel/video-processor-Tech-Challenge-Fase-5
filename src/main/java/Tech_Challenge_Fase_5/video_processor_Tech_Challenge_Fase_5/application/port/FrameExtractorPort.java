package Tech_Challenge_Fase_5.video_processor_Tech_Challenge_Fase_5.application.port;
import java.nio.file.Path;
public interface FrameExtractorPort {
    int extract(Path video, Path zip) throws Exception;
}
