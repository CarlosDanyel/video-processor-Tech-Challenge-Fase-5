package techchallenge.fiapx.processor.application.port;
import java.nio.file.Path;
public interface FrameExtractorPort {
    int extract(Path video, Path zip) throws Exception;
}
