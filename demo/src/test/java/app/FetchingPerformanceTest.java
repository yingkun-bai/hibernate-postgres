package app;

import app.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Slf4j
class FetchingPerformanceTest {
    private static final int BATCH_SIZE = 100; // Number of entities per batch
    private static final int PARALLELISM = 4;  // Number of parallel threads

    private static ReactiveStreamProcessingExamples sut;

    @BeforeAll
    static void setUpBeforeClass() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        sut = context.getBean(ReactiveStreamProcessingExamples.class);
    }

    @Test
    void fetch_size_memory_examples() {
        StepVerifier.create(Flux.fromStream(sut.generateSourceStreamWithoutFetchSize())          // Create a Flux from the source stream
                        .buffer(BATCH_SIZE)                              // Collect entities into batches of size BATCH_SIZE
                        .flatMap(batch -> sut.processBatchAsync(batch), PARALLELISM)// Process each batch asynchronously
                        .doOnComplete(() -> log.info("Processing complete"))
                        .doOnError(e -> log.error("Error during processing", e)))
                .verifyComplete();
    }

    @Test
    void long_running_processing_examples() {
        StepVerifier.create(Flux.fromStream(sut.generateSourceStream())
                        .subscribeOn(Schedulers.boundedElastic())// Create a Flux from the source stream
                        // Create a Flux from the source stream
                        .buffer(BATCH_SIZE)                              // Collect entities into batches of size BATCH_SIZE
                        .flatMap(batch -> sut.processBatchAsync(batch), PARALLELISM)// Process each batch asynchronously
                        .doOnComplete(() -> log.info("Processing complete"))
                        .doOnError(e -> log.error("Error during processing", e)))
                .verifyComplete();
    }

    @Test
    void streaming_from_chronicle_examples() {
        StepVerifier.create(Flux.fromStream(sut.generateSourceStreamFromChronicleSet())
                        .subscribeOn(Schedulers.boundedElastic())// Create a Flux from the source stream
                        .buffer(BATCH_SIZE)                              // Collect entities into batches of size BATCH_SIZE
                        .flatMap(batch -> sut.processBatchAsync(batch), PARALLELISM)// Process each batch asynchronously
                        .doOnComplete(() -> log.info("Processing complete"))
                        .doOnError(e -> log.error("Error during processing", e)))

                .verifyComplete();
    }


}
