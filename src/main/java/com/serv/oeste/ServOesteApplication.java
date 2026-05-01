package com.serv.oeste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.data.jdbc.autoconfigure.DataJdbcRepositoriesAutoConfiguration;

@SpringBootApplication(exclude = DataJdbcRepositoriesAutoConfiguration.class)
public class ServOesteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServOesteApplication.class, args);
	}

}
