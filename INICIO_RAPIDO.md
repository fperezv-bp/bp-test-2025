# 🚀 GUÍA RÁPIDA DE INICIO

## Inicio Rápido (5 minutos)

### 1️⃣ Ejecutar Tests
```bash
cd /home/fperezv/Workspace/bp-test-2025
mvn test
```

**Resultado esperado:**
```
Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### 2️⃣ Ejecutar Aplicación
```bash
mvn spring-boot:run
```

**La aplicación estará disponible en:** `http://localhost:8080`

### 3️⃣ Probar la API

**Health Check:**
```bash
curl http://localhost:8080/api/notificaciones/health
```

**Crear una notificación:**
```bash
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "usuario@banco.com",
    "mensaje": "Prueba del sistema",
    "canal": "EMAIL",
    "prioridad": "ALTA"
  }'
```

**Respuesta:**
```json
{
  "id": "abc-123-def-456",
  "destinatario": "usuario@banco.com",
  "mensaje": "Prueba del sistema",
  "canal": "EMAIL",
  "prioridad": "ALTA",
  "estado": "PENDIENTE",
  "costo": 0.10,
  "fechaCreacion": "2025-12-19T10:00:00",
  "fechaEnvio": null
}
```

---

## 🐳 Opción Docker

### Ejecutar con Docker Compose
```bash
docker-compose up --build
```

La aplicación estará disponible en `http://localhost:8080`

---

## 📊 Verificar Tests por Categoría

### Tests de Strategy (14 tests)
```bash
mvn -Dtest=*StrategyTest test
```

### Tests de Servicio (13 tests)
```bash
mvn -Dtest=NotificacionServiceImplTest test
```

---

## 📁 Archivos Principales

| Archivo | Descripción |
|---------|-------------|
| `README.md` | Documentación completa |
| `RESUMEN_PROYECTO.md` | Resumen ejecutivo |
| `INFORMACION_CLAVE.md` | Especificaciones técnicas |
| `EJEMPLOS_USO.sh` | Comandos y ejemplos |
| `pom.xml` | Configuración Maven |
| `docker-compose.yml` | Configuración Docker |

---

## 🎯 Casos de Uso Principales

### Caso 1: Alerta de Seguridad por SMS
```bash
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "5512345678",
    "mensaje": "Código de verificación: 123456",
    "canal": "SMS",
    "prioridad": "ALTA"
  }'
```

### Caso 2: Estado de Cuenta por Email
```bash
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "cliente@banco.com",
    "mensaje": "Su estado de cuenta está disponible",
    "canal": "EMAIL",
    "prioridad": "MEDIA"
  }'
```

### Caso 3: Promoción por Push
```bash
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "device_xyz123",
    "mensaje": "¡Promoción especial! 20% de descuento",
    "canal": "PUSH",
    "prioridad": "BAJA"
  }'
```

---

## ✅ Verificación de Funcionamiento

### 1. Tests Pasan
```bash
mvn test
# Debe mostrar: BUILD SUCCESS
```

### 2. Aplicación Inicia
```bash
mvn spring-boot:run
# Debe mostrar: Started SistemaNotificacionesApplication
```

### 3. Health Check Responde
```bash
curl http://localhost:8080/api/notificaciones/health
# Debe retornar: {"status":"UP","service":"Sistema de Notificaciones Bancarias"}
```

---

## 🐛 Troubleshooting

### Puerto 8080 ocupado
```bash
# Cambiar puerto en application.properties
server.port=8081
```

### Tests fallan
```bash
# Limpiar y recompilar
mvn clean install
```

### Docker no inicia
```bash
# Verificar que Docker esté corriendo
docker ps

# Limpiar contenedores anteriores
docker-compose down
docker-compose up --build
```

---

## 📚 Siguiente Paso

Lee el `README.md` para documentación completa y ejemplos avanzados.

**¡Proyecto listo para usar! ✅**
