package com.tpo.unoMas.demo;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.estado.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * DEMO INTERACTIVO DE STATE PATTERN
 * 
 * Permite al usuario explorar interactivamente los diferentes estados de un partido:
 * - NecesitamosJugadores
 * - PartidoArmado  
 * - Confirmado
 * - EnJuego
 * - Finalizado
 * - Cancelado
 * 
 * Para ejecutar: java -jar target/unoMas-0.0.1-SNAPSHOT.jar --demo-estados
 */
@Component
public class DemoEstados implements CommandLineRunner {

    private Scanner scanner;
    private Partido partido;
    private Zona zona;
    private Deporte deporte;
    private Jugador organizador;

    @Override
    public void run(String... args) throws Exception {
        if (Arrays.asList(args).contains("--demo-estados")) {
            System.out.println("=".repeat(80));
            System.out.println("🎭 DEMO INTERACTIVO - STATE PATTERN");
            System.out.println("=".repeat(80));
            
            ejecutarDemoEstados();
        }
    }

    private void ejecutarDemoEstados() {
        scanner = new Scanner(System.in);
        
        System.out.println("\n🎯 Bienvenido al Demo Interactivo de Estados de Partido");
        System.out.println("Podrás explorar todos los estados y transiciones del sistema.\n");
        
        try {
            inicializarDatos();
            mostrarMenuPrincipal();
        } catch (Exception e) {
            System.err.println("❌ Error en demo: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private void inicializarDatos() {
        // Crear zona
        zona = new Zona();
        zona.setBarrio("Palermo");
        zona.setPartido("CABA");
        zona.setUbicacion(new Ubicacion(-34.5875, -58.4217));
        
        // Crear deporte
        deporte = new Deporte();
        deporte.setNombre("Fútbol 11");
        deporte.setDescripcion("Deporte de equipo");
        deporte.setCantidadJugadores(22);
        
        // Crear organizador
        organizador = new Jugador("Organizador Demo", "organizador@email.com", "password123", zona);
        
        // Crear partido inicial
        crearNuevoPartido();
        
        System.out.println("✅ Datos inicializados correctamente");
        System.out.println("📅 Partido creado: " + partido.getTitulo());
    }

    private void crearNuevoPartido() {
        partido = new Partido();
        partido.setTitulo("Partido Demo Estados");
        partido.setFechaHora(LocalDateTime.now().plusDays(1));
        partido.setZona(zona);
        partido.setDeporte(deporte);
        partido.setOrganizador(organizador);
        partido.setDuracionMinutos(90);
        partido.setNivel(Nivel.INTERMEDIO);
    }

    private void mostrarMenuPrincipal() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarEstadoActual();
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

    private void mostrarEstadoActual() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 ESTADO ACTUAL DEL PARTIDO");
        System.out.println("=".repeat(60));
        System.out.println("📋 Título: " + partido.getTitulo());
        System.out.println("🏆 Estado: " + partido.getEstado().getClass().getSimpleName());
        System.out.println("📅 Fecha: " + partido.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        System.out.println("📍 Zona: " + partido.getZona().getBarrio());
        System.out.println("⚽ Deporte: " + partido.getDeporte().getNombre());
        System.out.println("👥 Jugadores: " + partido.getJugadores().size() + "/" + partido.getDeporte().getCantidadJugadores());
        System.out.println("✅ Confirmados: " + partido.getJugadoresConfirmados().size());
        System.out.println("💬 Mensaje del estado: " + partido.getEstado().armarMensaje());
    }

    private void mostrarOpcionesMenu() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("🔧 OPCIONES DISPONIBLES:");
        System.out.println("-".repeat(40));
        
        System.out.println("1. ➕ Agregar jugador");
        System.out.println("2. ➖ Remover jugador"); 
        System.out.println("3. ✅ Confirmar asistencia");
        System.out.println("4. 🎮 Iniciar partido");
        System.out.println("5. 🏁 Finalizar partido");
        System.out.println("6. ❌ Cancelar partido");
        System.out.println("7. 🔄 Cambiar estado manualmente");
        System.out.println("8. 👥 Ver jugadores");
        System.out.println("9. 🔄 Reiniciar partido");
        System.out.println("0. 🚪 Salir");
        
        System.out.print("\nSeleccione una opción: ");
    }

    private boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                agregarJugador();
                break;
            case 2:
                removerJugador();
                break;
            case 3:
                confirmarAsistencia();
                break;
            case 4:
                iniciarPartido();
                break;
            case 5:
                finalizarPartido();
                break;
            case 6:
                cancelarPartido();
                break;
            case 7:
                cambiarEstadoManualmente();
                break;
            case 8:
                verJugadores();
                break;
            case 9:
                reiniciarPartido();
                break;
            case 0:
                System.out.println("👋 ¡Gracias por usar el demo de estados!");
                return false;
            default:
                System.out.println("❌ Opción no válida");
        }
        return true;
    }

    private void agregarJugador() {
        System.out.print("Ingrese el nombre del jugador: ");
        String nombre = scanner.nextLine().trim();
        
        if (nombre.isEmpty()) {
            System.out.println("❌ El nombre no puede estar vacío");
            return;
        }
        
        try {
            Jugador nuevoJugador = new Jugador(nombre, nombre.toLowerCase().replace(" ", "") + "@email.com", "password123", zona);
            partido.agregarJugador(nuevoJugador);
            System.out.println("✅ Jugador " + nombre + " agregado exitosamente");
        } catch (Exception e) {
            System.out.println("❌ Error al agregar jugador: " + e.getMessage());
        }
    }

    private void removerJugador() {
        if (partido.getJugadores().isEmpty()) {
            System.out.println("❌ No hay jugadores para remover");
            return;
        }
        
        System.out.println("Jugadores disponibles:");
        for (int i = 0; i < partido.getJugadores().size(); i++) {
            System.out.println((i + 1) + ". " + partido.getJugadores().get(i).getNombre());
        }
        
        System.out.print("Seleccione el número del jugador a remover: ");
        try {
            int indice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (indice >= 0 && indice < partido.getJugadores().size()) {
                Jugador jugadorARemover = partido.getJugadores().get(indice);
                partido.removerJugador(jugadorARemover);
                System.out.println("✅ Jugador " + jugadorARemover.getNombre() + " removido exitosamente");
            } else {
                System.out.println("❌ Número inválido");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor ingrese un número válido");
        } catch (Exception e) {
            System.out.println("❌ Error al remover jugador: " + e.getMessage());
        }
    }

    private void confirmarAsistencia() {
        if (partido.getJugadores().isEmpty()) {
            System.out.println("❌ No hay jugadores en el partido");
            return;
        }
        
        System.out.println("Jugadores disponibles para confirmar:");
        for (int i = 0; i < partido.getJugadores().size(); i++) {
            Jugador jugador = partido.getJugadores().get(i);
            String confirmado = partido.getJugadoresConfirmados().contains(jugador) ? " ✅" : " ⏳";
            System.out.println((i + 1) + ". " + jugador.getNombre() + confirmado);
        }
        
        System.out.print("Seleccione el número del jugador: ");
        try {
            int indice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (indice >= 0 && indice < partido.getJugadores().size()) {
                Jugador jugador = partido.getJugadores().get(indice);
                partido.confirmarAsistencia(jugador);
                System.out.println("✅ Asistencia confirmada para " + jugador.getNombre());
            } else {
                System.out.println("❌ Número inválido");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor ingrese un número válido");
        } catch (Exception e) {
            System.out.println("❌ Error al confirmar asistencia: " + e.getMessage());
        }
    }

    private void iniciarPartido() {
        try {
            partido.iniciar();
            System.out.println("🎮 ¡Partido iniciado exitosamente!");
        } catch (Exception e) {
            System.out.println("❌ Error al iniciar partido: " + e.getMessage());
        }
    }

    private void finalizarPartido() {
        try {
            partido.finalizar();
            System.out.println("🏁 ¡Partido finalizado exitosamente!");
        } catch (Exception e) {
            System.out.println("❌ Error al finalizar partido: " + e.getMessage());
        }
    }

    private void cancelarPartido() {
        System.out.print("¿Está seguro que desea cancelar el partido? (s/N): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();
        
        if (confirmacion.equals("s") || confirmacion.equals("si")) {
            try {
                partido.cancelar();
                System.out.println("❌ Partido cancelado");
            } catch (Exception e) {
                System.out.println("❌ Error al cancelar partido: " + e.getMessage());
            }
        } else {
            System.out.println("Cancelación abortada");
        }
    }

    private void cambiarEstadoManualmente() {
        System.out.println("Estados disponibles:");
        System.out.println("1. NecesitamosJugadores");
        System.out.println("2. PartidoArmado");
        System.out.println("3. Confirmado");
        System.out.println("4. EnJuego");
        System.out.println("5. Finalizado");
        System.out.println("6. Cancelado");
        
        System.out.print("Seleccione el nuevo estado: ");
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            EstadoPartido nuevoEstado = null;
            
            switch (opcion) {
                case 1:
                    nuevoEstado = new NecesitamosJugadores();
                    break;
                case 2:
                    nuevoEstado = new PartidoArmado();
                    break;
                case 3:
                    nuevoEstado = new Confirmado();
                    break;
                case 4:
                    nuevoEstado = new EnJuego();
                    break;
                case 5:
                    nuevoEstado = new Finalizado();
                    break;
                case 6:
                    nuevoEstado = new Cancelado();
                    break;
                default:
                    System.out.println("❌ Opción inválida");
                    return;
            }
            
            partido.cambiarEstado(nuevoEstado);
            System.out.println("✅ Estado cambiado a: " + nuevoEstado.getClass().getSimpleName());
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor ingrese un número válido");
        } catch (Exception e) {
            System.out.println("❌ Error al cambiar estado: " + e.getMessage());
        }
    }

    private void verJugadores() {
        System.out.println("\n👥 LISTA DE JUGADORES:");
        System.out.println("-".repeat(40));
        
        if (partido.getJugadores().isEmpty()) {
            System.out.println("No hay jugadores en el partido");
        } else {
            for (int i = 0; i < partido.getJugadores().size(); i++) {
                Jugador jugador = partido.getJugadores().get(i);
                String confirmado = partido.getJugadoresConfirmados().contains(jugador) ? " ✅ Confirmado" : " ⏳ Pendiente";
                String organizador = jugador.equals(partido.getOrganizador()) ? " 👑 Organizador" : "";
                System.out.println((i + 1) + ". " + jugador.getNombre() + confirmado + organizador);
            }
        }
    }

    private void reiniciarPartido() {
        System.out.print("¿Está seguro que desea reiniciar el partido? (s/N): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();
        
        if (confirmacion.equals("s") || confirmacion.equals("si")) {
            crearNuevoPartido();
            System.out.println("🔄 Partido reiniciado exitosamente");
        } else {
            System.out.println("Reinicio abortado");
        }
    }
} 