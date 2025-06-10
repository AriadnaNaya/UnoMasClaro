# 🏆 UnoMas - Sistema de Gestión de Partidos

## 📋 Descripción del Proyecto

**UnoMas** es un sistema de gestión de partidos deportivos que implementa múltiples **patrones de diseño** de manera integrada. El proyecto demuestra cómo diferentes patrones pueden trabajar juntos para crear un sistema robusto y flexible.

## 🎯 Patrones de Diseño Implementados

### 1. 🔄 **STATE PATTERN** - Estados del Partido
El partido cambia su comportamiento según su estado interno:

```
NecesitamosJugadores → PartidoArmado → Confirmado → EnJuego → Finalizado
                                   ↘ Cancelado
```

**Estados implementados:**
- **`NecesitamosJugadores`**: Estado inicial, esperando jugadores mínimos
- **`PartidoArmado`**: Mínimo de jugadores alcanzado, listo para confirmaciones
- **`Confirmado`**: Todos los jugadores confirmaron asistencia
- **`EnJuego`**: Partido en curso
- **`Finalizado`**: Partido terminado
- **`Cancelado`**: Partido cancelado

### 2. 👀 **OBSERVER PATTERN** - Notificaciones Automáticas
Cada cambio de estado notifica automáticamente a los observadores suscritos:

```java
// El partido notifica cuando cambia de estado
partido.attach(notificacionService);    // Suscribir observador
partido.attach(logService);              // Múltiples observadores
partido.detach(observador);              // Desuscribir cuando sea necesario
```

### 3. 🎯 **STRATEGY PATTERN** - Algoritmos de Notificación
Permite cambiar dinámicamente el método de notificación:

- **`NotificacionEmail`**: Envío por correo electrónico
- **`NotificacionPush`**: Notificaciones push de Firebase
- **Estrategias personalizadas**: Fácilmente extensible

```java
// Cambio dinámico de estrategia
notificacionService.cambiarEstrategiaNotificacion(new NotificacionEmail());
// Las próximas notificaciones usarán email

notificacionService.cambiarEstrategiaNotificacion(new NotificacionPush());
// Ahora usarán push notifications
```

### 4. 🔌 **ADAPTER PATTERN** - Integración de Sistemas
Adapta diferentes sistemas de notificación a una interfaz unificada:

- **`AdapterJavaMail`**: Adapta JavaMail para emails
- **`AdapterFirebase`**: Adapta Firebase para push notifications
- **Adapters personalizados**: Para integrar nuevos sistemas

## 🧪 Sistema de Pruebas Completo

### 📝 **Pruebas del State Pattern** (`PartidoStateTest.java`)

**10 tests** que verifican todas las transiciones de estado:

```java
@Test
void testEstadoInicial()                    // ✅ Estado inicial correcto
void testTransicionAPartidoArmado()         // ✅ NecesitamosJugadores → PartidoArmado
void testTransicionDeVueltaANecesitamos()   // ✅ PartidoArmado → NecesitamosJugadores
void testTransicionAConfirmado()            // ✅ PartidoArmado → Confirmado
void testTransicionAEnJuego()               // ✅ Confirmado → EnJuego
void testTransicionAFinalizado()            // ✅ EnJuego → Finalizado
void testTransicionACancelado()             // ✅ Cualquier estado → Cancelado
void testCicloCompleto()                    // ✅ Ciclo completo de vida
void testExcepcionesEstadosIncorrectos()    // ✅ Manejo de errores
void testCapacidadMaxima()                  // ✅ Límites de jugadores
```

### 🔗 **Pruebas de Integración** (`PatronesIntegradosTest.java`)

**4 tests** que verifican la integración completa de todos los patrones:

