package com.tpo.unoMas.config;

// import java.util.Base64;
// import javax.crypto.SecretKey;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable()) // útil si estás usando Postman
        .authorizeHttpRequests(authz -> authz
            .anyRequest().permitAll()); // Sin autenticación - Acceso libre a todos los endpoints
    return http.build();
	}

	// AUTENTICACIÓN JWT DESACTIVADA
	// Descomenta estos métodos cuando quieras activar autenticación
	
	/*
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("saludos/hola", "auth/login");
	}

	@Bean
	public JwtAuthFilter jwtAuth() {
		return new JwtAuthFilter(secretKey());
	}

	@Bean
	public SecretKey secretKey() {
		SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		byte[] encodedKey = secretKey.getEncoded();
		String encodedKeyBase64 = Base64.getEncoder().encodeToString(encodedKey);

		// Registro de la clave secreta (solo para fines de depuración)
		System.out.println("Secret Key (Base64): " + encodedKeyBase64);

		return secretKey;
	}
	*/
}
