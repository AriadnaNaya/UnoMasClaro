package com.tpo.unoMas.demo;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.estado.*;
import java.time.LocalDateTime;

public class PartidoStateDemo {

    public static void main(String[] args) {
        System.out.println("🏆 DEMO DEL PATRÓN STATE EN PARTIDO 🏆");
        System.out.println("=====================================\n");

        // Crear datos de prueba
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(-34.6036840);
        ubicacion.setLongitud(-58.3815591);
        
        Zona zona = new Zona("Palermo", "CABA", ubicacion);
        Deporte deporte = new Deporte("Fútbol 5", "Fútbol de salón", 10);
        
        // Crear jugadores
        Jugador organizador = crearJugador("Ana García", "ana@email.com", zona);
        Jugador jugador1 = crearJugador("Carlos López", "carlos@email.com", zona);
        Jugador jugador2 = crearJugador("María Pérez", "maria@email.com", zona);
        Jugador jugador3 = crearJugador("Diego Martín", "diego@email.com", zona);
        
        // Crear partido
        Partido partido = new Partido();
        partido.setTitulo("Partido del Sábado");
        partido.setFechaHora(LocalDateTime.now().plusDays(1));
        partido.setZona(zona);
        partido.setDeporte(deporte);
        partido.setNivel(Nivel.INTERMEDIO);
        partido.setOrganizador(organizador);
        partido.setDuracionMinutos(90);
        
        System.out.println("📋 Información del Partido:");
        System.out.println("   Título: " + partido.getTitulo());
        System.out.println("   Fecha: " + partido.getFechaHora().toLocalDate());
        System.out.println("   Zona: " + zona.getBarrio() + ", " + zona.getPartido());
        System.out.println("   Deporte: " + deporte.getNombre());
        System.out.println("   Organizador: " + organizador.getNombre());
        
        // Demostrar el patrón State
        mostrarEstado(partido, "ESTADO INICIAL");
        
        // 1. Agregar primer jugador
        System.out.println("\n🟡 Agregando primer jugador...");
        partido.agregarJugador(jugador1);
        mostrarEstado(partido, "DESPUÉS DE AGREGAR JUGADOR 1");
        
        // 2. Agregar segundo jugador (alcanza mínimo)
        System.out.println("\n🟢 Agregando segundo jugador (alcanza mínimo)...");
        partido.agregarJugador(jugador2);
        mostrarEstado(partido, "DESPUÉS DE AGREGAR JUGADOR 2");
        
        // 3. Agregar tercer jugador
        System.out.println("\n➕ Agregando tercer jugador...");
        partido.agregarJugador(jugador3);
        mostrarEstado(partido, "DESPUÉS DE AGREGAR JUGADOR 3");
        
        // 4. Remover un jugador (volver atrás en estado)
        System.out.println("\n➖ Removiendo un jugador...");
        partido.removerJugador(jugador3);
        mostrarEstado(partido, "DESPUÉS DE REMOVER JUGADOR");
        
        // 5. Agregar jugador de nuevo y confirmar asistencias
        System.out.println("\n➕ Agregando jugador nuevamente...");
        partido.agregarJugador(jugador3);
        mostrarEstado(partido, "JUGADOR AGREGADO NUEVAMENTE");
        
        // 6. Confirmar asistencias
        System.out.println("\n✅ Confirmando asistencias...");
        partido.confirmarAsistencia(jugador1);
        System.out.println("   " + jugador1.getNombre() + " confirmó asistencia");
        mostrarEstado(partido, "DESPUÉS DE 1 CONFIRMACIÓN");
        
        partido.confirmarAsistencia(jugador2);
        System.out.println("   " + jugador2.getNombre() + " confirmó asistencia");
        mostrarEstado(partido, "DESPUÉS DE 2 CONFIRMACIONES");
        
        partido.confirmarAsistencia(jugador3);
        System.out.println("   " + jugador3.getNombre() + " confirmó asistencia");
        mostrarEstado(partido, "TODAS LAS CONFIRMACIONES");
        
        // 7. Iniciar partido
        System.out.println("\n🏁 Iniciando partido...");
        partido.iniciar();
        mostrarEstado(partido, "PARTIDO INICIADO");
        
        // 8. Finalizar partido
        System.out.println("\n🏆 Finalizando partido...");
        partido.finalizar();
        mostrarEstado(partido, "PARTIDO FINALIZADO");
        
        // Demostrar manejo de excepciones
        System.out.println("\n❌ DEMOSTRANDO MANEJO DE EXCEPCIONES:");
        demostrarExcepciones();
        
        System.out.println("\n🎉 ¡Demo completada exitosamente!");
        System.out.println("El patrón State permite que el partido cambie su comportamiento");
        System.out.println("según su estado interno, manteniendo una interfaz consistente.");
    }
    
    private static Jugador crearJugador(String nombre, String email, Zona zona) {
        Jugador jugador = new Jugador();
        jugador.setNombre(nombre);
        jugador.setEmail(email);
        jugador.setPassword("password123");
        jugador.setZona(zona);
        return jugador;
    }
    
    private static void mostrarEstado(Partido partido, String momento) {
        System.out.println("\n📊 " + momento + ":");
        System.out.println("   Estado: " + partido.getEstado().getClass().getSimpleName());
        System.out.println("   Jugadores inscritos: " + partido.getJugadores().size() + "/" + partido.getDeporte().getCantidadJugadores());
        System.out.println("   Jugadores confirmados: " + partido.getJugadoresConfirmados().size());
        
        if (!partido.getJugadores().isEmpty()) {
            System.out.print("   Lista de jugadores: ");
            partido.getJugadores().forEach(j -> System.out.print(j.getNombre() + " "));
            System.out.println();
        }
    }
    
    private static void demostrarExcepciones() {
        // Crear un partido simple para demostrar excepciones
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(-34.6036840);
        ubicacion.setLongitud(-58.3815591);
        
        Zona zona = new Zona("Centro", "CABA", ubicacion);
        Deporte deporte = new Deporte("Básquet", "Deporte de equipo", 10);
        
        Partido partidoTest = new Partido();
        partidoTest.setTitulo("Partido de Prueba");
        partidoTest.setFechaHora(LocalDateTime.now().plusDays(1));
        partidoTest.setZona(zona);
        partidoTest.setDeporte(deporte);
        partidoTest.setNivel(Nivel.PRINCIPIANTE);
        partidoTest.setDuracionMinutos(60);
        
        // Intentar iniciar sin estar confirmado
        try {
            partidoTest.iniciar();
        } catch (IllegalStateException e) {
            System.out.println("   ✓ Exception capturada: " + e.getMessage());
        }
        
        // Intentar finalizar sin estar en juego
        try {
            partidoTest.finalizar();
        } catch (IllegalStateException e) {
            System.out.println("   ✓ Exception capturada: " + e.getMessage());
        }
    }
} 