package Tech_Challenge_Fase_5.video_processor_Tech_Challenge_Fase_5.domain;
import java.util.UUID;
public final class Events {
    private Events() {}
    public record VideoRequested(UUID videoId, UUID attemptId, String sourceKey) {}
    public record VideoResult(UUID videoId, UUID attemptId, String status, String outputKey, Integer frameCount, String errorMessage) {}
}