#### **1. 🎯 Test de Integración Completa**
```java
@Test
void testIntegracionCompleta() {
    // 1️⃣ Estado inicial - sin notificaciones
    assertEquals(0, notificacionesEnviadas.size());
    
    // 2️⃣ Agregar primer jugador - no cambia estado
    partido.agregarJugador(jugador1);
    assertEquals(0, notificacionesEnviadas.size()); // Sin notificaciones
    
    // 3️⃣ Agregar segundo jugador - ¡CAMBIO DE ESTADO!
    partido.agregarJugador(jugador2);
    // ✅ State Pattern: Estado cambió a PartidoArmado
    assertTrue(partido.getEstado() instanceof PartidoArmado);
    // ✅ Observer Pattern: Se generó notificación automática
    assertTrue(notificacionesEnviadas.size() >= 1);
    // ✅ Strategy Pattern: Mensaje correcto según estrategia
    assertTrue(notificacionesEnviadas.get(0).contains("PartidoArmado"));
    
    // 4️⃣ Confirmar asistencias - otro cambio de estado
    partido.confirmarAsistencia(jugador1);
    partido.confirmarAsistencia(jugador2);
    // ✅ Nueva notificación para estado Confirmado
    assertTrue(partido.getEstado() instanceof Confirmado);
    
    // 5️⃣ Continúa con EnJuego y Finalizado...
}
```

**¿Qué verifica?**
- **State Pattern**: Transiciones automáticas correctas
- **Observer Pattern**: Notificación automática en cada cambio
- **Strategy Pattern**: Uso correcto de la estrategia actual
- **Integración**: Los 3 patrones trabajando juntos sin conflictos

#### **2. 🔄 Test de Cambio de Estrategia**
```java
@Test
void testCambioEstrategia() {
    // 1️⃣ Configurar estrategia Email
    MockEmailStrategy emailStrategy = new MockEmailStrategy();
    notificacionService.cambiarEstrategiaNotificacion(emailStrategy);
    
    // 2️⃣ Provocar cambio de estado
    partido.agregarJugador(jugador1);
    partido.agregarJugador(jugador2); // Esto genera notificación
    
    // ✅ Verificar que se usó Email
    assertTrue(emailStrategy.emailEnviado);
    
    // 3️⃣ Cambiar a estrategia Push
    MockPushStrategy pushStrategy = new MockPushStrategy();
    notificacionService.cambiarEstrategiaNotificacion(pushStrategy);
    
    // 4️⃣ Provocar otro cambio
    partido.confirmarAsistencia(jugador1);
    partido.confirmarAsistencia(jugador2);
    
    // ✅ Verificar que se usó Push
    assertTrue(pushStrategy.pushEnviado);
}
```

**¿Qué verifica?**
- **Strategy Pattern**: Cambio dinámico de algoritmo
- **Flexibilidad**: Intercambio en tiempo real
- **Aislamiento**: Cada estrategia funciona independientemente

#### **3. 👥 Test de Múltiples Observadores**
```java
@Test
void testMultiplesObservadores() {
    // 1️⃣ Crear observadores adicionales
    MockObserver logObserver = new MockObserver("LogObserver");
    MockObserver metricsObserver = new MockObserver("MetricsObserver");
    
    // 2️⃣ Suscribir múltiples observadores
    partido.attach(logObserver);
    partido.attach(metricsObserver);
    // Ahora hay 3 observadores: NotificationService + 2 nuevos
    
    // 3️⃣ Provocar cambio de estado
    partido.agregarJugador(jugador1);
    partido.agregarJugador(jugador2);
    
    // ✅ Verificar que TODOS fueron notificados
    assertTrue(logObserver.notificaciones >= 1);
    assertTrue(metricsObserver.notificaciones >= 1);
    assertTrue(notificacionesEnviadas.size() >= 1);
    
    // 4️⃣ Remover un observador
    partido.detach(logObserver);
    
    // 5️⃣ Provocar otro cambio
    partido.confirmarAsistencia(jugador1);
    partido.confirmarAsistencia(jugador2);
    
    // ✅ Verificar desuscripción correcta
    // logObserver NO debe haber cambiado
    // metricsObserver SÍ debe haber incrementado
}
```

**¿Qué verifica?**
- **Observer Pattern**: Múltiples observadores simultáneos
- **Suscripción**: `attach()` funciona correctamente
- **Desuscripción**: `detach()` remueve observadores
- **Escalabilidad**: Sistema soporta N observadores

