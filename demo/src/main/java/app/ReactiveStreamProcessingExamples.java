package app;

import app.entity.hibernate.identifier.assigned.UuidV6Entity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.openhft.chronicle.set.ChronicleSet;
import net.openhft.chronicle.set.ChronicleSetBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class ReactiveStreamProcessingExamples {

	private static final int BATCH_SIZE = 100; // Number of entities per batch
	private static final int PARALLELISM = 4;  // Number of parallel threads

	private final SessionFactory sessionFactory;

	public Stream<UuidV6Entity> generateSourceStream() {
		StatelessSession session = sessionFactory.openStatelessSession();
		Transaction transaction = session.beginTransaction(); // Start a transaction

		try {
			return session.createQuery("FROM UuidV6Entity", UuidV6Entity.class)
					.setFetchSize(100) // Important for streaming large datasets
					.stream()
					.onClose(() -> {
						log.info("Closing StatelessSession and transaction!");

						try {
							transaction.commit(); // Commit the transaction when the stream is closed
						} catch (Exception e) {
							transaction.rollback(); // Rollback on failure
							throw e;
						} finally {
							session.close(); // Ensure the session is closed
						}
					});
		} catch (Exception e) {
			transaction.rollback(); // Rollback if an exception occurs
			session.close(); // Close the session
			throw e;
		}
	}

	public Stream<UuidV6Entity> generateSourceStreamWithoutFetchSize() {
		StatelessSession session = sessionFactory.openStatelessSession();
		Transaction transaction = session.beginTransaction(); // Start a transaction

		try {
			return session.createQuery("FROM UuidV6Entity", UuidV6Entity.class)
					.stream()
					.onClose(() -> {
						try {
							transaction.commit(); // Commit the transaction when the stream is closed
							log.info("Transaction committed and StatelessSession closed");
						} catch (Exception e) {
							transaction.rollback(); // Rollback the transaction in case of failure
							log.error("Transaction rollback due to an error", e);
							throw e;
						} finally {
							session.close(); // Ensure the session is closed
						}
					});
		} catch (Exception e) {
			transaction.rollback(); // Rollback the transaction if an error occurs before streaming starts
			session.close(); // Close the session
			throw e;
		}
	}

	public Stream<UuidV6Entity> generateSourceStreamFromChronicleSet() {
		long entriesNum = getEntriesCount();
		// Create ChronicleSet (off-heap, persisted, or in-memory as needed)
		ChronicleSet<UuidV6Entity> chronicleSet = ChronicleSetBuilder
				.of(UuidV6Entity.class)
				.averageKey(new UuidV6Entity()) // Adjust as needed
				.entries(entriesNum) // Estimate number of entries
				.create();

		// Stream data into ChronicleSet
		try (Stream<UuidV6Entity> sourceStream = generateSourceStream()) {
			sourceStream.forEach(chronicleSet::add);
		}

		// Return a stream from the ChronicleSet
		return chronicleSet.stream().onClose(chronicleSet::close);
	}

	private long getEntriesCount() {
		try (StatelessSession session = sessionFactory.openStatelessSession()) {
			return session.createNativeQuery("select count(*) from UUID_V6_ENTITY ", long.class).getSingleResult();
		}
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


	public Mono<Void> processBatchAsync(List<UuidV6Entity> batch) {
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


