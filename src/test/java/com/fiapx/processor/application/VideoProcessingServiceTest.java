package com.fiapx.processor.application;
import com.fiapx.processor.application.port.FrameExtractorPort;
import com.fiapx.processor.application.port.VideoStoragePort;
import com.fiapx.processor.domain.Events;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class VideoProcessingServiceTest {
    @Test void resultUsesAttemptSpecificOutputKey() throws Exception {
        var storage = mock(VideoStoragePort.class);
        var extractor = mock(FrameExtractorPort.class);
        when(extractor.extract(any(Path.class), any(Path.class))).thenReturn(2);
        var job = new Events.VideoRequested(UUID.randomUUID(), UUID.randomUUID(), "uploads/source");
        var result = new VideoProcessingService(storage, extractor).process(job);
        assertEquals("COMPLETED", result.status());
        assertEquals(2, result.frameCount());
        assertEquals("outputs/" + job.videoId() + "/" + job.attemptId() + "/frames.zip", result.outputKey());
        verify(storage).upload(eq(result.outputKey()), any(Path.class));
    }
}
