package com.tpo.unoMas.controller;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpo.unoMas.model.entity.UsuarioDTO;
import com.tpo.unoMas.service.IUsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final int EXPIRATION_TIME_IN_MIN = 60;
	
	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private SecretKey secretKey; // Inyecta la clave secreta

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UsuarioDTO credentials) {
		// Validar las credenciales aquí (puedes usar Spring Security u otros
		// mecanismos)
		if (usuarioService.findUser(credentials.getUsername(), credentials.getPassword()) != null) {
			// Crear el token JWT
			String token = Jwts.builder().setSubject(credentials.getUsername()).setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MIN * 60 * 1000))
					.signWith(secretKey, SignatureAlgorithm.HS256).compact();

			return new ResponseEntity<>(token, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Credenciales inválidas.", HttpStatus.UNAUTHORIZED);
		}
	}

}
