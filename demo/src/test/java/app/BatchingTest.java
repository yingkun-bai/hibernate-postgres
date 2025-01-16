package app;

import app.config.AppConfig;
import jakarta.persistence.GenerationType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


class BatchingTest {

	private static HibernateBatchingExample sut;

	@BeforeAll
	static void setUpBeforeClass() {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		sut = context.getBean(HibernateBatchingExample.class);
	}

	@Test
	void create_400_entities_with_batch_size_diff_transactions() {
		sut.createEntitiesWithBatchedTransaction(400, 10);
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 5, 10, 20, 50, 100})
	void create_400_entities_with_batch_size(int batchSize) {
		sut.createEntitiesInBatchSize(400, batchSize);
	}

	@ParameterizedTest
	@CsvSource({
			"400, 25",
			"400, 50",
			"400, 100",
			"400, 200",
			"400, 400",
			"400000, 25",
			"400000, 50",
			"400000, 100",
			"400000, 200",
			"400000, 400",
			"400000,800",
	})
	void compare_total_size_and_batch_size(int totalSize, int batchSize) {
		sut.createEntitiesInBatchSize(totalSize, batchSize);
	}


	@ParameterizedTest
	@EnumSource(value = GenerationType.class, names = {"IDENTITY", "SEQUENCE", "AUTO", "UUID"})
	void will_not_batch_if_mix_statements(GenerationType type) {
		sut.createEntitiesInBatchByGenerationType(400, 50, type);
	}

	@Test
	void will_not_batch_when_mixed_statements() {
		sut.createMixedEntities(40, 10);
	}
}