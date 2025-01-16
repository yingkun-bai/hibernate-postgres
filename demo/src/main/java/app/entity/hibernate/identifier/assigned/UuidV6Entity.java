package app.entity.hibernate.identifier.assigned;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

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
@Table(name = "UUID_V6_ENTITY")
public class UuidV6Entity implements Serializable {
	@Id
	private UUID id;
	private String text;

	public UuidV6Entity(String text) {
		this.id = UuidCreator.getTimeOrdered();
		this.text = text;
	}
}