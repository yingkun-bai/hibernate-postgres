package app;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class NativeJdbcBatchingExample implements Runnable {
	private static final String INSERT_SQL_QUERY = "INSERT INTO orders (id, amount) VALUES (?, ?)";

	// Method to clean up the table before inserting new rows
	private static void cleanUpTable(Connection connection) throws SQLException {
		String deleteSQL = "DELETE FROM orders";
		try (Statement stmt = connection.createStatement()) {
			int rowsDeleted = stmt.executeUpdate(deleteSQL);
			connection.commit();  // Commit the cleanup operation
			log.info("Table cleaned up. Rows deleted: {}", rowsDeleted);
		}
	}

	private static void executeInBatches(Connection connection) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(NativeJdbcBatchingExample.INSERT_SQL_QUERY)) {
			int batchSize = 50;  // Define the batch size
			int count = 0;

			for (int i = 1; i <= 1000; i++) {
				ps.setInt(1, i);
				ps.setBigDecimal(2, new java.math.BigDecimal("100.00"));
				ps.addBatch();                   // Add to batch

				if (++count % batchSize == 0) {
					ps.executeBatch();           // Execute the batch every 50 records
					connection.commit();         // Commit the transaction
					log.info("Batch executed and committed.");
				}
			}

			ps.executeBatch();  // Execute the remaining records
			connection.commit();  // Final commit
			log.info("Final batch executed and committed.");

		}
		catch (SQLException e) {
			connection.rollback();
			log.error("SQL Exception: {}", e.getMessage());
		}
	}

	// Used for slides only
	private static void executeInBatchesCodeExample(Connection connection) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(NativeJdbcBatchingExample.INSERT_SQL_QUERY)) {

			for (int i = 1; i <= 50; i++) {
				ps.setInt(1, i);
				ps.setBigDecimal(2, new java.math.BigDecimal("100.00"));
				ps.addBatch();                   // Add to batch
			}
			ps.executeBatch();
			connection.commit();
			log.info("Batch executed and committed.");

		}
		catch (SQLException e) {
			connection.rollback();
			log.error("SQL Exception: {}", e.getMessage());
		}
	}

	public void run() {
		String url = "jdbc:postgresql://localhost:5432/demo";
		String user = "user";
		String password = "password";

		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			connection.setAutoCommit(false);  // Disable auto-commit for batching
			cleanUpTable(connection);
			executeInBatches(connection);
		}
		catch (SQLException e) {

			log.error("Error while executing batch", e);
		}
	}
}
