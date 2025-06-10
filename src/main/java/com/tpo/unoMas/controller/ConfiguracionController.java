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
     * Crear datos de prueba para el sistema
     */
    @PostMapping("/datos-prueba")
    public ResponseEntity<?> crearDatosPrueba() {
        try {
            // Crear zonas de prueba si no existen
            if (zonaRepository.count() == 0) {
                // Crear ubicaciones
                Ubicacion ubicacionPalermo = new Ubicacion();
                ubicacionPalermo.setLatitud(-34.5875);
                ubicacionPalermo.setLongitud(-58.4217);
                
                Ubicacion ubicacionBelgrano = new Ubicacion();
                ubicacionBelgrano.setLatitud(-34.5631);
                ubicacionBelgrano.setLongitud(-58.4550);
                
                Ubicacion ubicacionSanTelmo = new Ubicacion();
                ubicacionSanTelmo.setLatitud(-34.6219);
                ubicacionSanTelmo.setLongitud(-58.3731);

                // Crear zonas usando el constructor correcto
                Zona zona1 = new Zona("Palermo", "CABA", ubicacionPalermo);
                zonaRepository.save(zona1);

                Zona zona2 = new Zona("Belgrano", "CABA", ubicacionBelgrano);
                zonaRepository.save(zona2);

                Zona zona3 = new Zona("San Telmo", "CABA", ubicacionSanTelmo);
                zonaRepository.save(zona3);
            }

            // Crear deportes de prueba si no existen
            if (deporteRepository.count() == 0) {
                // Usar el constructor correcto de Deporte
                Deporte futbol = new Deporte("Fútbol", "Deporte de equipo jugado con los pies");
                deporteRepository.save(futbol);

                Deporte tenis = new Deporte("Tenis", "Deporte de raqueta individual o por parejas");
                deporteRepository.save(tenis);

                Deporte basquet = new Deporte("Básquet", "Deporte de equipo jugado con las manos");
                deporteRepository.save(basquet);
            }

            return ResponseEntity.ok(Map.of(
                "mensaje", "Datos de prueba creados exitosamente",
                "zonas", zonaRepository.count(),
                "deportes", deporteRepository.count()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al crear datos de prueba",
                "detalle", e.getMessage()
            ));
        }
    }
} 