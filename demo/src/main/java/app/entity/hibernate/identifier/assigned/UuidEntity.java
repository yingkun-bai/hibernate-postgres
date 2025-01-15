package app.entity.hibernate.identifier.assigned;

import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <a href="https://github.com/f4b6a3/uuid-creator">UUID-creator</a>
 * When to Use UUID v6:
 * Index Efficiency: If you're using a database that relies on B-tree indexing (which is common in PostgreSQL, MySQL, etc.), UUID v6 is a better choice over UUID v4 or even UUID v1. It keeps the UUIDs more sequential, reducing fragmentation and improving index performance.
 * Large Datasets: For applications with frequent inserts into large datasets, the sequential nature of UUID v6 helps maintain better performance over time.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class UuidEntity {
	@Id
	private UUID id;
	private String text;

	public UuidEntity(String text) {
		this.id = UuidCreator.getTimeOrdered();
		this.text = text;
	}
}