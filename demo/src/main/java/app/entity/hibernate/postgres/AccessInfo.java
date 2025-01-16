package app.entity.hibernate.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLInetJdbcType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ACCESS_INFO")
public class AccessInfo {
	@Id
	private UUID id;
	//columnDefinition (Optional) The SQL fragment that is used when generating the DDL for the column.
	//Defaults to the generated SQL to create a column of the inferred type.
	@JdbcType(PostgreSQLInetJdbcType.class)
	@Column(name = "ip", columnDefinition = "inet")
	private String ip;
}
