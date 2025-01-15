package app.reactor;

import java.util.stream.Stream;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.query.Query;
import org.reactivestreams.Publisher;

import app.entity.hibernate.identifier.assigned.UuidEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Slf4j
public class EntityProducer {

	private final SessionFactory sessionFactory;

	public Flux<UuidEntity> publish() {
		return Flux.using(sessionFactory::openStatelessSession, this::streamData, StatelessSession::close);

	}

	private Publisher<UuidEntity> streamData(StatelessSession session) {
		String hql = "from UuidEntity";
		Query<UuidEntity> query = session.createQuery(hql, UuidEntity.class).setFetchSize(10);
		return Flux.fromStream(query.stream());
	}

	private Stream<UuidEntity> getCatStream(StatelessSession session) {
		return session.createQuery("from UuidEntity", UuidEntity.class).getResultStream();
	}

}