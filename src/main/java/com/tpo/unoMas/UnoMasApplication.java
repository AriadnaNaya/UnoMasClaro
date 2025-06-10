package com.tpo.unoMas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Habilita las tareas programadas para transiciones automáticas
public class UnoMasApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnoMasApplication.class, args);
	}

}
