package com.bac.bac;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "BAC API", version = "1.0", description = "BAC Información"))
public class BacApplication {

	public static void main(String[] args) {
		SpringApplication.run(BacApplication.class, args);
	}

}
