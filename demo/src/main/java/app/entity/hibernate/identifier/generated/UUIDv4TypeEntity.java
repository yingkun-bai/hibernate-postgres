package app.entity.hibernate.identifier.generated;

import app.entity.hibernate.identifier.IdentifiableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Batch size has no impact for the performance for this generation type
 * Not recommended to use as it impacts indexing.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "UUID_V4_ENTITY")
public class UUIDv4TypeEntity implements IdentifiableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID uuid;
	private String text;

	public UUIDv4TypeEntity(String text) {
		this.text = text;
	}
}