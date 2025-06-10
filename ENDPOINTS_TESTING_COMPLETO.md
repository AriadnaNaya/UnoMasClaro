# 🧪 Testing Completo de Endpoints - UnoMas API

## 📋 Información General

**Base URL**: `http://localhost:8080/api`  
**Swagger UI**: `http://localhost:8080/swagger-ui.html`  
**OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🚀 Guía de Testing Paso a Paso

### 📝 Preparación del Entorno

1. **Iniciar la aplicación:**
```bash
./mvnw spring-boot:run
```

2. **Verificar que está funcionando:**
```bash
curl http://localhost:8080/actuator/health
```

### 🎬 Escenario Completo de Testing

#### **Paso 1: 🏗️ Configuración Inicial**

```bash
# Crear datos maestros del sistema
curl -X POST http://localhost:8080/api/configuracion/datos-prueba \
  -H "Content-Type: application/json"
```

**Respuesta Esperada:**
```json
{
  "mensaje": "Datos de prueba creados exitosamente",
  "zonas": 3,
  "deportes": 3
}
```

#### **Paso 2: 📊 Verificar Datos Maestros**

```bash
# Listar zonas disponibles
curl http://localhost:8080/api/configuracion/zonas

# Listar deportes disponibles  
curl http://localhost:8080/api/configuracion/deportes

# Listar niveles de habilidad
curl http://localhost:8080/api/configuracion/niveles
```

#### **Paso 3: 👤 Registrar Usuarios**

**Registrar Organizador (Juan):**
```bash
curl -X POST http://localhost:8080/api/jugadores/registro \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "email": "juan@email.com",
    "telefono": "+54 11 1234-5678",
    "zonaId": 1,
    "deporteFavoritoId": 1,
    "nivel": "INTERMEDIO"
  }'
```

**Registrar Participantes:**
```bash
# María
curl -X POST http://localhost:8080/api/jugadores/registro \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "María García",
    "email": "maria@email.com",
    "telefono": "+54 11 2345-6789",
    "zonaId": 1,
    "deporteFavoritoId": 1,
    "nivel": "INTERMEDIO"
  }'

# Carlos
curl -X POST http://localhost:8080/api/jugadores/registro \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos López",
    "email": "carlos@email.com",
    "telefono": "+54 11 3456-7890",
    "zonaId": 1,
    "deporteFavoritoId": 1,
    "nivel": "AVANZADO"
  }'

# Ana (5 jugadores más para completar el ejemplo...)
```

#### **Paso 4: ⚽ Crear Partido**

```bash
curl -X POST http://localhost:8080/api/partidos \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Fútbol 5 del Viernes",
    "fechaHora": "2024-06-15T19:00:00",
    "zonaId": 1,
    "deporteId": 1,
    "nivel": "INTERMEDIO",
    "organizadorId": 1,
    "minJugadores": 6,
    "maxJugadores": 10,
    "duracionMinutos": 90
  }'
```

**🎯 Eventos Automáticos que se Disparan:**
- ✅ **State Pattern**: Estado inicial `NecesitamosJugadores`
- ✅ **Observer Pattern**: `InvitacionService` notificado automáticamente
- ✅ **Strategy Pattern**: Estrategia de emparejamiento ejecutada
- ✅ **Adapter Pattern**: Notificaciones enviadas por múltiples canales

#### **Paso 5: 🔍 Buscar Partidos**

```bash
curl -X POST http://localhost:8080/api/partidos/buscar \
  -H "Content-Type: application/json" \
  -d '{
    "zonaId": 1,
    "deporteId": 1,
    "nivel": "INTERMEDIO",
    "soloConEspaciosDisponibles": true,
    "fechaDesde": "2024-06-15T00:00:00",
    "fechaHasta": "2024-06-16T23:59:59"
  }'
```

#### **Paso 6: 🤝 Jugadores se Unen al Partido**

```bash
# María se une (Jugador ID: 2)
curl -X POST "http://localhost:8080/api/partidos/1/unirse?jugadorId=2"

# Carlos se une (Jugador ID: 3)
curl -X POST "http://localhost:8080/api/partidos/1/unirse?jugadorId=3"

# ... hasta alcanzar el mínimo de 6 jugadores
```

**🎯 Transición de Estado Automática:**
- Al llegar a 6 jugadores: `NecesitamosJugadores` → `PartidoArmado`

#### **Paso 7: ✅ Confirmación de Asistencias**

```bash
# Cada jugador confirma su participación
curl -X POST "http://localhost:8080/api/partidos/1/confirmar?jugadorId=1"
curl -X POST "http://localhost:8080/api/partidos/1/confirmar?jugadorId=2"
curl -X POST "http://localhost:8080/api/partidos/1/confirmar?jugadorId=3"
# ... todos los jugadores
```

