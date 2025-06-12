package com.tpo.unoMas.demo;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.adapter.AdapterJavaMail;
import com.tpo.unoMas.model.adapter.NotificacionEmailAdapter;
import com.tpo.unoMas.model.strategy.emparejamiento.*;
import com.tpo.unoMas.model.strategy.notificacion.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * DEMO INTERACTIVO DE STRATEGY PATTERN
 * 
 * Permite al usuario explorar interactivamente las diferentes estrategias:
 * 
 * EMPAREJAMIENTO:
 * - EmparejamientoPorCercania (proximidad geográfica)
 * - EmparejamientoPorNivel (nivel de habilidad)
 * - EmparejamientoPorHistorial (partidos anteriores)
 * 
 * NOTIFICACIONES:
 * - NotificacionEmail (JavaMail)
 * - NotificacionPushFirebase (FCM)
 * 
 * Para ejecutar: java -jar target/unoMas-0.0.1-SNAPSHOT.jar --demo-estrategias
 */
@Component
public class DemoEstrategias implements CommandLineRunner {

    private Scanner scanner;
    private DatosDemo datosDemo;

    @Override
    public void run(String... args) throws Exception {
        if (Arrays.asList(args).contains("--demo-estrategias")) {
            System.out.println("=".repeat(80));
            System.out.println("🎯 DEMO INTERACTIVO - STRATEGY PATTERN");
            System.out.println("=".repeat(80));
            
            ejecutarDemoEstrategias();
        }
    }

    private void ejecutarDemoEstrategias() {
        scanner = new Scanner(System.in);
        
        System.out.println("\n🎯 Bienvenido al Demo Interactivo de Strategy Pattern");
        System.out.println("Podrás comparar diferentes estrategias de emparejamiento y notificaciones.\n");
        
        try {
            datosDemo = crearDatosDemo();
            mostrarMenuPrincipal();
        } catch (Exception e) {
            System.err.println("❌ Error en demo: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private DatosDemo crearDatosDemo() {
        // Crear zonas con diferentes ubicaciones
        Zona palermo = new Zona();
        palermo.setBarrio("Palermo");
        palermo.setPartido("CABA");
        palermo.setUbicacion(new Ubicacion(-34.5875, -58.4217));
        
        Zona belgrano = new Zona();
        belgrano.setBarrio("Belgrano");
        belgrano.setPartido("CABA");
        belgrano.setUbicacion(new Ubicacion(-34.5631, -58.4550));
        
        Zona laPlata = new Zona();
        laPlata.setBarrio("Centro");
        laPlata.setPartido("La Plata");
        laPlata.setUbicacion(new Ubicacion(-34.9214, -57.9544));
        
        // Crear deporte
        Deporte futbol = new Deporte();
        futbol.setNombre("Fútbol 11");
        futbol.setDescripcion("Deporte de equipo");
        futbol.setCantidadJugadores(22);
        
        // Crear organizador
        Jugador organizador = new Jugador("Organizador", "org@email.com", "password123", palermo);
        
        // Crear partido
        Partido partido = new Partido();
        partido.setTitulo("Partido Demo Estrategias");
        partido.setFechaHora(LocalDateTime.now().plusDays(1));
        partido.setZona(palermo);
        partido.setDeporte(futbol);
        partido.setOrganizador(organizador);
        partido.setDuracionMinutos(90);
        partido.setNivel(Nivel.INTERMEDIO);
        
        // Crear jugadores con diferentes características
        List<Jugador> jugadores = new ArrayList<>();
        
        // Jugador 1: Cerca, nivel intermedio, sin historial
        Jugador ana = new Jugador("Ana García", "ana@email.com", "password123", palermo);
        ana.setTokenFCM("ana-fcm-token");
        DeporteJugador djAna = new DeporteJugador();
        djAna.setJugador(ana);
        djAna.setDeporte(futbol);
        djAna.setNivel(Nivel.INTERMEDIO);
        ana.getDeportes().add(djAna);
        jugadores.add(ana);
        
        // Jugador 2: Cerca, nivel principiante, sin historial
        Jugador carlos = new Jugador("Carlos López", "carlos@email.com", "password123", palermo);
        carlos.setTokenFCM("carlos-fcm-token");
        DeporteJugador djCarlos = new DeporteJugador();
        djCarlos.setJugador(carlos);
        djCarlos.setDeporte(futbol);
        djCarlos.setNivel(Nivel.PRINCIPIANTE);
        carlos.getDeportes().add(djCarlos);
        jugadores.add(carlos);
        
        // Jugador 3: Medianamente cerca, nivel intermedio, sin historial
        Jugador laura = new Jugador("Laura Rodríguez", "laura@email.com", "password123", belgrano);
        laura.setTokenFCM("laura-fcm-token");
        DeporteJugador djLaura = new DeporteJugador();
        djLaura.setJugador(laura);
        djLaura.setDeporte(futbol);
        djLaura.setNivel(Nivel.INTERMEDIO);
        laura.getDeportes().add(djLaura);
        jugadores.add(laura);
        
        // Jugador 4: Lejos, nivel avanzado, sin historial
        Jugador diego = new Jugador("Diego Martínez", "diego@email.com", "password123", laPlata);
        diego.setTokenFCM("diego-fcm-token");
        DeporteJugador djDiego = new DeporteJugador();
        djDiego.setJugador(diego);
        djDiego.setDeporte(futbol);
        djDiego.setNivel(Nivel.AVANZADO);
        diego.getDeportes().add(djDiego);
        jugadores.add(diego);
        
        // Jugador 5: Cerca, nivel intermedio, CON historial
        Jugador martin = new Jugador("Martín Fernández", "martin@email.com", "password123", palermo);
        martin.setTokenFCM("martin-fcm-token");
        DeporteJugador djMartin = new DeporteJugador();
        djMartin.setJugador(martin);
        djMartin.setDeporte(futbol);
        djMartin.setNivel(Nivel.INTERMEDIO);
        martin.getDeportes().add(djMartin);
        
        // Simular historial
        martin.agregarAlHistorial(partido); // Martin tiene historial
        
        jugadores.add(martin);
        
        System.out.println("✅ Datos demo creados exitosamente");
        System.out.println("📋 Partido: " + partido.getTitulo());
        System.out.println("👥 Jugadores disponibles: " + jugadores.size());
        
        return new DatosDemo(partido, jugadores);
    }

    private void mostrarMenuPrincipal() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarInformacionGeneral();
            mostrarOpcionesMenu();
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                continuar = procesarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número válido");
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
            
            System.out.println(); // Línea en blanco para separar
        }
    }