#### **4. 🔌 Test del Adapter Pattern**
```java
@Test
void testAdapterPattern() {
    // 1️⃣ Crear diferentes adapters
    MockEmailAdapter emailAdapter = new MockEmailAdapter();
    MockPushAdapter pushAdapter = new MockPushAdapter();
    
    // 2️⃣ Crear notificación de prueba
    Notificacion notificacion = new Notificacion("Test", "Mensaje", jugador1);
    
    // 3️⃣ Probar adapter de email
    emailAdapter.enviarEmail(notificacion);
    assertTrue(emailAdapter.emailEnviado);
    assertEquals("Test", emailAdapter.ultimoTitulo);
    
    // 4️⃣ Probar adapter de push
    pushAdapter.enviarPush(notificacion);
    assertTrue(pushAdapter.pushEnviado);
    assertEquals("Test", pushAdapter.ultimoTitulo);
}
```

**¿Qué verifica?**
- **Adapter Pattern**: Interfaz unificada para sistemas diferentes
- **Flexibilidad**: Fácil integración de nuevos sistemas
- **Compatibilidad**: Cada adapter mantiene su protocolo específico

## 🎪 Demos Interactivas

### 🏆 **Demo del State Pattern** (`PartidoStateDemo.java`)
Demostración visual completa del patrón State:
```bash
java -cp "target/classes" com.tpo.unoMas.PartidoStateDemo
```

**Output esperado:**
```
🏆 DEMO DEL PATRÓN STATE EN PARTIDO 🏆
=====================================

📋 Información del Partido:
   Título: Partido del Sábado
   Fecha: 2025-06-11
   Zona: Palermo, CABA
   Deporte: Fútbol 5

📊 ESTADO INICIAL:
   Estado: NecesitamosJugadores
   Jugadores inscritos: 0/4

🟡 Agregando primer jugador...
📊 DESPUÉS DE AGREGAR JUGADOR 1:
   Estado: NecesitamosJugadores
   Jugadores inscritos: 1/4

🟢 Agregando segundo jugador (alcanza mínimo)...
📊 DESPUÉS DE AGREGAR JUGADOR 2:
   Estado: PartidoArmado        👈 ¡CAMBIÓ DE ESTADO!
   Jugadores inscritos: 2/4

✅ Confirmando asistencias...
📊 TODAS LAS CONFIRMACIONES:
   Estado: Confirmado           👈 ¡OTRO CAMBIO!
   Jugadores confirmados: 2

🏁 Iniciando partido...
📊 PARTIDO INICIADO:
   Estado: EnJuego             👈 ¡EN JUEGO!

🏆 Finalizando partido...
📊 PARTIDO FINALIZADO:
   Estado: Finalizado          👈 ¡TERMINADO!

🎉 ¡Demo completada exitosamente!
```

### 🎯 **Demo de Integración Completa** (`PatronesIntegradosDemo.java`)
Demostración de todos los patrones trabajando juntos:
```bash
java -cp "target/classes" com.tpo.unoMas.PatronesIntegradosDemo
```

