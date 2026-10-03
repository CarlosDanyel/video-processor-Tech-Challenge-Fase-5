package com.fiapx.processor.domain;
import java.util.UUID;
public final class Events {
    private Events() {}
    public record VideoRequested(UUID videoId, UUID attemptId, String sourceKey) {}
    public record VideoResult(UUID videoId, UUID attemptId, String status, String outputKey, Integer frameCount, String errorMessage) {}
}
