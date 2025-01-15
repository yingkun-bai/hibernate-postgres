package app.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import app.HibernateBatchingExample;
import app.NativeJdbcBatchingExample;
import app.SimpleStreamProcessingExamples;

@Configuration
@Import(DatabaseConfig.class)
public class AppConfig {
	@Bean
	public SimpleStreamProcessingExamples app(SessionFactory sessionFactory) {
		return new SimpleStreamProcessingExamples(sessionFactory);
	}

	@Bean
	public HibernateBatchingExample catFactory(SessionFactory sessionFactory) {
		return new HibernateBatchingExample(sessionFactory);
	}


	@Bean
	public NativeJdbcBatchingExample jdbcBatchingExample() {
		return new NativeJdbcBatchingExample();
	}
}
