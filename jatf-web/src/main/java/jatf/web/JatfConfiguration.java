package jatf.web;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariDataSource;

import jatf.api.constraints.ConstraintsService;

@Configuration
@PropertySource("classpath:application.properties")
public class JatfConfiguration {

	@Autowired
	private ConstraintsService constraintsService;

	@Bean
	public CommandLineRunner updateConstraints() {
		return args -> constraintsService.updateConstraints();
	}

	@Primary
	@Bean(name = "dataSource")
	@ConfigurationProperties("spring.datasource")
	public DataSource dataSource() {
		HikariDataSource dataSource = DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.build();
		dataSource.setConnectionTestQuery("SELECT 1");
		return dataSource;
	}
}
