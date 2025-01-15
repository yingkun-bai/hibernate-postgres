package app.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

/**
 * Proxy Database is used to see database round trips
 */
@Configuration
public class DatabaseConfig {

	@Bean
	public DataSource dataSource() {

		// Here we use a connection pool from tomcat..you can choose Hikari or
		org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");

		// Use this URL for connecting from the host
		dataSource.setUrl("jdbc:postgresql://localhost:5432/demo?reWriteBatchedInserts=true");

		dataSource.setUsername("user");
		dataSource.setPassword("password");
		dataSource.setDefaultAutoCommit(false);

		dataSource.setConnectionProperties("reWriteBatchedInserts=true");
		Formatter formatter = FormatStyle.BASIC.getFormatter();
		// Create ProxyDataSource, overhead is mainly around logs. Can be improved on log4j side.
		return ProxyDataSourceBuilder
				.create(dataSource)
				.formatQuery(formatter::format)
				.multiline()
				.logQueryBySlf4j(SLF4JLogLevel.INFO)
				.build();

	}

	@Bean
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan("app/entity");
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		return txManager;
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.put("hibernate.hbm2ddl.auto", "update"); //Not recommended for prod!! just for demo purpose
		//properties.put("hibernate.jdbc.batch_size", "10"); You can enable global batch size here, by default, it's disabled
		//This is to enable hibernate statistics generation. Note that, this will also enable StatisticalLoggingSessionEventListener logging,
		// which is sometimes annoying.
		// <Logger name="org.hibernate.engine.internal.StatisticalLoggingSessionEventListener" level="OFF" additivity="false"/>
		properties.put("hibernate.generate_statistics", "true");
		return properties;
	}


}