    private void mostrarInformacionGeneral() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 DEMO DE STRATEGY PATTERN");
        System.out.println("=".repeat(60));
        System.out.println("📋 Partido: " + datosDemo.partido.getTitulo());
        System.out.println("📍 Ubicación: " + datosDemo.partido.getZona().getBarrio());
        System.out.println("🎯 Nivel requerido: " + datosDemo.partido.getNivel());
        System.out.println("👥 Jugadores disponibles: " + datosDemo.jugadores.size());
        System.out.println("🔧 Estrategia actual: " + datosDemo.partido.getEstrategiaEmparejamiento().getClass().getSimpleName());
    }

    private void mostrarOpcionesMenu() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("🔧 OPCIONES DISPONIBLES:");
        System.out.println("-".repeat(40));
        
        System.out.println("1. 📍 Probar EmparejamientoPorCercania");
        System.out.println("2. 📊 Probar EmparejamientoPorNivel");
        System.out.println("3. 📚 Probar EmparejamientoPorHistorial");
        System.out.println("4. 🔀 Comparar todas las estrategias de emparejamiento");
        System.out.println("5. 📧 Probar NotificacionEmail");
        System.out.println("6. 📱 Probar NotificacionPushFirebase");
        System.out.println("7. 🔔 Probar notificaciones combinadas");
        System.out.println("8. 👥 Ver detalles de jugadores");
        System.out.println("0. 🚪 Salir");
        
        System.out.print("\nSeleccione una opción: ");
    }

    private boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                probarEstrategiaEmparejamientoPorCercania();
                break;
            case 2:
                probarEstrategiaEmparejamientoPorNivel();
                break;
            case 3:
                probarEstrategiaEmparejamientoPorHistorial();
                break;
            case 4:
                compararEstrategiasEmparejamiento();
                break;
            case 5:
                probarNotificacionEmail();
                break;
            case 6:
                probarNotificacionPushFirebase();
                break;
            case 7:
                probarNotificacionesCombinadas();
                break;
            case 8:
                verDetallesJugadores();
                break;
            case 0:
                System.out.println("👋 ¡Gracias por usar el demo de estrategias!");
                return false;
            default:
                System.out.println("❌ Opción no válida");
        }
        return true;
    }

    private void probarEstrategiaEmparejamientoPorCercania() {
        System.out.println("\n📍 ESTRATEGIA: EmparejamientoPorCercania");
        System.out.println("=".repeat(50));
        System.out.println("Esta estrategia ordena los jugadores por proximidad geográfica al partido.");
        
        EstrategiaEmparejamiento estrategia = new EmparejamientoPorCercania();
        datosDemo.partido.cambiarEstrategiaEmparejamiento(estrategia);
        
        List<Jugador> resultado = estrategia.encontrarJugadoresPotenciales(datosDemo.partido, datosDemo.jugadores);
        
        System.out.println("\n🎯 Resultados ordenados por cercanía:");
        for (int i = 0; i < resultado.size(); i++) {
            Jugador jugador = resultado.get(i);
            double distancia = calcularDistancia(datosDemo.partido.getZona().getUbicacion(), jugador.getZona().getUbicacion());
            System.out.println((i+1) + ". " + jugador.getNombre() + " - " + jugador.getZona().getBarrio() + 
                             " (Distancia: " + String.format("%.1f", distancia) + " km)");
        }
    }

    private void probarEstrategiaEmparejamientoPorNivel() {
        System.out.println("\n📊 ESTRATEGIA: EmparejamientoPorNivel");
        System.out.println("=".repeat(50));
        System.out.println("Esta estrategia prioriza jugadores con nivel similar al requerido.");
        System.out.println("Nivel del partido: " + datosDemo.partido.getNivel());
        
        EstrategiaEmparejamiento estrategia = new EmparejamientoPorNivel();
        datosDemo.partido.cambiarEstrategiaEmparejamiento(estrategia);
        
        List<Jugador> resultado = estrategia.encontrarJugadoresPotenciales(datosDemo.partido, datosDemo.jugadores);
        
        System.out.println("\n🎯 Resultados ordenados por nivel:");
        for (int i = 0; i < resultado.size(); i++) {
            Jugador jugador = resultado.get(i);
            Nivel nivelJugador = jugador.getDeportes().isEmpty() ? Nivel.PRINCIPIANTE : 
                jugador.getDeportes().get(0).getNivel();
            boolean coincide = nivelJugador == datosDemo.partido.getNivel();
            String indicador = coincide ? " ✅" : " ⚠️ ";
            System.out.println((i+1) + ". " + jugador.getNombre() + " - Nivel: " + nivelJugador + indicador);
        }
    }

    private void probarEstrategiaEmparejamientoPorHistorial() {
        System.out.println("\n📚 ESTRATEGIA: EmparejamientoPorHistorial");
        System.out.println("=".repeat(50));
        System.out.println("Esta estrategia prioriza jugadores que han jugado anteriormente con el organizador.");
        
        EstrategiaEmparejamiento estrategia = new EmparejamientoPorHistorial();
        datosDemo.partido.cambiarEstrategiaEmparejamiento(estrategia);
        
        List<Jugador> resultado = estrategia.encontrarJugadoresPotenciales(datosDemo.partido, datosDemo.jugadores);
        
        System.out.println("\n🎯 Resultados ordenados por historial:");
        for (int i = 0; i < resultado.size(); i++) {
            Jugador jugador = resultado.get(i);
            boolean tieneHistorial = !jugador.getHistorial().isEmpty();
            String indicador = tieneHistorial ? " 📚 Con historial" : " 🆕 Nuevo";
            System.out.println((i+1) + ". " + jugador.getNombre() + indicador);
        }
    }

    private void compararEstrategiasEmparejamiento() {
        System.out.println("\n🔀 COMPARACIÓN DE ESTRATEGIAS DE EMPAREJAMIENTO");
        System.out.println("=".repeat(60));
        
        EstrategiaEmparejamiento[] estrategias = {
            new EmparejamientoPorCercania(),
            new EmparejamientoPorNivel(),
            new EmparejamientoPorHistorial()
        };
        
        String[] nombres = {
            "Por Cercanía",
            "Por Nivel", 
            "Por Historial"
        };
        
        for (int i = 0; i < estrategias.length; i++) {
            System.out.println("\n📋 " + nombres[i] + ":");
            System.out.println("-".repeat(30));
            
            List<Jugador> resultado = estrategias[i].encontrarJugadoresPotenciales(datosDemo.partido, datosDemo.jugadores);
            
            for (int j = 0; j < Math.min(3, resultado.size()); j++) {
                Jugador jugador = resultado.get(j);
                System.out.println((j+1) + ". " + jugador.getNombre());
            }
            
            if (resultado.size() > 3) {
                System.out.println("   ... y " + (resultado.size() - 3) + " más");
            }
        }
    }

    private void probarNotificacionEmail() {
        System.out.println("\n📧 ESTRATEGIA: NotificacionEmail");
        System.out.println("=".repeat(30));
        
        NotificacionEmailAdapter javaMailAdapter = new AdapterJavaMail();
        INotificacionStrategy estrategiaEmail = new NotificacionEmail(javaMailAdapter);
        
        for (Jugador jugador : datosDemo.jugadores) {
            Notificacion notificacion = new Notificacion(
                "Invitación al partido",
                "Te invitamos a participar en el partido de fútbol en " + datosDemo.partido.getZona().getBarrio(),
                jugador
            );
            
            try {
                estrategiaEmail.enviarNotificacion(notificacion);
                System.out.println("✅ Email enviado a " + jugador.getEmail());
            } catch (Exception e) {
                System.out.println("⚠️  Email simulado para " + jugador.getNombre() + ": " + e.getMessage());
            }
        }
    }

    private void probarNotificacionPushFirebase() {
        System.out.println("\n📱 ESTRATEGIA: NotificacionPushFirebase");
        System.out.println("=".repeat(35));
        
        INotificacionStrategy estrategiaPush = new NotificacionPushFirebase();
        
        for (Jugador jugador : datosDemo.jugadores) {
            Notificacion notificacion = new Notificacion(
                "Invitación al partido",
                "Te invitamos a participar en el partido de fútbol en " + datosDemo.partido.getZona().getBarrio(),
                jugador
            );
            
            if (jugador.getTokenFCM() != null) {
                try {
                    estrategiaPush.enviarNotificacion(notificacion);
                    System.out.println("✅ Push enviado a " + jugador.getNombre());
                } catch (Exception e) {
                    System.out.println("⚠️  Push simulado para " + jugador.getNombre() + ": " + e.getMessage());
                }
            } else {
                System.out.println("❌ " + jugador.getNombre() + " no tiene token FCM");
            }
        }
    }

    private void probarNotificacionesCombinadas() {
        System.out.println("\n🔔 NOTIFICACIONES COMBINADAS");
        System.out.println("=".repeat(30));
        
        NotificacionEmailAdapter javaMailAdapter = new AdapterJavaMail();
        INotificacionStrategy email = new NotificacionEmail(javaMailAdapter);
        INotificacionStrategy push = new NotificacionPushFirebase();
        
        for (Jugador jugador : datosDemo.jugadores) {
            System.out.println("\n👤 " + jugador.getNombre() + ":");
            
            Notificacion notificacion = new Notificacion(
                "Invitación urgente al partido",
                "¡Último llamado para unirte al partido en " + datosDemo.partido.getZona().getBarrio() + "!",
                jugador
            );
            
            // Email
            try {
                email.enviarNotificacion(notificacion);
                System.out.println("   📧 Email: ✅");
            } catch (Exception e) {
                System.out.println("   📧 Email: ⚠️  Simulado");
            }
            
            // Push
            if (jugador.getTokenFCM() != null) {
                try {
                    push.enviarNotificacion(notificacion);
                    System.out.println("   📱 Push: ✅");
                } catch (Exception e) {
                    System.out.println("   📱 Push: ⚠️  Simulado");
                }
            } else {
                System.out.println("   📱 Push: ❌ Sin token");
            }
        }
    }

    private void verDetallesJugadores() {
        System.out.println("\n👥 DETALLES DE JUGADORES");
        System.out.println("=".repeat(50));
        
        for (int i = 0; i < datosDemo.jugadores.size(); i++) {
            Jugador jugador = datosDemo.jugadores.get(i);
            System.out.println("\n" + (i+1) + ". " + jugador.getNombre());
            System.out.println("   📧 Email: " + jugador.getEmail());
            System.out.println("   📍 Zona: " + jugador.getZona().getBarrio() + ", " + jugador.getZona().getPartido());
            
            if (!jugador.getDeportes().isEmpty()) {
                Nivel nivel = jugador.getDeportes().get(0).getNivel();
                System.out.println("   📊 Nivel: " + nivel);
            }
            
            System.out.println("   📱 Token FCM: " + (jugador.getTokenFCM() != null ? "✅" : "❌"));
            System.out.println("   📚 Historial: " + jugador.getHistorial().size() + " partidos");
            
            double distancia = calcularDistancia(datosDemo.partido.getZona().getUbicacion(), jugador.getZona().getUbicacion());
            System.out.println("   📏 Distancia al partido: " + String.format("%.1f", distancia) + " km");
        }
    }

    private double calcularDistancia(Ubicacion ubicacion1, Ubicacion ubicacion2) {
        // Fórmula de Haversine simplificada para demo
        double lat1 = ubicacion1.getLatitud();
        double lon1 = ubicacion1.getLongitud();
        double lat2 = ubicacion2.getLatitud();
        double lon2 = ubicacion2.getLongitud();
        
        double deltaLat = Math.abs(lat1 - lat2);
        double deltaLon = Math.abs(lon1 - lon2);
        
        // Aproximación simple para Buenos Aires
        return Math.sqrt(deltaLat * deltaLat + deltaLon * deltaLon) * 111.0;
    }

    // Clase auxiliar para datos del demo
    private static class DatosDemo {
        final Partido partido;
        final List<Jugador> jugadores;
        
        DatosDemo(Partido partido, List<Jugador> jugadores) {
            this.partido = partido;
            this.jugadores = jugadores;
        }
    }
} 