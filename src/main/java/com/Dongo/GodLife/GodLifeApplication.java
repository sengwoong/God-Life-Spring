package com.Dongo.GodLife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GodLifeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GodLifeApplication.class, args);
	}

}
