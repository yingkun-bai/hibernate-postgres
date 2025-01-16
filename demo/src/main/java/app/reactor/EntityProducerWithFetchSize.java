package app.reactor;

import app.entity.hibernate.identifier.assigned.UuidV6Entity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.query.Query;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

//TODO
@RequiredArgsConstructor
@Slf4j
public class EntityProducerWithFetchSize {

	private final SessionFactory sessionFactory;

    public Flux<UuidV6Entity> publish() {
		return Flux.using(sessionFactory::openStatelessSession, this::streamData, StatelessSession::close);

	}

    private Publisher<UuidV6Entity> streamData(StatelessSession session) {
        String hql = "from UuidV6Entity";
        Query<UuidV6Entity> query = session.createQuery(hql, UuidV6Entity.class).setFetchSize(10);
		return Flux.fromStream(query.stream());
	}

}