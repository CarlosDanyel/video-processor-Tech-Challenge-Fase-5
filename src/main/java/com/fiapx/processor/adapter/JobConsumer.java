package com.fiapx.processor.adapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiapx.processor.application.VideoProcessingService;
import com.fiapx.processor.domain.Events;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
@Component
public class JobConsumer {
    private final VideoProcessingService processing;
    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper;
    public JobConsumer(VideoProcessingService processing, RabbitTemplate rabbit, ObjectMapper mapper) {
        this.processing = processing; this.rabbit = rabbit; this.mapper = mapper; this.rabbit.setMandatory(true);
    }
    @RabbitListener(queues = "video.jobs", concurrency = "${processor.concurrency:2}")
    public void receive(String payload) throws Exception {
        var job = mapper.readValue(payload, Events.VideoRequested.class);
        send(new Events.VideoResult(job.videoId(), job.attemptId(), "PROCESSING", null, null, null));
        Events.VideoResult outcome;
        try {
            outcome = processing.process(job);
        } catch (Exception e) {
            outcome = new Events.VideoResult(job.videoId(), job.attemptId(), "FAILED", null, null, e.getMessage());
        }
        send(outcome);
    }
    private void send(Events.VideoResult result) throws Exception {
        var confirmation = new CorrelationData();
        rabbit.convertAndSend("fiapx.events", "video.result", mapper.writeValueAsString(result), message -> {
            message.getMessageProperties().setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT);
            return message;
        }, confirmation);
        if (!confirmation.getFuture().get(10, TimeUnit.SECONDS).isAck() || confirmation.getReturned() != null)
            throw new IllegalStateException("Result was not confirmed by RabbitMQ");
    }
}
