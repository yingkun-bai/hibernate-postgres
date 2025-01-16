package app.config;

import app.HibernateBatchingExample;
import app.NativeJdbcBatchingExample;
import app.ReactiveStreamProcessingExamples;
import app.SimpleStreamProcessingExamples;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DatabaseConfig.class)
public class AppConfig {
	@Bean
	public SimpleStreamProcessingExamples simpleStreamProcessingExamples(SessionFactory sessionFactory) {
		return new SimpleStreamProcessingExamples(sessionFactory);
	}

	@Bean
	public ReactiveStreamProcessingExamples reactiveStreamProcessingExamples(SessionFactory sessionFactory) {
		return new ReactiveStreamProcessingExamples(sessionFactory);
	}

	@Bean
	public HibernateBatchingExample hibernateBatchingExample(SessionFactory sessionFactory) {
		return new HibernateBatchingExample(sessionFactory);
	}


	@Bean
	public NativeJdbcBatchingExample nativeJdbcBatchingExample() {
		return new NativeJdbcBatchingExample();
	}
}