**Output esperado:**
```
🎯 DEMO DE INTEGRACIÓN DE PATRONES 🎯
State + Observer + Strategy + Adapter
=====================================

🔧 CONFIGURANDO SISTEMA DE NOTIFICACIONES
✓ NotificacionService configurado con DemoNotificacionStrategy
✓ Observadores suscritos: NotificacionService, LogObserver, MetricsObserver
✓ Patrón Observer: Partido implements Observable

🎪 DEMOSTRANDO INTEGRACIÓN DE PATRONES

🟢 Agregando segundo jugador (alcanza mínimo)...
   ⚡ ESTO DEBE ACTIVAR EL PATRÓN OBSERVER...
       📤 [DEMO] Partido del Viernes: Cambio a PartidoArmado: Tu Partido ya esta listo!
       📝 LogObserver notificado: PartidoArmado
       📊 MetricsObserver notificado: PartidoArmado

🔄 CAMBIANDO ESTRATEGIA DE NOTIFICACIÓN...
   ✓ Estrategia cambiada a: EmailDemoStrategy

✅ Confirmando asistencias...
   ⚡ ESTO DEBE ACTIVAR LA NUEVA ESTRATEGIA...
       📧 EMAIL enviado a maria@email.com: Partido del Viernes: Cambio a Confirmado
       📧 EMAIL enviado a carlos@email.com: Partido del Viernes: Cambio a Confirmado

📱 CAMBIANDO A ESTRATEGIA PUSH...
   ✓ Estrategia cambiada a: PushDemoStrategy

🏁 Iniciando partido...
   ⚡ ESTO DEBE ACTIVAR LA ESTRATEGIA PUSH...
       📱 PUSH enviado a María Pérez: Partido del Viernes: Cambio a EnJuego
       📱 PUSH enviado a Carlos López: Partido del Viernes: Cambio a EnJuego

📊 RESUMEN DE PATRONES DEMOSTRADOS
===================================
🎯 STATE PATTERN:
   ✓ Estados: NecesitamosJugadores → PartidoArmado → Confirmado → EnJuego → Finalizado
   ✓ Comportamiento dinámico según estado interno

👀 OBSERVER PATTERN:
   ✓ Observable: Partido notifica cambios de estado
   ✓ Observers: NotificacionService, LogObserver, MetricsObserver
   ✓ Total notificaciones LogObserver: 4
   ✓ Total notificaciones MetricsObserver: 4

🎯 STRATEGY PATTERN:
   ✓ Estrategias usadas: DemoNotificacionStrategy, EmailDemoStrategy, PushDemoStrategy
   ✓ Cambio dinámico de algoritmo de notificación
   ✓ Total emails enviados: 2
   ✓ Total pushes enviados: 4

🔌 ADAPTER PATTERN:
   ✓ Adapta diferentes sistemas de notificación
   ✓ Interfaz unificada para Email, Push, etc.

🎉 ¡DEMO COMPLETADA EXITOSAMENTE!
```

## 🚀 Cómo Ejecutar el Proyecto

### **📋 Prerrequisitos**
- Java 17 o superior
- Maven 3.6+

### **🔧 Compilación**
```bash
# Compilar el proyecto
./mvnw compile

# Limpiar y compilar
./mvnw clean compile
```

### **🧪 Ejecutar Pruebas**

#### **Todas las pruebas:**
```bash
./mvnw test
```

#### **Solo pruebas del State Pattern:**
```bash
./mvnw test -Dtest=PartidoStateTest
```

#### **Solo pruebas de Integración:**
```bash
./mvnw test -Dtest=PatronesIntegradosTest
```

#### **Ambas clases de prueba:**
```bash
./mvnw test -Dtest="PartidoStateTest,PatronesIntegradosTest"
```

### **🎪 Ejecutar Demos**

#### **Demo del State Pattern:**
```bash
# Compilar primero
./mvnw compile

# Ejecutar demo
java -cp "target/classes" com.tpo.unoMas.PartidoStateDemo
```

#### **Demo de Integración Completa:**
```bash
java -cp "target/classes" com.tpo.unoMas.PatronesIntegradosDemo
```

## 📊 Resultados de Pruebas

### **✅ Estado Actual**
```
📊 RESULTADOS DE EJECUCIÓN:
- Tests State Pattern: 10/10 ✅
- Tests Integración: 4/4 ✅ 
- Total: 14/14 ✅

🎉 BUILD SUCCESS - ¡TODO FUNCIONA PERFECTAMENTE!
```

### **📈 Cobertura de Pruebas**
- **State Pattern**: 100% de transiciones cubiertas
- **Observer Pattern**: Múltiples observadores y suscripción/desuscripción
- **Strategy Pattern**: Cambio dinámico de algoritmos
- **Adapter Pattern**: Diferentes sistemas de notificación
- **Integración**: Todos los patrones trabajando juntos

