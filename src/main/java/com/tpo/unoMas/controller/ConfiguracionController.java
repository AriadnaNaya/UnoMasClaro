package com.tpo.unoMas.controller;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller para configuración y datos maestros
 */
@RestController
@RequestMapping("/api/configuracion")
@CrossOrigin(origins = "*")
@Tag(name = "⚙️ Configuración", description = "Datos maestros del sistema y configuración inicial")
public class ConfiguracionController {

    @Autowired
    private ZonaRepository zonaRepository;
    
    @Autowired
    private DeporteRepository deporteRepository;

    /**
     * Obtener todas las zonas disponibles
     */
    @GetMapping("/zonas")
    public ResponseEntity<?> obtenerZonas() {
        try {
            List<Zona> zonas = zonaRepository.findAll();
            return ResponseEntity.ok(Map.of(
                "zonas", zonas,
                "cantidad", zonas.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener zonas",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Obtener todos los deportes disponibles
     */
    @GetMapping("/deportes")
    public ResponseEntity<?> obtenerDeportes() {
        try {
            List<Deporte> deportes = deporteRepository.findAll();
            return ResponseEntity.ok(Map.of(
                "deportes", deportes,
                "cantidad", deportes.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener deportes",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Obtener todos los niveles disponibles
     */
    @GetMapping("/niveles")
    public ResponseEntity<?> obtenerNiveles() {
        try {
            Nivel[] niveles = Nivel.values();
            return ResponseEntity.ok(Map.of(
                "niveles", niveles,
                "cantidad", niveles.length
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener niveles",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Crear datos de prueba para el sistema (DELEGADO A DemoUtils)
     */
    @PostMapping("/datos-prueba")
    public ResponseEntity<?> crearDatosPrueba() {
        try {
            Map<String, Object> resultado = com.tpo.unoMas.demo.DemoUtils.crearDatosPrueba(zonaRepository, deporteRepository);
            
            if (resultado.containsKey("error")) {
                return ResponseEntity.badRequest().body(resultado);
            }
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al crear datos de prueba",
                "detalle", e.getMessage()
            ));
        }
    }
} 