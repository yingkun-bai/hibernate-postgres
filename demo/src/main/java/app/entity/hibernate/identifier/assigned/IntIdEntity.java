package app.entity.hibernate.identifier.assigned;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "INT_ID_ENTITY")
public class IntIdEntity {
	@Id
	private int id;
	private String text;

}