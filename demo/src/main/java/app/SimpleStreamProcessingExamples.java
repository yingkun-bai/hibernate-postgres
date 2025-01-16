package app;

import app.entity.hibernate.identifier.assigned.UuidV6Entity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

import java.util.stream.Stream;
@Slf4j
@RequiredArgsConstructor
public class SimpleStreamProcessingExamples {

	private final SessionFactory sessionFactory;

	private Stream<UuidV6Entity> generateSourceStream() {
		StatelessSession session = sessionFactory.openStatelessSession();
		return session.createQuery("FROM UuidV6Entity", UuidV6Entity.class).stream()
				.onClose(session::close); // Ensure session closes when the stream is closed
	}

	public void processStreamWithSlowIntermediary() {
		try (Stream<UuidV6Entity> stream = generateSourceStream().parallel()) { // Use parallel stream for concurrent processing
			stream.forEach(this::processEntity);
		}
	}

	private void processEntity(UuidV6Entity entity) {
		try {
			log.info("Processing entity: {}", entity.getId());
			// Simulate a slow I/O operation with sleep (can be replaced with real I/O task)
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Thread was interrupted while processing entity: {}", entity.getId(), e);
		}
	}
}


