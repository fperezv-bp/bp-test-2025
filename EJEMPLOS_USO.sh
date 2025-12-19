###############################################
# EJEMPLOS DE USO - API REST
###############################################

# 1. HEALTH CHECK
curl -X GET http://localhost:8080/api/notificaciones/health

# 2. CREAR NOTIFICACIÓN POR EMAIL
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "cliente@banco.com",
    "mensaje": "Su estado de cuenta de diciembre está disponible",
    "canal": "EMAIL",
    "prioridad": "MEDIA"
  }'

# Respuesta esperada:
# {
#   "id": "uuid-generado",
#   "destinatario": "cliente@banco.com",
#   "mensaje": "Su estado de cuenta de diciembre está disponible",
#   "canal": "EMAIL",
#   "prioridad": "MEDIA",
#   "estado": "PENDIENTE",
#   "costo": 0.10,
#   "fechaCreacion": "2025-12-19T10:00:00",
#   "fechaEnvio": null
# }

# 3. CREAR NOTIFICACIÓN POR SMS (Alerta de Seguridad)
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "5512345678",
    "mensaje": "Código de verificación: 789456. Válido por 5 minutos.",
    "canal": "SMS",
    "prioridad": "ALTA"
  }'

# 4. CREAR NOTIFICACIÓN PUSH (Promoción)
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "device_abc123xyz",
    "mensaje": "¡Tienes una promoción especial! 20% de descuento en transferencias.",
    "canal": "PUSH",
    "prioridad": "BAJA"
  }'

# 5. ENVIAR NOTIFICACIÓN (reemplaza {id} con el UUID generado)
curl -X POST http://localhost:8080/api/notificaciones/{id}/enviar

# 6. OBTENER NOTIFICACIÓN POR ID
curl -X GET http://localhost:8080/api/notificaciones/{id}

# 7. OBTENER NOTIFICACIONES PENDIENTES
curl -X GET http://localhost:8080/api/notificaciones/estado/PENDIENTE

# 8. OBTENER NOTIFICACIONES ENVIADAS
curl -X GET http://localhost:8080/api/notificaciones/estado/ENVIADA

# 9. OBTENER NOTIFICACIONES FALLIDAS
curl -X GET http://localhost:8080/api/notificaciones/estado/FALLIDA

# 10. CALCULAR COSTO TOTAL
curl -X GET http://localhost:8080/api/notificaciones/costo-total

# Respuesta esperada:
# {
#   "costoTotal": 0.65
# }

###############################################
# ESCENARIO COMPLETO - FLUJO DE TRABAJO
###############################################

# Paso 1: Crear una notificación de email
NOTIF_ID=$(curl -s -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "usuario@banco.com",
    "mensaje": "Tiene un cargo pendiente en su tarjeta de crédito",
    "canal": "EMAIL",
    "prioridad": "ALTA"
  }' | jq -r '.id')

echo "Notificación creada con ID: $NOTIF_ID"

# Paso 2: Verificar que está pendiente
curl -X GET http://localhost:8080/api/notificaciones/$NOTIF_ID

# Paso 3: Enviar la notificación
curl -X POST http://localhost:8080/api/notificaciones/$NOTIF_ID/enviar

# Paso 4: Verificar que fue enviada
curl -X GET http://localhost:8080/api/notificaciones/$NOTIF_ID

# Paso 5: Ver costo total
curl -X GET http://localhost:8080/api/notificaciones/costo-total

###############################################
# EJEMPLOS DE VALIDACIÓN - CASOS QUE FALLAN
###############################################

# EMAIL SIN @
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "usuariobanco.com",
    "mensaje": "Mensaje de prueba",
    "canal": "EMAIL",
    "prioridad": "MEDIA"
  }'
# Esto creará la notificación pero FALLARÁ al enviarla

# SMS CON MENOS DE 10 DÍGITOS
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "123456789",
    "mensaje": "Código: 123456",
    "canal": "SMS",
    "prioridad": "ALTA"
  }'
# Fallará al enviar

# PUSH SIN PREFIJO device_
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "abc123",
    "mensaje": "Promoción especial",
    "canal": "PUSH",
    "prioridad": "BAJA"
  }'
# Fallará al enviar

# MENSAJE MUY LARGO (más de 500 caracteres)
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d "{
    \"destinatario\": \"test@banco.com\",
    \"mensaje\": \"$(python3 -c 'print("A" * 501)')\",
    \"canal\": \"EMAIL\",
    \"prioridad\": \"MEDIA\"
  }"
# Error 400 - Bad Request

###############################################
# COMANDOS DOCKER
###############################################

# Construir la imagen
docker-compose build

# Iniciar el servicio
docker-compose up

# Iniciar en segundo plano
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener el servicio
docker-compose down

# Reconstruir y reiniciar
docker-compose up --build

###############################################
# COMANDOS MAVEN
###############################################

# Compilar el proyecto
mvn clean install

# Ejecutar tests
mvn test

# Ejecutar solo un test específico
mvn -Dtest=NotificacionServiceImplTest test

# Ejecutar la aplicación
mvn spring-boot:run

# Empaquetar como JAR
mvn clean package

# Ejecutar el JAR generado
java -jar target/sistema-notificaciones-1.0.0.jar

###############################################
# TESTING - VERIFICAR PATRONES
###############################################

# Ver resultados de los tests
cat target/surefire-reports/*.txt

# Contar tests ejecutados
mvn test | grep "Tests run"

# Ver solo errores
mvn test | grep ERROR
