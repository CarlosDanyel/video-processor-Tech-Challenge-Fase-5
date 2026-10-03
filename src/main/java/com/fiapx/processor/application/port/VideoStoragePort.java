package com.fiapx.processor.application.port;
import java.nio.file.Path;
public interface VideoStoragePort {
    void download(String key, Path destination);
    void upload(String key, Path source);
}
