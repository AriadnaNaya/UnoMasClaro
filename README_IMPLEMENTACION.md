# Sistema de Gestión de Partidos Deportivos - TPO

## Arquitectura Implementada

### **Patrones de Diseño Integrados**

#### 1. **State Pattern** - Gestión del Ciclo de Vida de Partidos
- **Estados**: `NecesitamosJugadores` → `PartidoArmado` → `Confirmado` → `EnJuego` → `Finalizado`/`Cancelado`
- **Transiciones automáticas** controladas por los propios estados
- **Contexto**: Clase `Partido` delega comportamiento a `EstadoPartido`
- **⚠️ Importante**: Los cambios de estado NO se fuerzan externamente, se manejan automáticamente

#### 2. **Observer Pattern** - Sistema de Notificaciones Automáticas
- **Observable**: `Partido` (implementa `Observable`)
- **Observers**: 
  - `NotificacionService` - Notificaciones generales
  - `InvitacionService` - Invitaciones automáticas al crear partidos
- **Eventos**: Cambios de estado, creación de partidos

#### 3. **Strategy Pattern** - Algoritmos de Emparejamiento y Notificaciones
- **Estrategias de Emparejamiento**:
  - `EmparejamientoPorCercania` - Jugadores en la misma zona
  - `EmparejamientoPorNivel` - Jugadores con nivel compatible (±1)
  - `EmparejamientoPorHistorial` - Basado en historial de partidos
- **Estrategias de Notificación**:
  - `NotificacionEmail` - Notificaciones por email
  - `NotificacionPush` - Notificaciones push

#### 4. **Adapter Pattern** - Integración con Servicios Externos
- **Adapters**: `AdapterJavaMail`, `NotificacionEmailAdapter`
- **Propósito**: Integrar diferentes proveedores de notificaciones

---

## **API REST Endpoints**

### **Partidos** (`/api/partidos`)

| Método | Endpoint | Descripción | Requerimiento TPO |
|--------|----------|-------------|-------------------|
| `POST` | `/` | Crear nuevo partido | RF3: Creación de partido |
| `POST` | `/buscar` | Buscar partidos por criterios | RF2: Búsqueda de partidos |
| `GET` | `/{id}` | Obtener partido por ID | - |
| `POST` | `/{id}/unirse` | Unirse a un partido | - |
| `POST` | `/{id}/salirse` | Salirse de un partido | - |
| `POST` | `/{id}/cancelar` | Cancelar partido (solo organizador) | RF4: Estado de partidos |
| `POST` | `/{id}/confirmar` | Confirmar participación | - |
| `GET` | `/jugador/{jugadorId}` | Partidos de un jugador | - |
| `POST` | `/{id}/invitaciones` | Enviar invitaciones con estrategia | RF5.d: Algoritmos emparejamiento |

### **Jugadores** (`/api/jugadores`)

| Método | Endpoint | Descripción | Requerimiento TPO |
|--------|----------|-------------|-------------------|
| `POST` | `/registro` | Registrar nuevo jugador | RF1: Registro de usuarios |
| `GET` | `/{id}` | Obtener jugador por ID | - |
| `GET` | `/` | Obtener todos los jugadores | - |
| `GET` | `/zona/{zonaId}` | Jugadores por zona | - |
| `GET` | `/email/{email}` | Buscar por email | - |
| `PUT` | `/{id}` | Actualizar perfil | - |
| `GET` | `/{id}/estadisticas` | Estadísticas del jugador | - |

### **Configuración** (`/api/configuracion`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/zonas` | Obtener zonas disponibles |
| `GET` | `/deportes` | Obtener deportes disponibles |
| `GET` | `/niveles` | Obtener niveles disponibles |
| `POST` | `/datos-prueba` | Crear datos de prueba |

---

## **DTOs para API**

### **Requests**
- `CrearPartidoRequest` - Datos para crear partido
- `BuscarPartidosRequest` - Criterios de búsqueda
- `RegistroJugadorRequest` - Datos para registro

### **Responses**
- `PartidoDTO` - Información completa de partido
- `JugadorDTO` - Información completa de jugador
- `JugadorSimpleDTO` - Información básica de jugador

---

## **Servicios Implementados**

### **PartidoService**
- Creación y gestión de partidos
- Búsqueda con filtros múltiples
- Gestión de estados y participantes
- Integración con patrones Observer y Strategy

