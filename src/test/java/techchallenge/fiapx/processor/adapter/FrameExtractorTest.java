package techchallenge.fiapx.processor.adapter;
import java.nio.file.*;
import java.util.zip.ZipFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
class FrameExtractorTest {
    @TempDir Path directory;
    @Test void extractsOnePngPerSecondIntoZip() throws Exception {
        Path video = directory.resolve("clip.mp4");
        var creator = new ProcessBuilder("ffmpeg", "-f", "lavfi", "-i", "color=c=blue:s=16x16:d=2", "-y", video.toString())
            .redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.DISCARD).start();
        assertEquals(0, creator.waitFor());
        Path zip = directory.resolve("frames.zip");
        int count = new FrameExtractor().extract(video, zip);
        try (var archive = new ZipFile(zip.toFile())) {
            assertEquals(2, count);
            assertEquals(count, archive.size());
            assertNotNull(archive.getEntry("frame_000001.png"));
        }
    }
    @Test void invalidVideoFails() throws Exception {
        Path invalid = directory.resolve("invalid.mp4");
        Files.writeString(invalid, "not a video");
        assertThrows(Exception.class, () -> new FrameExtractor().extract(invalid, directory.resolve("invalid.zip")));
    }
}
