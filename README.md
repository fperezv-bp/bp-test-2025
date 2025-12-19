# Sistema de Notificaciones Bancarias

Sistema centralizado de notificaciones desarrollado con Spring Boot 3 que soporta múltiples canales de comunicación con diferentes costos operativos.

## 🏗️ Arquitectura

### Patrones de Diseño Implementados

- **Strategy Pattern**: Para manejar diferentes canales de notificación (Email, SMS, Push)
- **Factory Pattern**: Para crear instancias de estrategias de canal
- **Repository Pattern**: Para persistencia en memoria
- **Dependency Injection**: A través de Spring Framework

### Principios SOLID Aplicados

- **SRP** (Single Responsibility Principle): Cada clase tiene una responsabilidad única
- **OCP** (Open/Closed Principle): Extensible a nuevos canales sin modificar código base
- **DIP** (Dependency Inversion Principle): Dependencias basadas en abstracciones

## 📋 Características

### Canales de Notificación

| Canal | Costo | Validación |
|-------|-------|------------|
| EMAIL | $0.10 | Debe contener "@" |
| SMS | $0.50 | Exactamente 10 dígitos numéricos |
| PUSH | $0.05 | Debe iniciar con "device_" |

### Prioridades

- **ALTA**: Para alertas críticas de seguridad
- **MEDIA**: Para comunicaciones importantes
- **BAJA**: Para promociones y recordatorios

### Estados de Notificación

- **PENDIENTE**: Creada pero no enviada
- **ENVIADA**: Enviada exitosamente
- **FALLIDA**: Falló el envío

## 🚀 Inicio Rápido

### Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- Docker y Docker Compose (opcional)

### Ejecución Local

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

### Ejecución con Docker

```bash
# Construir y ejecutar con Docker Compose
docker-compose up --build

# Detener los servicios
docker-compose down
```

## 📡 API REST

### Endpoints Principales

#### Crear Notificación
```bash
POST /api/notificaciones
Content-Type: application/json

{
  "destinatario": "usuario@banco.com",
  "mensaje": "Su estado de cuenta está disponible",
  "canal": "EMAIL",
  "prioridad": "ALTA"
}
```

#### Enviar Notificación
```bash
POST /api/notificaciones/{id}/enviar
```

#### Obtener Notificación por ID
```bash
GET /api/notificaciones/{id}
```

#### Filtrar por Estado
```bash
GET /api/notificaciones/estado/PENDIENTE
GET /api/notificaciones/estado/ENVIADA
GET /api/notificaciones/estado/FALLIDA
```

#### Calcular Costo Total
```bash
GET /api/notificaciones/costo-total
```

#### Health Check
```bash
GET /api/notificaciones/health
```

## 🧪 Testing

El proyecto incluye más de 10 tests unitarios usando JUnit 5 y Mockito.

### Ejecutar Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar con reporte de cobertura
mvn test jacoco:report
```

### Categorías de Tests

- **Tests de Strategy** (3): Validan cada canal de notificación
- **Tests de Servicio** (7+): Validan lógica de negocio con mocks
  - Creación exitosa de notificaciones
  - Validación de destinatarios
  - Validación de longitud de mensaje
  - Envío exitoso por canal
  - Cálculo de costos totales
  - Filtrado por estado
  - Factory genera estrategias correctas

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/banco/notificaciones/
│   │   ├── controller/          # Controladores REST
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── exception/           # Manejo de excepciones
│   │   ├── factory/             # Factory Pattern
│   │   ├── model/               # Entidades y Enums
│   │   ├── repository/          # Capa de persistencia
│   │   ├── service/             # Lógica de negocio
│   │   ├── strategy/            # Strategy Pattern
│   │   └── SistemaNotificacionesApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/banco/notificaciones/
        ├── service/             # Tests del servicio
        └── strategy/            # Tests de estrategias
```

## 🔧 Validaciones DRY