**🎯 Transición de Estado Automática:**
- Al confirmar todos: `PartidoArmado` → `Confirmado`

#### **Paso 8: 📊 Verificar Estado del Partido**

```bash
curl http://localhost:8080/api/partidos/1
```

**Respuesta Esperada:**
```json
{
  "partido": {
    "id": 1,
    "titulo": "Fútbol 5 del Viernes",
    "estado": "Confirmado",
    "jugadores": [ /* 6 jugadores */ ],
    "jugadoresConfirmados": [ /* 6 confirmados */ ]
  },
  "estado": "Confirmado",
  "jugadores": 6,
  "necesita": 4
}
```

## 🧪 Testing Avanzado

### 🎯 Testing de Patrones de Diseño

#### **1. State Pattern - Transiciones de Estado**

```bash
# Obtener estado actual
curl http://localhost:8080/api/partidos/1 | jq '.estado'

# Forzar cancelación (único cambio manual permitido)
curl -X POST "http://localhost:8080/api/partidos/1/cancelar?organizadorId=1"

# Verificar cambio a estado "Cancelado"
curl http://localhost:8080/api/partidos/1 | jq '.estado'
```

#### **2. Observer Pattern - Notificaciones**

Las notificaciones se disparan automáticamente. Para verificar:

```bash
# Crear nuevo partido y observar logs de notificaciones
curl -X POST http://localhost:8080/api/partidos \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Partido de Prueba Observer",
    "fechaHora": "2024-06-16T20:00:00",
    "zonaId": 1,
    "deporteId": 1,
    "nivel": "PRINCIPIANTE",
    "organizadorId": 1,
    "minJugadores": 4,
    "maxJugadores": 8,
    "duracionMinutos": 60
  }'
```

#### **3. Strategy Pattern - Emparejamiento**

Las estrategias se ejecutan automáticamente al crear partidos:

- **🌍 EmparejamientoPorCercania**: Jugadores en la misma zona
- **⭐ EmparejamientoPorNivel**: Jugadores con nivel compatible
- **📊 EmparejamientoPorHistorial**: Jugadores con historial en la zona

#### **4. Adapter Pattern - Múltiples Canales**

El sistema soporta múltiples canales de notificación:

- 📧 **Email** (vía SMTP)
- 📱 **Push Notifications** (vía Firebase)
- 🌐 **Webhook** (para integraciones)

### 🔍 Testing de Búsquedas Avanzadas

```bash
# Búsqueda por múltiples criterios
curl -X POST http://localhost:8080/api/partidos/buscar \
  -H "Content-Type: application/json" \
  -d '{
    "zonaId": 1,
    "deporteId": 1,
    "nivel": "INTERMEDIO",
    "soloConEspaciosDisponibles": true,
    "estado": "NecesitamosJugadores"
  }'

# Búsqueda por rango de fechas
curl -X POST http://localhost:8080/api/partidos/buscar \
  -H "Content-Type: application/json" \
  -d '{
    "fechaDesde": "2024-06-15T00:00:00",
    "fechaHasta": "2024-06-20T23:59:59"
  }'
```

### 📊 Testing de Estadísticas

```bash
# Estadísticas de jugador
curl http://localhost:8080/api/jugadores/1/estadisticas

# Partidos de un jugador específico
curl http://localhost:8080/api/partidos/jugador/1

# Jugadores por zona
curl http://localhost:8080/api/jugadores/zona/1
```

## 🛠️ Testing con Postman

### 📁 Colección de Postman

**Importar la siguiente colección JSON:**

```json
{
  "info": {
    "name": "UnoMas API - Testing Completo",
    "description": "Colección completa para testing de la API UnoMas",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "1. Configuración",
      "item": [
        {
          "name": "Crear Datos de Prueba",
          "request": {
            "method": "POST",
            "header": [],
            "url": {
              "raw": "{{base_url}}/api/configuracion/datos-prueba",
              "host": ["{{base_url}}"],
              "path": ["api", "configuracion", "datos-prueba"]
            }
          }
        },
        {
          "name": "Listar Zonas",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{base_url}}/api/configuracion/zonas",
              "host": ["{{base_url}}"],
              "path": ["api", "configuracion", "zonas"]
            }
          }
        }
      ]
    },
    {
      "name": "2. Jugadores",
      "item": [
        {
          "name": "Registrar Jugador",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"nombre\": \"Juan Pérez\",\n  \"email\": \"juan@email.com\",\n  \"telefono\": \"+54 11 1234-5678\",\n  \"zonaId\": 1,\n  \"deporteFavoritoId\": 1,\n  \"nivel\": \"INTERMEDIO\"\n}"
            },
            "url": {
              "raw": "{{base_url}}/api/jugadores/registro",
              "host": ["{{base_url}}"],
              "path": ["api", "jugadores", "registro"]
            }
          }
        }
      ]
    }
  ],
  "variable": [
    {
      "key": "base_url",
      "value": "http://localhost:8080",
      "type": "string"
    }
  ]
}
```

