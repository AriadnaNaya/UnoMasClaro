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
                                **Sistema completo de gestión de partidos deportivos** desarrollado con Spring Boot.
                                
                                ## 🎯 Características Principales
                                
                                - ✅ **Registro de usuarios** y gestión de perfiles
                                - ⚽ **Creación y gestión** de partidos deportivos  
                                - 🔍 **Búsqueda avanzada** con múltiples filtros
                                - 🔄 **Estados automáticos** de partidos (State Pattern)
                                - 📧 **Notificaciones automáticas** (Observer Pattern)
                                - 🎯 **Estrategias de emparejamiento** (Strategy Pattern)
                                - 🔌 **Múltiples canales** de notificación (Adapter Pattern)
                                
                                ## 🏗️ Patrones de Diseño Implementados
                                
                                | Patrón | Propósito |
                                |--------|-----------|
                                | **State** | Gestión automática de estados de partidos |
                                | **Observer** | Sistema de notificaciones en tiempo real |
                                | **Strategy** | Algoritmos intercambiables de emparejamiento |
                                | **Adapter** | Integración unificada de servicios externos |
                                
                                ## 🚀 Flujo de Uso Recomendado
                                
                                1. **Configuración**: `POST /api/configuracion/datos-prueba`
                                2. **Registro**: `POST /api/jugadores/registro`
                                3. **Crear Partido**: `POST /api/partidos`
                                4. **Buscar Partidos**: `POST /api/partidos/buscar`
                                5. **Unirse**: `POST /api/partidos/{id}/unirse`
                                6. **Confirmar**: `POST /api/partidos/{id}/confirmar`
                                
                                ## 📊 Estados de Partidos
                                
                                ```
                                NecesitamosJugadores → PartidoArmado → Confirmado → EnJuego → Finalizado
                                ```
                                
                                **Nota**: Los cambios de estado son automáticos basados en la lógica de negocio.
                                """)
                        .version("1.0.0")
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