package app;

import static app.util.MessageGenerator.generateMessagesList;
import static app.util.MessageGenerator.generateRandomString;

import java.util.List;
import java.util.stream.IntStream;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import app.entity.hibernate.identifier.IdentifiableEntity;
import app.entity.hibernate.identifier.assigned.IntIdEntity;
import app.entity.hibernate.identifier.assigned.UuidEntity;
import jakarta.persistence.GenerationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * begin;
 * insert into ...
 * insert into ...
 * insert into ...
 * // here, three inserts have been done on the database. But they will only be made
 * // definitively persistent at commit time
 * ...
 * commit;
 * Note that
 */
@RequiredArgsConstructor
@Slf4j
public class HibernateBatchingExample {

	public final SessionFactory sessionFactory;

	public void createEntitiesInBatchSize(int total, int batchSize) {
		List<UuidEntity> uuidEntityList = IntStream.range(1, total + 1)
				.mapToObj(integer -> new UuidEntity(generateRandomString())).toList();
		try (StatelessSession statelessSession = sessionFactory.openStatelessSession()) {
			statelessSession.setJdbcBatchSize(batchSize);
			Transaction transaction = statelessSession.beginTransaction();
			uuidEntityList.forEach(statelessSession::insert);
			transaction.commit();
		}
	}

	public void createEntitiesWithBatchedTransaction(int total, int batchSize) {
		// Prepare the insert list
		List<UuidEntity> toInsertList = IntStream.range(0, total)
				.mapToObj(integer -> new UuidEntity(generateRandomString())).toList();
		// Calculate the number of transactions
		int count = (total + batchSize) / batchSize;
		log.info("We will have in total {} transactions", count);
		try (StatelessSession statelessSession = sessionFactory.openStatelessSession()) {
			statelessSession.setJdbcBatchSize(batchSize);
			for (int i = 0; i < count; i++) {
				// start and end index to fetch the corresponding entries to insert
				int startIndex = i * batchSize;
				int endIndex = Math.min(startIndex + batchSize, total);
				statelessSession.beginTransaction();
				for (int j = startIndex; j < endIndex; j++) {
					statelessSession.insert(toInsertList.get(j));
				}
				statelessSession.getTransaction().commit();

			}

		}
	}


	public void createEntitiesInBatchByGenerationType(int total, int batchSize, GenerationType type) {
		List<IdentifiableEntity> toInsert = generateMessagesList(total, type);
		// Note, if you do inStatelessSession only, hibernate will wrap each operation in a transaction.
		sessionFactory.inStatelessTransaction(statelessSession -> {
			statelessSession.setJdbcBatchSize(batchSize);
			toInsert.forEach(statelessSession::insert);
		});
	}

	public void createMixedEntities(int total, int batchSize) {
		List<UuidEntity> uuidEntityList = IntStream.range(1, total + 1)
				.mapToObj(integer -> new UuidEntity(generateRandomString())).toList();
		List<IntIdEntity> intIdEntityList = IntStream.range(1, total + 1)
				.mapToObj(integer -> new IntIdEntity(integer, generateRandomString())).toList();
		// Hibernate 6.3 new API :)
		sessionFactory.inStatelessTransaction(statelessSession -> {
			statelessSession.setJdbcBatchSize(batchSize);
			for (int i = 0; i < total; i++) {
				statelessSession.insert(uuidEntityList.get(i));
				statelessSession.insert(intIdEntityList.get(i));
			}

		});
	}
}

