package dk.purplegreen.musiclibrary.tools.persistence;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


@Configuration
public class TestConfig {
	
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
		.setType(EmbeddedDatabaseType.HSQL)
		.addScript("sql/schema.sql")
		.addScript("sql/data.sql")
		.build();
	}
	
	@Bean 
	public JdbcTemplate jdbcTemplate() {		
		return new JdbcTemplate(dataSource());
	}
	
	@Bean
	public JDBCDAO jdbcDAO() {
		return new JDBCDAO(jdbcTemplate());
	}			
}
