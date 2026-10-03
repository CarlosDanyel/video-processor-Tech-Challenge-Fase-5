package Tech_Challenge_Fase_5.video_processor_Tech_Challenge_Fase_5.adapter;
import java.io.IOException;
import Tech_Challenge_Fase_5.video_processor_Tech_Challenge_Fase_5.application.port.FrameExtractorPort;
import java.nio.file.*;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.stereotype.Component;
@Component
public class FrameExtractor implements FrameExtractorPort {
    public int extract(Path video, Path zip) throws Exception {
        Path frames = Files.createDirectory(video.getParent().resolve("frames"));
        Path log = video.getParent().resolve("ffmpeg.log");
        var process = new ProcessBuilder("ffmpeg", "-nostdin", "-i", video.toString(), "-vf", "fps=1", "-y",
            frames.resolve("frame_%06d.png").toString()).redirectErrorStream(true).redirectOutput(log.toFile()).start();
        if (!process.waitFor(20, TimeUnit.MINUTES)) {
            process.destroyForcibly();
            throw new IOException("Video processing timed out");
        }
        if (process.exitValue() != 0) throw new IOException("FFmpeg could not decode this video");
        List<Path> images;
        try (var files = Files.list(frames)) {
            images = files.filter(f -> f.getFileName().toString().endsWith(".png"))
                .sorted(Comparator.comparing(Path::toString)).toList();
        }
        if (images.isEmpty()) throw new IOException("No frames were extracted");
        try (var stream = new ZipOutputStream(Files.newOutputStream(zip))) {
            for (Path image : images) {
                stream.putNextEntry(new ZipEntry(image.getFileName().toString()));
                Files.copy(image, stream);
                stream.closeEntry();
            }
        }
        return images.size();
    }
}
