package com.fiapx.processor.application;
import com.fiapx.processor.application.port.FrameExtractorPort;
import com.fiapx.processor.application.port.VideoStoragePort;
import com.fiapx.processor.domain.Events;
import java.nio.file.*;
import java.util.Comparator;
import org.springframework.stereotype.Service;
@Service
public class VideoProcessingService {
    private final VideoStoragePort storage;
    private final FrameExtractorPort extractor;
    public VideoProcessingService(VideoStoragePort storage, FrameExtractorPort extractor) {
        this.storage = storage; this.extractor = extractor;
    }
    public Events.VideoResult process(Events.VideoRequested job) throws Exception {
        Path work = Files.createTempDirectory("fiapx-");
        try {
            Path source = work.resolve("video");
            Path zip = work.resolve("frames.zip");
            storage.download(job.sourceKey(), source);
            int count = extractor.extract(source, zip);
            String key = "outputs/" + job.videoId() + "/" + job.attemptId() + "/frames.zip";
            storage.upload(key, zip);
            return new Events.VideoResult(job.videoId(), job.attemptId(), "COMPLETED", key, count, null);
        } finally {
            try (var files = Files.walk(work)) {
                files.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try { Files.deleteIfExists(path); } catch (Exception ignored) { }
                });
            } catch (Exception ignored) { }
        }
    }
}
