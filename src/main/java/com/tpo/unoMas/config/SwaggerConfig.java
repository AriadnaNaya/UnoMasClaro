package com.tpo.unoMas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de Swagger/OpenAPI para documentación automática de la API
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🎾 UnoMas - API de Gestión de Partidos Deportivos")
                        .description("""
                                **UnoMas** es una API REST para la gestión de partidos deportivos, con flujos automáticos y patrones de diseño avanzados.

                                ## 🎯 Funcionalidades
                                - Registro y gestión de usuarios
                                - Creación, búsqueda y gestión de partidos
                                - Estados automáticos de partidos (State Pattern)
                                - Invitaciones automáticas (Observer + Strategy)
                                - Notificaciones por email y push (Adapter)
                                - Endpoints públicos (autenticación desactivada por defecto)

                                ## 🏗️ Patrones de Diseño
                                | Patrón      | Propósito |
                                |------------|-----------|
                                | State      | Estados automáticos de partidos |
                                | Observer   | Notificaciones automáticas |
                                | Strategy   | Algoritmos de emparejamiento |
                                | Adapter    | Integración de canales de notificación |

                                ## 🚀 Flujo Automático
                                1. Crear partido → Invitaciones automáticas a jugadores compatibles
                                2. Jugadores se unen y confirman asistencia
                                3. Estados cambian automáticamente según reglas y tiempo
                                4. Notificaciones enviadas por email/push

                                ## 🌐 Endpoints principales
                                - POST `/api/partidos` (crear partido)
                                - POST `/api/partidos/buscar` (buscar partidos)
                                - POST `/api/partidos/{id}/unirse` (unirse a partido)
                                - POST `/api/partidos/{id}/confirmar` (confirmar asistencia)
                                - POST `/api/partidos/{id}/cancelar` (cancelar partido)
                                - GET `/api/jugadores` (listar jugadores)
                                - POST `/api/jugadores/registro` (registrar usuario)
                                - GET `/api/configuracion/zonas` (zonas disponibles)
                                - GET `/api/configuracion/deportes` (deportes disponibles)
                                - GET `/api/configuracion/niveles` (niveles disponibles)

                                ## 📝 Ejemplo de uso
                                ```json
                                {
                                  "titulo": "Fútbol 5 del Viernes",
                                  "fechaHora": "2024-06-15T19:00:00",
                                  "zonaId": 1,
                                  "deporteId": 1,
                                  "nivel": "INTERMEDIO",
                                  "organizadorId": 1,
                                  "duracionMinutos": 90
                                }
                                ```

                                ## 🛡️ Seguridad
                                - Autenticación desactivada para testing/demo.
                                - Para activar JWT, modificar SecurityConfig.java.
                                """)
                        .version("1.1.0")
                        .contact(new Contact()
                                .name("Equipo UnoMas")
                                .email("soporte@unomas.com")
                                .url("https://github.com/tu-usuario/unoMas"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("🔧 Servidor de Desarrollo"),
                        new Server()
                                .url("https://api.unomas.com")
                                .description("🌐 Servidor de Producción")
                ));
    }
} 