package app;

import java.util.List;
import java.util.stream.Stream;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

import app.entity.hibernate.identifier.assigned.UuidEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.openhft.chronicle.set.ChronicleSet;
import net.openhft.chronicle.set.ChronicleSetBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
public class ReactiveStreamProcessingExamples {

	private static final int BATCH_SIZE = 100; // Number of entities per batch
	private static final int PARALLELISM = 4;  // Number of parallel threads

	private final SessionFactory sessionFactory;

	private Stream<UuidEntity> generateSourceStream() {
		StatelessSession session = sessionFactory.openStatelessSession();
		return session.createQuery("FROM UuidEntity", UuidEntity.class)
				.setFetchSize(100) // very important if you are streaming a large data set
				.stream()
				.onClose(session::close); // Ensure the session closes when the stream is closed
	}
	private Stream<UuidEntity> generateSourceStreamFromChronicleSet() {
		// Create ChronicleSet (off-heap, persisted, or in-memory as needed)
		ChronicleSet<UuidEntity> chronicleSet = ChronicleSetBuilder
				.of(UuidEntity.class)
				.averageKey(new UuidEntity()) // Adjust as needed
				.entries(10_000) // Estimate number of entries
				.create();

		// Stream data into ChronicleSet
		try (Stream<UuidEntity> sourceStream = generateSourceStream()) {
			sourceStream.forEach(chronicleSet::add);
		}

		// Return a stream from the ChronicleSet
		return chronicleSet.stream().onClose(chronicleSet::close);
	}

	public void processAsyncWithParallelism() {
		Flux.fromStream(this::generateSourceStream)          // Create a Flux from the source stream
				.buffer(BATCH_SIZE)                              // Collect entities into batches of size BATCH_SIZE
				.parallel(PARALLELISM)                           // Enable parallel processing with PARALLELISM threads
				.runOn(Schedulers.boundedElastic())              // Use bounded elastic scheduler for I/O-bound tasks
				.flatMap(this::processBatchAsync)// Process each batch asynchronously
				.sequential()                                    // Convert back to sequential Flux for final handling
				.doOnComplete(() -> log.info("Processing complete"))
				.doOnError(e -> log.error("Error during processing", e))
				.subscribe();
	}
	public void processAsyncWithFlatMap() {
		Flux.fromStream(this::generateSourceStream)          // Create a Flux from the source stream
				.buffer(BATCH_SIZE)                              // Collect entities into batches of size BATCH_SIZE
				.flatMap(this::processBatchAsync, PARALLELISM)// Process each batch asynchronously
				.doOnComplete(() -> log.info("Processing complete"))
				.doOnError(e -> log.error("Error during processing", e))
				.subscribe();
	}

	private Mono<Void> processBatchAsync(List<UuidEntity> batch) {
		return Mono.fromRunnable(() -> {
			log.info("Processing batch of size {}", batch.size());
			batch.forEach(entity -> {
				try {
					// Simulate slow I/O processing like network call
					Thread.sleep(1000);
					log.info("Processed entity: {}", entity.getId());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					log.error("Thread interrupted while processing entity", e);
				}
			});
		});
	}
}


