package app.entity.hibernate.identifier.generated;

import app.entity.hibernate.identifier.IdentifiableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * When Hibernate fetches a block of IDs, it gets the starting value (e.g., 100).
 * It calculates the range of IDs: 100 to 100 + allocation size
 * Hibernate maintains these IDs in memory and assigns them as needed.
 * Only when all IDs in the current block are used does Hibernate make another sequence call.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "SEQUENCE_ENTITY")
public class SequenceTypeEntity implements IdentifiableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
	@SequenceGenerator(name = "message_seq", sequenceName = "message_seq", allocationSize = 100) // default is 50!
	private Long id;
	private String text;

	public SequenceTypeEntity(String text) {
		this.text = text;
	}
}