# 🚀 Endpoints Disponibles para Testing (Sin Autenticación)

## **✅ Sistema Listo para Usar**

**Autenticación JWT DESACTIVADA** - Todos los endpoints son accesibles libremente.

---

## **📋 Endpoints Principales**

### **🏟️ Gestión de Partidos** (`/api/partidos`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/partidos` | Crear nuevo partido |
| `POST` | `/api/partidos/buscar` | Buscar partidos por criterios |
| `GET` | `/api/partidos/{id}` | Obtener partido por ID |
| `POST` | `/api/partidos/{id}/unirse` | Unirse a un partido |
| `POST` | `/api/partidos/{id}/salirse` | Salirse de un partido |
| `POST` | `/api/partidos/{id}/cancelar` | Cancelar partido (organizador) |
| `POST` | `/api/partidos/{id}/confirmar` | Confirmar participación |
| `GET` | `/api/partidos/jugador/{jugadorId}` | Partidos de un jugador |

### **👥 Gestión de Jugadores** (`/api/jugadores`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/jugadores/registro` | Registrar nuevo jugador |
| `GET` | `/api/jugadores/{id}` | Obtener jugador por ID |
| `GET` | `/api/jugadores` | Obtener todos los jugadores |
| `GET` | `/api/jugadores/zona/{zonaId}` | Jugadores por zona |
| `GET` | `/api/jugadores/email/{email}` | Buscar por email |
| `PUT` | `/api/jugadores/{id}` | Actualizar perfil |
| `GET` | `/api/jugadores/{id}/estadisticas` | Estadísticas del jugador |

### **⚙️ Configuración** (`/api/configuracion`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/configuracion/zonas` | Obtener zonas disponibles |
| `GET` | `/api/configuracion/deportes` | Obtener deportes disponibles |
| `GET` | `/api/configuracion/niveles` | Obtener niveles disponibles |
| `POST` | `/api/configuracion/datos-prueba` | Crear datos de prueba |

---

## **🧪 Ejemplos de Testing**

### **1. Crear datos de prueba:**
```bash
POST http://localhost:8080/api/configuracion/datos-prueba
```

### **2. Registrar jugador:**
```bash
POST http://localhost:8080/api/jugadores/registro
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@email.com",
  "zonaId": 1,
  "deporteFavoritoId": 1,
  "nivel": "INTERMEDIO"
}
```

### **3. Crear partido:**
```bash
POST http://localhost:8080/api/partidos
Content-Type: application/json

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

### **4. Buscar partidos:**
```bash
POST http://localhost:8080/api/partidos/buscar
Content-Type: application/json

{
  "zonaId": 1,
  "deporteId": 1,
  "soloConEspaciosDisponibles": true
}
```

---

## **🎯 Patrones de Diseño en Acción**

Al usar estos endpoints, verás los patrones funcionando:

- **State Pattern**: Estados de partidos cambian automáticamente
- **Observer Pattern**: Invitaciones automáticas al crear partidos
- **Strategy Pattern**: Diferentes algoritmos de emparejamiento
- **Adapter Pattern**: Múltiples canales de notificación

---

## **🔧 Para Reactivar Autenticación (Futuro)**

Si quieres activar JWT más adelante:
1. Descomenta los métodos en `SecurityConfig.java`
2. Restaura las importaciones comentadas
3. Cambia `.anyRequest().permitAll()` por configuración restrictiva

¡Listo para hacer testing completo del sistema! 🚀 