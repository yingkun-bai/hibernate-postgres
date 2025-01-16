package app.entity.hibernate.identifier.generated;

import app.entity.hibernate.identifier.IdentifiableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * In PostgreSQL, when AUTO is used, Hibernate typically defaults to the SEQUENCE strategy because PostgreSQL supports sequences natively.
 * Hibernate would generate a sequence like hibernate_sequence (if not customized) and use it to generate IDs.
 * The main advantage of using AUTO is portability. If your application needs to support multiple databases, AUTO allows Hibernate to choose the
 * best strategy based on the target database without requiring manual configuration.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "AUTO_ENTITY")
public class AutoTypeEntity implements IdentifiableEntity {

	@Id
	@GeneratedValue
	private Long id;
	private String text;

	public AutoTypeEntity(String text) {
		this.text = text;
	}
}