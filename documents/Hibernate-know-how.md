### When Does Hibernate Use Prepared Statements?

In Hibernate, prepared statements are used automatically behind the scenes for most database interactions. When you use Hibernate’s query methods
or perform CRUD operations, Hibernate typically generates and executes prepared statements, although you may not see them directly.

1. **CRUD Operations**: When you perform `session.save()`, `session.update()`, or `session.delete()`, Hibernate uses prepared statements. For
   example:
   ```java
   User user = new User("John", "Doe");
   session.save(user);
   ```
   The SQL generated might look like:
   ```sql
   INSERT INTO users (first_name, last_name) VALUES (?, ?);
   ```
   The values are passed as parameters using a prepared statement.

2. **Queries with Parameters**: Whether using HQL, Criteria API, or native SQL, queries that involve parameters are executed using prepared
   statements.

```java
Session session = sessionFactory.openSession();
Query<User> query = session.createQuery("FROM User WHERE id = :userId", User.class);
query.

setParameter("userId",1);

User user = query.uniqueResult();
```

In this example:

- Hibernate internally generates a prepared statement like `SELECT * FROM User WHERE id = ?`.
- The `:userId` placeholder is replaced by the value `1` using a prepared statement.

This means that the SQL query is compiled and optimized by the database once, and the parameter is simply passed each time the query is executed,
minimizing overhead.

3. **Batching Operations**: Hibernate also uses prepared statements when batching insert/update operations, which improves performance by reusing
   the same compiled SQL statement multiple times with different parameter sets.

### Benefits of Prepared Statements in Hibernate

- **SQL Injection Protection**: Prepared statements automatically escape parameter values, making it difficult for attackers to inject malicious
  SQL code.
- **Performance Optimization**: Prepared statements are compiled and cached by the database, reducing the overhead of parsing and compiling SQL
  each time the query is executed.
- **Reusability**: For repeated queries or operations, Hibernate reuses the same prepared statement structure with different parameter values,
  which is more efficient than creating a new SQL statement every time.

### Controlling Prepared Statements in Hibernate

Hibernate manages prepared statements automatically, but you can influence their behavior:

1. **Batch Size Configuration**:
   Hibernate can batch multiple insert, update, or delete operations together to reduce round trips and enhance performance. You can configure
   this with:
   ```properties
   hibernate.jdbc.batch_size=20
   ```
   This tells Hibernate to batch up to 20 statements together, using prepared statements to perform the operations more efficiently.

2. **Fetching Strategies**:
   The choice between eager and lazy loading can affect how Hibernate generates prepared statements. Lazy loading (the default) often results in
   additional prepared statements when associations are accessed.

3. **Second-Level Cache**:
   Hibernate’s second-level cache stores entities and query results. This reduces the need for repeated database calls and further minimizes the
   need for repeated round trips or re-executing prepared statements.

### Hibernate Batch Processing: When Does it Execute?

When you configure a batch size in Hibernate (e.g., `hibernate.jdbc.batch_size=20`), it doesn’t necessarily mean that Hibernate will always wait
until exactly 20 statements are accumulated before executing them. There are a few factors that influence when the batch is executed:

1. **Batch Size Trigger**:
   Hibernate will **attempt** to collect the specified batch size (e.g., 20) before executing it. However, it might execute the batch earlier if
   certain conditions are met:
    - If the session is flushed (manually via `session.flush()` or automatically).
    - If the transaction is committed (causing a flush).
    - If there’s a change in the type of operation (e.g., from `INSERT` to `UPDATE`).
    - If you perform an operation that requires data consistency (e.g., querying or fetching an entity).

2. **Timeout or Time-Based Execution**:
   Hibernate itself doesn’t have built-in logic to trigger batch execution based on time. It mainly focuses on the batch size and session
   flushing. However, you can force the batch to execute earlier by manually flushing the session.

3. **JDBC Driver’s Role**:
   While Hibernate is responsible for accumulating and batching the operations, the actual execution is handled by the JDBC driver. Hibernate
   sends the batch to the JDBC driver once the batch size is met or under other triggering conditions. The driver then executes the batch as a
   single database round trip.

### Who Controls the Batch: Hibernate or JDBC?

1. **Hibernate Controls the Batching Logic**:
   Hibernate accumulates and tracks the batch internally until certain conditions are met (like reaching the batch size). It decides when to send
   the batch for execution.

2. **JDBC Executes the Batch**:
   Once Hibernate sends the batch, the JDBC driver takes over and executes it using the `PreparedStatement.executeBatch()` method. This is where
   the actual database round trip happens.

### Example of Batch Execution in Hibernate

Let’s say you have the following configuration:

```properties
hibernate.jdbc.batch_size=20
```

And you’re performing a batch insert:

```java
Session session = sessionFactory.openSession();
Transaction transaction = session.beginTransaction();

for(
int i = 1;
i <=50;i++){
User user = new User("User" + i);
    session.

save(user);

    if(i %10==0){
		session.

flush();  // Optional: Manually flush
        session.

clear();  // Optional: Clear session to avoid memory issues
    }
			}

			transaction.

commit();
session.

close();

```

In this example:

- Hibernate will attempt to batch the inserts in groups of 20. However, if you manually flush or clear the session, it will execute whatever is
  accumulated in the batch, even if the batch isn’t full.

- On committing the transaction, Hibernate will also flush any remaining statements, regardless of whether the batch is complete.

### Scenarios Where Batches Might Not Be Full

1. **Session Flushes**: If you manually or automatically flush the session before the batch size is reached, the batch will be executed early.

2. **Transaction Commit**: On committing a transaction, Hibernate flushes the session, executing any incomplete batch.

3. **Different SQL Operations**: If you switch between different operations (e.g., `INSERT` to `UPDATE`), Hibernate will execute the current
   batch and start a new one for the different operation type.

4. **Entity Interleaving**: If you mix operations on different entities, the batch might be flushed earlier than the configured size.

### JDBC’s Role in Execution

Once Hibernate decides to send the batch for execution, it uses the JDBC driver’s `executeBatch()` method. At this point, the actual
communication with the database happens, and the driver handles sending the batch as a single round trip.

### Conclusion

- Hibernate controls the batching process by accumulating statements until the batch size is reached or until other triggers (like flushes or
  commits) force the batch to execute.
- The batch is executed by the JDBC driver, which sends it as a single request to the database.
- Batches may be executed before they are fully filled if certain conditions are met, but Hibernate itself doesn’t have a time-based mechanism to
  force batch execution.

In summary, while Hibernate manages the batching logic, the actual execution is performed by the JDBC driver, which sends the batch in one or
more round trips depending on the number of operations batched together.