### **JugadorService** 
- Registro y autenticación de usuarios
- Gestión de perfiles
- Estadísticas de participación

### **InvitacionService**
- **Observer**: Automático al crear partidos
- **Strategy**: Diferentes algoritmos de emparejamiento
- Integración con `NotificacionService`

### **NotificacionService**
- **Observer**: Notificaciones automáticas
- **Strategy**: Múltiples canales de notificación
- **Adapter**: Integración con servicios externos

---

## **Repositorios JPA**

- `PartidoRepository` - Consultas avanzadas para partidos
- `JugadorRepository` - Gestión de usuarios y jugadores
- `ZonaRepository` - Gestión de zonas geográficas
- `DeporteRepository` - Gestión de tipos de deportes

---

## **Cumplimiento de Requerimientos TPO**

| Requerimiento | Implementación | Estado |
|---------------|----------------|--------|
| **RF1**: Registro de usuarios | `JugadorController.registrarJugador()` | ✅ Completo |
| **RF2**: Búsqueda de partidos | `PartidoController.buscarPartidos()` | ✅ Completo |
| **RF3**: Creación de partidos | `PartidoController.crearPartido()` | ✅ Completo |
| **RF4**: Estados de partidos | State Pattern + API endpoints | ✅ Completo |
| **RF5.d**: Algoritmos emparejamiento | Strategy Pattern + API | ✅ Completo |
| **RF6.a.i**: Notificaciones automáticas | Observer Pattern | ✅ Completo |

---

## **Flujo de Uso Típico**

### 1. **Registro de Usuario**
```bash
POST /api/jugadores/registro
{
  "nombre": "Juan Pérez",
  "email": "juan@email.com",
  "zonaId": 1,
  "deporteFavoritoId": 1,
  "nivel": "INTERMEDIO"
}
```

### 2. **Creación de Partido**
```bash
POST /api/partidos
{
  "titulo": "Fútbol en Palermo",
  "fechaHora": "2024-12-20T15:00:00",
  "zonaId": 1,
  "deporteId": 1,
  "nivel": "INTERMEDIO",
  "organizadorId": 1,
  "minJugadores": 10,
  "maxJugadores": 22,
  "duracionMinutos": 90
}
```
**Resultado**: 
- Estado inicial: `NecesitamosJugadores`
- `InvitacionService` envía invitaciones automáticamente
- `NotificacionService` notifica a jugadores interesados

### 3. **Búsqueda de Partidos**
```bash
POST /api/partidos/buscar
{
  "zonaId": 1,
  "deporteId": 1,
  "nivel": "INTERMEDIO",
  "soloConEspaciosDisponibles": true
}
```

### 4. **Ciclo de Vida del Partido (Patrón State Puro)**
- **Creación** → `NecesitamosJugadores`
- **Alcanzar mín. jugadores** → `PartidoArmado` (automático al agregar jugadores)
- **Todos confirman** → `Confirmado` (automático al confirmar asistencias)
- **Llega la hora** → `EnJuego` (automático por `GestorEstadosPartidoService`)
- **Termina duración** → `Finalizado` (automático por tiempo)
- **Cancelar** → `Cancelado` (manual, solo organizador)

---

## **Testing Implementado**

### **Tests Unitarios**
- `PartidoStateTest` - Todas las transiciones de estado
- `PatronesIntegradosTest` - Integración de patrones

### **Demos Funcionales**
- `ObserverStrategyDemo` - Observer + Strategy
- `SistemaInvitacionesDemo` - Sistema completo
- `EstrategiasEmparejamientoDemo` - Diferentes algoritmos

---

## **Tecnologías Utilizadas**

- **Spring Boot 3.x** - Framework principal
- **Spring Data JPA** - Persistencia
- **Jakarta Validation** - Validaciones
- **Maven** - Gestión de dependencias
- **Java 17** - Versión del lenguaje

---

## **Extensibilidad**

### **Nuevos Estados**
Agregar clases que implementen `EstadoPartido` en package `model.estado`

### **Nuevas Estrategias de Emparejamiento**
Implementar `EstrategiaEmparejamiento` en package `model.strategy`

### **Nuevos Canales de Notificación**
Implementar `INotificacionStrategy` o usar Adapter pattern

### **Nuevas Funcionalidades**
El sistema está preparado para:
- Sistema de rating/puntuación
- Geolocalización avanzada
- Chat entre jugadores
- Reserva de canchas
- Pagos integrados 