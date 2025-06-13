package com.tpo.unoMas.model;

import com.tpo.unoMas.dto.PartidoDTO;
import com.tpo.unoMas.model.estado.*;
import com.tpo.unoMas.model.strategy.emparejamiento.EmparejamientoPorCercania;
import com.tpo.unoMas.model.strategy.emparejamiento.EstrategiaEmparejamiento;
import com.tpo.unoMas.model.observer.Observable;
import com.tpo.unoMas.model.observer.Observer;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "partidos")
public class Partido implements Observable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotNull(message = "La fecha y hora no pueden ser nulas")
    @Future(message = "La fecha y hora deben ser futuras")
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @NotNull(message = "La zona no puede ser nula")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @NotNull(message = "El deporte no puede ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deporte_id", nullable = false)
    private Deporte deporte;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_id")
    private Nivel nivel;

    @ManyToMany
    @JoinTable(
            name = "partido_jugador",
            joinColumns = @JoinColumn(name = "partido_id"),
            inverseJoinColumns = @JoinColumn(name = "jugador_id")
    )
    private ArrayList<Jugador> jugadores = new ArrayList<>();

    @Transient
    private EstadoPartido estado;
    //Que el estado sea Transient quiere decir que no se guarda en la DB, que no se persiste
    //Entiendo entonces que cuando traemos un partido del repositorio, esto seria null

    //Por lo de arriba, Y SOLO POR lO DE ARRIBA, guardamos en la DB el estado
    @Column(name = "estado")
    private String estadoDB;

    @Transient
    private EstrategiaEmparejamiento estrategiaEmparejamiento;

    @NotNull(message = "El organizador no puede ser null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizador_id", nullable = false)
    private Jugador organizador;

    @NotNull(message = "La duración no puede ser nula")
    @Min(value = 1, message = "La duración debe ser al menos 1 minuto")
    @Column(nullable = false)
    private Integer duracionMinutos;

    @ManyToMany
    @JoinTable(
            name = "partido_jugador_confirmado",
            joinColumns = @JoinColumn(name = "partido_id"),
            inverseJoinColumns = @JoinColumn(name = "jugador_id")
    )
    private Set<Jugador> jugadoresConfirmados = new HashSet<>();

    @Transient
    private List<Observer> observers;


    // Constructores
    public Partido() {
        this.jugadores = new ArrayList<>();
        this.jugadoresConfirmados = new HashSet<>();
        this.estrategiaEmparejamiento = new EmparejamientoPorCercania();
        this.estado = new NecesitamosJugadores();
        this.observers = new ArrayList<>();
    }

    public Partido(String nombre, String descripcion, LocalDateTime fechaHora,Zona zona) {
        this.titulo=nombre;
        this.fechaHora = fechaHora;
        this.zona = zona;
        this.jugadores = new ArrayList<>();
        this.jugadoresConfirmados = new HashSet<>();
        this.estado = new NecesitamosJugadores();
        this.estrategiaEmparejamiento = new EmparejamientoPorCercania();
        this.observers = new ArrayList<>();
    }

    //Podria ser un Factory
    @PostLoad
    public void inicializarEstado() {
        switch (estadoDB) {
            case "Cancelado" -> this.estado = new Cancelado();
            case "Confirmado"   -> this.estado = new Confirmado();
            case "EnJuego" -> this.estado = new EnJuego();
            case "Finalizado"  -> this.estado = new Finalizado();
            case "NecesitamosJugadores"  -> this.estado = new NecesitamosJugadores();
            case "PartidoArmado"  -> this.estado = new PartidoArmado();
            default -> throw new IllegalStateException("Estado inválido: " + estadoDB);
        }
    }


    public List<Jugador> matchearJugadores(List<Jugador> jugadores) {
        return this.estrategiaEmparejamiento.encontrarJugadoresPotenciales(this, jugadores);
    }


    // Emparejamiento - Partido
    public void cambiarEstrategiaEmparejamiento(EstrategiaEmparejamiento estrategiaEmparejamiento) {
        this.estrategiaEmparejamiento = estrategiaEmparejamiento;
    }