### 🌐 Variables de Entorno

```json
{
  "name": "UnoMas Development",
  "values": [
    {
      "key": "base_url",
      "value": "http://localhost:8080",
      "enabled": true
    },
    {
      "key": "jugador_id",
      "value": "1",
      "enabled": true
    },
    {
      "key": "partido_id",
      "value": "1",
      "enabled": true
    }
  ]
}
```

## 🎭 Testing de Demos Interactivos

### 🚀 Ejecutar Demos desde Terminal

```bash
# Compilar proyecto
./mvnw compile

# Demo completo de patrones integrados
java -cp target/classes com.tpo.unoMas.PatronesIntegradosDemo

# Demo específico de estados
java -cp target/classes com.tpo.unoMas.PartidoStateDemo

# Demo de estrategias de emparejamiento
java -cp target/classes com.tpo.unoMas.EstrategiasEmparejamientoDemo
```

### 📊 Testing Unitario

```bash
# Ejecutar todos los tests
./mvnw test

# Tests específicos por patrón
./mvnw test -Dtest=PartidoStateTest
./mvnw test -Dtest=PatronesIntegradosTest
./mvnw test -Dtest=EstrategiasEmparejamientoTest

# Test con reporte de cobertura
./mvnw test jacoco:report
```

## 🔧 Troubleshooting

### ❌ Errores Comunes

#### **1. Error 400 - Bad Request**
```bash
# Verificar formato de JSON
curl -X POST http://localhost:8080/api/jugadores/registro \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Test"}' # JSON inválido - faltan campos requeridos
```

#### **2. Error 404 - Not Found**
```bash
# Verificar que existan datos maestros
curl http://localhost:8080/api/configuracion/zonas
curl http://localhost:8080/api/configuracion/deportes
```

#### **3. Error 500 - Internal Server Error**
```bash
# Verificar logs de la aplicación
tail -f logs/application.log

# Verificar conexión a base de datos
curl http://localhost:8080/actuator/health
```

### 🔍 Debugging

```bash
# Verificar estado de la aplicación
curl http://localhost:8080/actuator/info

# Ver métricas
curl http://localhost:8080/actuator/metrics

# Health check detallado
curl http://localhost:8080/actuator/health
```

## 📊 Métricas y Monitoreo

### 📈 Endpoints de Actuator

```bash
# Health check
curl http://localhost:8080/actuator/health

# Información de la aplicación
curl http://localhost:8080/actuator/info

# Métricas disponibles
curl http://localhost:8080/actuator/metrics

# Métricas de HTTP requests
curl http://localhost:8080/actuator/metrics/http.server.requests
```

## 🎯 Casos de Uso Específicos

### 🔄 Testing de Flujo Completo Automatizado

**Script de testing automatizado:**

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api"

echo "🚀 Iniciando testing automatizado de UnoMas API..."

# 1. Crear datos de prueba
echo "📋 Creando datos de prueba..."
curl -s -X POST "$BASE_URL/configuracion/datos-prueba" | jq '.'

# 2. Registrar jugadores
echo "👤 Registrando jugadores..."
for i in {1..6}; do
  curl -s -X POST "$BASE_URL/jugadores/registro" \
    -H "Content-Type: application/json" \
    -d "{
      \"nombre\": \"Jugador $i\",
      \"email\": \"jugador$i@email.com\",
      \"telefono\": \"+54 11 123$i-567$i\",
      \"zonaId\": 1,
      \"deporteFavoritoId\": 1,
      \"nivel\": \"INTERMEDIO\"
    }" | jq '.mensaje'
done

# 3. Crear partido
echo "⚽ Creando partido..."
PARTIDO_ID=$(curl -s -X POST "$BASE_URL/partidos" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Partido Automatizado",
    "fechaHora": "2024-06-15T19:00:00",
    "zonaId": 1,
    "deporteId": 1,
    "nivel": "INTERMEDIO",
    "organizadorId": 1,
    "minJugadores": 4,
    "maxJugadores": 8,
    "duracionMinutos": 90
  }' | jq -r '.partido.id')

echo "🎯 Partido creado con ID: $PARTIDO_ID"

# 4. Unir jugadores
echo "🤝 Uniendo jugadores al partido..."
for i in {2..4}; do
  curl -s -X POST "$BASE_URL/partidos/$PARTIDO_ID/unirse?jugadorId=$i" | jq '.mensaje'
done

# 5. Verificar estado
echo "📊 Estado final del partido:"
curl -s "$BASE_URL/partidos/$PARTIDO_ID" | jq '.estado'

echo "✅ Testing automatizado completado!"
```

---

**🎉 ¡Listo para testear!** La API UnoMas está completamente documentada y lista para ser probada con todos sus patrones de diseño integrados. 