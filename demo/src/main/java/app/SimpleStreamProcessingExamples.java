package app;

import java.util.stream.Stream;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

import app.entity.hibernate.identifier.assigned.UuidEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequiredArgsConstructor
public class SimpleStreamProcessingExamples {

	private final SessionFactory sessionFactory;

	private Stream<UuidEntity> generateSourceStream() {
		StatelessSession session = sessionFactory.openStatelessSession();
		return session.createQuery("FROM UuidEntity", UuidEntity.class).stream()
				.onClose(session::close); // Ensure session closes when the stream is closed
	}

	public void processStreamWithSlowIOIntermediary() {
		try (Stream<UuidEntity> stream = generateSourceStream().parallel()) { // Use parallel stream for concurrent processing
			stream.forEach(this::processEntity);
		}
	}

	private void processEntity(UuidEntity entity) {
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


