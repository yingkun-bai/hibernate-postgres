package app.entity.hibernate.identifier.generated;

import java.util.UUID;

import app.entity.hibernate.identifier.IdentifiableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Batch size has no impact for the performance for this generation type
 * Not recommended to use as it impacts indexing.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class UUIDv4TypeEntity implements IdentifiableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID uuid;
	private String text;

	public UUIDv4TypeEntity(String text) {
		this.text = text;
	}
}