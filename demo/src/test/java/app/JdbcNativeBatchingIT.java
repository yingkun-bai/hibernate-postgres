package app;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.config.AppConfig;

class JdbcNativeBatchingIT {
	private static NativeJdbcBatchingExample sut;

	@BeforeAll
	static void setUpBeforeClass() {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		sut = context.getBean(NativeJdbcBatchingExample.class);
	}

	@Test
	void run_test() {
		sut.run();
	}
}
