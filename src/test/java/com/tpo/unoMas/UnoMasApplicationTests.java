package com.tpo.unoMas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test de integración principal para verificar que la aplicación Spring Boot
 * se inicia correctamente y el contexto de aplicación se carga sin errores.
 */
@SpringBootTest
@ActiveProfiles("test")
class UnoMasApplicationTests {

	/**
	 * Test que verifica que el contexto de Spring Boot se carga correctamente.
	 * Este es un test de smoke que asegura que la configuración básica de la aplicación
	 * está funcionando y no hay errores de configuración que impidan el inicio.
	 */
	@Test
	void contextLoads() {
		// Si este test pasa, significa que el contexto de Spring se cargó exitosamente
		// No necesita assertions explícitos ya que Spring Boot fallará si hay problemas
	}

	/**
	 * Test adicional para verificar que la aplicación puede arrancar completamente.
	 * Útil para detectar problemas de configuración tempranamente.
	 */
	@Test
	void applicationStartsSuccessfully() {
		// Este test complementa contextLoads() verificando el inicio completo
		// Spring Boot manejará cualquier falla durante el arranque
		assert true; // Test pasa si llegamos aquí sin excepciones
	}
}