El servicio implementa 3 métodos privados reutilizables:

1. **validarDestinatario()**: No puede ser nulo ni vacío
2. **validarMensaje()**: No puede ser nulo, vacío, ni exceder 500 caracteres
3. **generarId()**: Usa UUID para crear identificadores únicos

## 📊 Especificaciones Técnicas

- **Framework**: Spring Boot 3.2.0
- **Java Version**: 17
- **Build Tool**: Maven
- **Testing**: JUnit 5 + Mockito
- **Logging**: SLF4J + Logback
- **Tipo de ID**: UUID (String)
- **Estado Inicial**: PENDIENTE
- **Máximo Longitud Mensaje**: 500 caracteres
- **Persistencia**: En memoria (HashMap)

## 📝 Ejemplos de Uso

### Crear y Enviar Email

```bash
# 1. Crear notificación
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "cliente@banco.com",
    "mensaje": "Su estado de cuenta de diciembre está disponible",
    "canal": "EMAIL",
    "prioridad": "MEDIA"
  }'

# Respuesta:
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "destinatario": "cliente@banco.com",
  "mensaje": "Su estado de cuenta de diciembre está disponible",
  "canal": "EMAIL",
  "prioridad": "MEDIA",
  "estado": "PENDIENTE",
  "costo": 0.10,
  "fechaCreacion": "2025-12-19T10:30:00",
  "fechaEnvio": null
}

# 2. Enviar notificación
curl -X POST http://localhost:8080/api/notificaciones/a1b2c3d4-e5f6-7890-abcd-ef1234567890/enviar
```

### Crear SMS de Seguridad

```bash
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "5512345678",
    "mensaje": "Código de verificación: 789456. Válido por 5 minutos.",
    "canal": "SMS",
    "prioridad": "ALTA"
  }'
```

### Crear Notificación Push

```bash
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "device_abc123xyz",
    "mensaje": "¡Tienes una promoción especial! 20% de descuento en transferencias.",
    "canal": "PUSH",
    "prioridad": "BAJA"
  }'
```

## ✅ Checklist de Completitud

### Modelos y Enumeraciones
- ✅ Enum CanalNotificacion (EMAIL, SMS, PUSH)
- ✅ Enum Prioridad (ALTA, MEDIA, BAJA)
- ✅ Enum Estado (PENDIENTE, ENVIADA, FALLIDA)
- ✅ Clase Notificación con 9 atributos

### Patrones de Diseño
- ✅ Interfaz Strategy con 3 métodos
- ✅ EmailNotificationStrategy (costo: $0.10)
- ✅ SmsNotificationStrategy (costo: $0.50)
- ✅ PushNotificationStrategy (costo: $0.05)
- ✅ Factory para creación de estrategias

### Capa de Servicio
- ✅ Interfaz de servicio con 5 operaciones
- ✅ Implementación de servicio aplicando SOLID
- ✅ 3 métodos privados para validaciones DRY

### Persistencia
- ✅ Repositorio en memoria funcional
- ✅ Métodos de búsqueda, guardado y filtrado

### Pruebas
- ✅ 7+ tests unitarios del servicio con Mockito
- ✅ 3+ tests de estrategias
- ✅ Todos los tests ejecutan exitosamente

### Adicionales
- ✅ API REST completa con endpoints
- ✅ Manejo de excepciones global
- ✅ DTOs para request/response
- ✅ Docker y Docker Compose configurados
- ✅ Documentación completa

## 🐛 Troubleshooting

### Error: Puerto 8080 en uso
```bash
# Cambiar el puerto en application.properties
server.port=8081
```

### Tests fallan
```bash
# Limpiar y recompilar
mvn clean install -DskipTests
mvn test
```

## 📄 Licencia

Este proyecto es un ejercicio académico para demostrar patrones de diseño y principios SOLID.

## 👨‍💻 Autor

Desarrollado como ejercicio de Sistema de Notificaciones Bancarias
