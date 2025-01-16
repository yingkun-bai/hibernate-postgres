package app.entity.hibernate.identifier.generated;

import app.entity.hibernate.identifier.IdentifiableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Batch size has no impact for the performance for this generation type, as identity disables batching
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "IDENTITY_ENTITY")
public class IdentityTypeEntity implements IdentifiableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String text;

	public IdentityTypeEntity(String text) {
		this.text = text;
	}
}