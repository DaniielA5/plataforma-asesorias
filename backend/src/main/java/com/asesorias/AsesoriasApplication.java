package com.asesorias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@ConfigurationPropertiesScan
@SpringBootApplication
public class AsesoriasApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsesoriasApplication.class, args);
	}

}