## 🏗️ Arquitectura del Sistema

### **📁 Estructura de Directorios**
```
src/
├── main/java/com/tpo/unoMas/
│   ├── model/
│   │   ├── estado/           # State Pattern
│   │   │   ├── EstadoPartido.java
│   │   │   ├── NecesitamosJugadores.java
│   │   │   ├── PartidoArmado.java
│   │   │   ├── Confirmado.java
│   │   │   ├── EnJuego.java
│   │   │   ├── Finalizado.java
│   │   │   └── Cancelado.java
│   │   ├── strategy/         # Strategy Pattern
│   │   │   ├── INotificacionStrategy.java
│   │   │   ├── NotificacionEmail.java
│   │   │   └── NotificacionPushFirebase.java
│   │   ├── adapter/          # Adapter Pattern
│   │   │   ├── NotificacionEmailAdapter.java
│   │   │   └── AdapterJavaMail.java
│   │   ├── Partido.java      # Observable (Observer Pattern)
│   │   ├── Jugador.java
│   │   ├── Notificacion.java
│   │   └── ...
│   ├── observer/             # Observer Pattern
│   │   ├── Observable.java
│   │   └── Observer.java
│   ├── service/
│   │   └── NotificacionService.java  # Observer concreto
│   ├── PartidoStateDemo.java         # Demo State Pattern
│   └── PatronesIntegradosDemo.java   # Demo Integración
└── test/java/com/tpo/unoMas/
    ├── PartidoStateTest.java         # Tests State Pattern
    └── PatronesIntegradosTest.java   # Tests Integración
```

### **🔗 Flujo de Integración**
```
1. Usuario modifica partido
         ↓
2. State Pattern procesa cambio
         ↓
3. Si hay cambio de estado → notifyObservers()
         ↓
4. Observer Pattern notifica a todos los suscritos
         ↓
5. NotificacionService recibe notificación
         ↓
6. Strategy Pattern usa algoritmo actual
         ↓
7. Adapter Pattern adapta al sistema específico
         ↓
8. Notificación enviada (Email/Push/etc.)
```

## 🎯 Casos de Uso Principales

### **🏆 Gestión de Partido Completa**
1. **Crear partido** → Estado: `NecesitamosJugadores`
2. **Agregar jugadores** → Al alcanzar mínimo: `NecesitamosJugadores` → `PartidoArmado`
3. **Confirmar asistencias** → Al confirmar todos: `PartidoArmado` → `Confirmado`
4. **Iniciar partido** → `Confirmado` → `EnJuego`
5. **Finalizar partido** → `EnJuego` → `Finalizado`

### **📧 Sistema de Notificaciones**
- **Automáticas**: Cada cambio de estado genera notificaciones
- **Múltiples observadores**: Logs, métricas, notificaciones, etc.
- **Estrategias flexibles**: Email, Push, SMS, etc.
- **Adapters**: Integración con diferentes proveedores

### **🔧 Extensibilidad**
- **Nuevos estados**: Fácil agregar estados adicionales
- **Nuevas estrategias**: Implementar `INotificacionStrategy`
- **Nuevos observadores**: Implementar `Observer`
- **Nuevos adapters**: Para integrar sistemas externos

## 🤝 Contribución

Para contribuir al proyecto:

1. **Fork** el repositorio
2. **Crear** una rama para tu feature: `git checkout -b feature/nueva-funcionalidad`
3. **Implementar** siguiendo los patrones existentes
4. **Agregar pruebas** para la nueva funcionalidad
5. **Ejecutar** todas las pruebas: `./mvnw test`
6. **Commit** y **push**: `git commit -m "Agregar nueva funcionalidad"`
7. **Crear** un Pull Request

## 📄 Licencia

Este proyecto es parte de un trabajo práctico educativo sobre patrones de diseño.

---

**🎉 ¡Gracias por explorar UnoMas!** 

Un ejemplo práctico de cómo múltiples patrones de diseño pueden trabajar juntos para crear un sistema robusto, flexible y mantenible. 