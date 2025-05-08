package com.mx.grp.porcentaje_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class PorcentajeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PorcentajeApiApplication.class, args);
	}

}
