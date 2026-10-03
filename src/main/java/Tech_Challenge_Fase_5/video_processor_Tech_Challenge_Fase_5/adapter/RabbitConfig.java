package Tech_Challenge_Fase_5.video_processor_Tech_Challenge_Fase_5.adapter;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitConfig {
    @Bean DirectExchange exchange() { return new DirectExchange("fiapx.events", true, false); }
    @Bean DirectExchange deadExchange() { return new DirectExchange("fiapx.dead", true, false); }
    @Bean Queue jobs() { return QueueBuilder.durable("video.jobs").deadLetterExchange("fiapx.dead").deadLetterRoutingKey("video.jobs").build(); }
    @Bean Queue results() { return QueueBuilder.durable("video.results").deadLetterExchange("fiapx.dead").deadLetterRoutingKey("video.results").build(); }
    @Bean Queue deadJobs() { return QueueBuilder.durable("video.jobs.dead").build(); }
    @Bean Queue deadResults() { return QueueBuilder.durable("video.results.dead").build(); }
    @Bean Binding jobsBinding() { return BindingBuilder.bind(jobs()).to(exchange()).with("video.requested"); }
    @Bean Binding resultsBinding() { return BindingBuilder.bind(results()).to(exchange()).with("video.result"); }
    @Bean Binding deadJobsBinding() { return BindingBuilder.bind(deadJobs()).to(deadExchange()).with("video.jobs"); }
    @Bean Binding deadResultsBinding() { return BindingBuilder.bind(deadResults()).to(deadExchange()).with("video.results"); }
}
