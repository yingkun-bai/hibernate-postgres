package app.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity used for JDBC example only
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Orders {
	@Id
	private int id;
	private BigDecimal amount;
}
