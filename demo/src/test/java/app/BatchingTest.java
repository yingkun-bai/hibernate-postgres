package app;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

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

	@ParameterizedTest(name = "{index}: Writing {0} rows in batches of {1}")
	@CsvSource({
			"400, 1, (Smallest dataset without batching)",
			"400, 25, (Small dataset, small batch size)",
			"400, 50, (Small dataset, moderate batching)",
			"400, 100, (Small dataset, larger batch size)",
			"400, 200, (Small dataset, near full batch)",
			"400, 400, (Small dataset, full batch processing)",

			"400000, 1, (Large dataset without batching, worst case)",
			"400000, 50, (Large dataset, small batch size)",
			"400000, 100, (Large dataset, moderate batching)",
			"400000, 200, (Large dataset, increasing batch size)",
			"400000, 400, (Large dataset, testing a higher batch size)",
			"400000, 800, (Large dataset, testing an even larger batch size)"
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

	@Test
	void small_batch_size_profile() {
		sut.createEntitiesInBatchSize(400_000, 25);
	}

	@Test
	void bit_batch_size_profile() {
		sut.createEntitiesInBatchSize(400_000, 800);
	}
}