//-------------------------  State Partido --------------------------------------------------------

    // Service Partido
    // Hay un agregar jugador y un confirmar jugador porque la invitacion se envia a x cant de jugadores.
    // Todos los que cumplan con la estrategia de emparejamiento se invitan.
    // Luego se puede confirmar la asistencia de los que aceptaron la invitacion.
    public void agregarJugador(Jugador jugador) {
        Objects.requireNonNull(jugador, "El jugador no puede ser null");
        if (this.estado != null) {
            this.estado.agregarJugador(this, jugador);
        } else {
            throw new IllegalStateException("No se puede agregar jugador sin un estado definido");
        }
    }

    public void removerJugador(Jugador jugador) {
        Objects.requireNonNull(jugador, "El jugador no puede ser null");
        if (this.estado != null) {
            this.estado.removerJugador(this, jugador);
        } else {
            throw new IllegalStateException("No se puede remover jugador sin un estado definido");
        }
    }

    public void confirmarAsistencia(Jugador jugador) {
        if (!jugadores.contains(jugador))
            throw new IllegalArgumentException("El jugador no está en la lista");

        jugadoresConfirmados.add(jugador);

        estado.confirmarPartido(this);

    }

    public void iniciar() {
        if (this.estado != null) {
            this.estado.iniciarPartido(this);
        }
    }

    public void finalizar() {
        if (this.estado != null) {
            this.estado.finalizarPartido(this);
        }
    }

    public void cancelar() {
        if (this.estado != null) {
            this.estado.cancelarPartido(this);
        }
    }

    public void cambiarEstado(EstadoPartido nuevoEstado) {
        Objects.requireNonNull(nuevoEstado, "El nuevo estado no puede ser null");
        this.estado = nuevoEstado;

        //Cada vez que se hace un cambio de estado se notifica a los observadores
        notifyObservers();

    }

    public boolean agregarJugadorInterno(Jugador jugador) {
        boolean agregado = jugadores.add(jugador);
        return agregado;
    }

    public boolean removerJugadorInterno(Jugador jugador) {
        boolean removido = jugadores.remove(jugador);
        return removido;
    }

//-------------------------  Observer - Partido --------------------------------------------------------

    @Override
    public void attach(Observer observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        if (observers != null) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        if (observers != null) {
            for (Observer observer : observers) {
                observer.update(this);
            }
        }
    }

//-------------------------  Metodos Utiles - Partido --------------------------------------------------------

    public boolean estaCompleto() {
        return jugadores.size() == deporte.getCantidadJugadores();
    }

    public boolean estaEnElFuturo() {
        return fechaHora.isAfter(LocalDateTime.now());
    }
//-------------------------  Getter and Setter - Partido --------------------------------------------------------

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public Jugador getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Jugador organizador) {
        this.organizador = organizador;

        if (organizador != null) {
            organizador.agregarAlHistorial(this);
        }
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Set<Jugador> getJugadoresConfirmados() {
        return jugadoresConfirmados;
    }

    public void setJugadoresConfirmados(Set<Jugador> jugadoresConfirmados) {
        this.jugadoresConfirmados = jugadoresConfirmados;
    }

    public EstrategiaEmparejamiento getEstrategiaEmparejamiento() {
        return this.estrategiaEmparejamiento;
    }

    public String getEstadoDB() {
        return estadoDB;
    }

    public void setEstadoDB(String estadoDB) {
        this.estadoDB = estadoDB;
    }

    @Override
    public String toString() {
        return "Partido{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", fechaHora=" + fechaHora +
                ", zona=" + zona +
                ", deporte=" + deporte +
                ", nivel=" + nivel +
                ", jugadores=" + jugadores +
                ", estado=" + estado +
                '}';
    }

    public PartidoDTO convertirADTO() {
        PartidoDTO dto = new PartidoDTO();
        dto.setId(this.id);
        dto.setTitulo(this.titulo);
        dto.setFechaHora(this.fechaHora);
        dto.setZonaDTO(this.zona != null ? this.zona.convertirADTO() : null);
        dto.setDeporteDTO(this.deporte != null ? this.deporte.convertirADTO() : null);
        dto.setNivel(this.nivel);
        dto.setOrganizadorDTO(this.organizador != null ? this.organizador.convertirADTO() : null);
        dto.setDuracionMinutos(this.duracionMinutos);
        dto.setEstado(this.estado != null ? this.estado.getClass().getSimpleName() : null);
        dto.setJugadoresNecesarios(this.deporte != null ? this.deporte.getCantidadJugadores() - this.jugadores.size() : 0);
        dto.setJugadores(this.jugadores.stream().map(Jugador::convertirADTO).toList());
        return dto;
    }

}