package com.tpo.unoMas.model;

// Imports comentados - estas clases ya no se usan
// import com.tpo.unoMas.model.strategy.jugador.EstrategiaEmparejamiento;
// import com.tpo.unoMas.model.strategy.jugador.EstrategiaPorNivel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "jugadores")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String password;

    @Column(name = "token_fcm")
    private String tokenFCM;

    @NotNull(message = "La zona no puede ser nula")
    @ManyToOne
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel")
    private Nivel nivel;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeporteJugador> deportes = new ArrayList<>();

    @OneToMany(mappedBy = "organizador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Partido> partidosOrganizados = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "jugador_partidos",
            joinColumns = @JoinColumn(name = "jugador_id"),
            inverseJoinColumns = @JoinColumn(name = "partido_id")
    )
    private Set<Partido> partidosParticipados = new HashSet<>();

    // Campo comentado - ya no se usa este patrón Strategy en Jugador
    // @Transient
    // private EstrategiaEmparejamiento estrategiaEmparejamiento;

    public Jugador() {
        // this.estrategiaEmparejamiento = new EstrategiaPorNivel(); // Estrategia por defecto
    }
    public Jugador(String nombre, String email, String password, Zona zona) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.zona = zona;
    }

    // Strategy Jugador - métodos comentados ya no se usan

    // public void cambiarEstrategiaEmparejamiento(EstrategiaEmparejamiento estrategia) {
    //     this.estrategiaEmparejamiento = Objects.requireNonNull(estrategia, "La estrategia no puede ser null");
    // }

    // State Jugador
    public void agregarPartido(Partido partido) {
        Objects.requireNonNull(partido, "El partido no puede ser null");
        this.partidosParticipados.add(partido);
    }

    public void removerPartido(Partido partido) {
        Objects.requireNonNull(partido, "El partido no puede ser null");
        this.partidosParticipados.remove(partido);
    }

    public void agregarAFavoritos(Deporte deporte){
        Objects.requireNonNull(deporte, "El deporte no puede ser null");
        for (DeporteJugador deporteJugador : deportes) {
            if (deporteJugador.getDeporte().equals(deporte)) {
                deporteJugador.setEsFavorito(true);
            }
        }
    }

    public List<Deporte> deportesFavoritos(){
        List<Deporte> deportesFavs = new ArrayList<>();
        for (DeporteJugador deporteJugador : deportes) {
            if(deporteJugador.esFavorito()){
                deportesFavs.add(deporteJugador.getDeporte());
            }
        }
        return deportesFavs;
    }


    //  Getter y Setter - Jugador
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenFCM() {
        return tokenFCM;
    }

    public void setTokenFCM(String tokenFCM) {
        this.tokenFCM = tokenFCM;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<DeporteJugador> getDeportes() {
        return deportes;
    }

    public void setDeportes(List<DeporteJugador> deportes) {
        this.deportes = deportes;
    }

    public List<Partido> getPartidosOrganizados() {
        return partidosOrganizados;
    }

    public void setPartidosOrganizados(List<Partido> partidosOrganizados) {
        this.partidosOrganizados = partidosOrganizados;
    }

    public Set<Partido> getPartidosParticipados() {
        return partidosParticipados;
    }

    public void setPartidosParticipados(Set<Partido> partidosParticipados) {
        this.partidosParticipados = partidosParticipados;
    }

    // public EstrategiaEmparejamiento getEstrategiaEmparejamiento() {
    //     return estrategiaEmparejamiento;
    // }
}
