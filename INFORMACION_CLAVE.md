# INFORMACIÓN CLAVE DEL SISTEMA DE NOTIFICACIONES BANCARIAS

## Costos por Canal
- Email: $0.10 por notificación
- SMS: $0.50 por notificación
- Push: $0.05 por notificación

## Reglas de Validación
- **Email**: Destinatario debe contener "@"
- **SMS**: Destinatario debe tener exactamente 10 dígitos numéricos
- **Push**: Destinatario debe iniciar con "device_"
- **Mensaje**: Máximo 500 caracteres
- **Destinatario**: No puede ser nulo ni vacío

## Especificaciones Técnicas
- **Estado inicial**: PENDIENTE
- **Tipo de ID**: UUID (String)
- **Cantidad de métodos en Strategy**: 3 (enviar, calcularCosto, getNombreCanal)
- **Cantidad de validaciones privadas (DRY)**: 3
  - validarDestinatario()
  - validarMensaje()
  - generarId()
- **Implementaciones de Strategy**: 3 (EmailNotificationStrategy, SmsNotificationStrategy, PushNotificationStrategy)

## Testing
- **Tests mínimos requeridos**: 10
- **Tests implementados**: 13+
- **Anotación para mocks**: @Mock
- **Anotación para inyección**: @InjectMocks
- **Framework**: JUnit 5 + Mockito
- **Extensión de Mockito**: @ExtendWith(MockitoExtension.class)

## Ciclo de Vida de una Notificación

1. **Creación**: 
   - Estado inicial: PENDIENTE
   - Se genera ID único (UUID)
   - Se calcula el costo según el canal
   - Se registra fecha de creación
   - fechaEnvio es null

2. **Envío**:
   - Se valida el destinatario según reglas del canal
   - Si es exitoso: Estado cambia a ENVIADA, se registra fechaEnvio
   - Si falla: Estado cambia a FALLIDA

3. **Persistencia**:
   - Se guarda en repositorio en memoria (HashMap)
   - Se puede consultar por ID o filtrar por estado

## Principios SOLID Aplicados

### SRP (Single Responsibility Principle)
- Cada clase tiene una única responsabilidad
- NotificacionService: Solo gestiona lógica de notificaciones
- Repository: Solo maneja persistencia
- Strategy: Solo implementa lógica de envío por canal

### OCP (Open/Closed Principle)
- Sistema abierto para extensión: Se pueden agregar nuevos canales
- Cerrado para modificación: No se modifica código existente
- Factory permite agregar canales fácilmente

### DIP (Dependency Inversion Principle)
- El servicio depende de abstracciones (CanalNotificacionStrategy)
- No depende de implementaciones concretas
- Uso de interfaces y dependency injection

## Endpoints REST

### POST /api/notificaciones
Crea una nueva notificación

### POST /api/notificaciones/{id}/enviar
Envía una notificación por su ID

### GET /api/notificaciones/{id}
Obtiene una notificación específica

### GET /api/notificaciones/estado/{estado}
Filtra notificaciones por estado (PENDIENTE, ENVIADA, FALLIDA)

### GET /api/notificaciones/costo-total
Calcula el costo total de todas las notificaciones

### GET /api/notificaciones/health
Health check del servicio

## Enums Implementados

### CanalNotificacion
- EMAIL
- SMS
- PUSH

### Prioridad
- ALTA
- MEDIA
- BAJA

### Estado
- PENDIENTE
- ENVIADA
- FALLIDA

## Métodos del Repositorio

1. guardar(Notificacion): Guarda una notificación
2. buscarPorId(String): Busca por ID (retorna Optional)
3. listarTodas(): Lista todas las notificaciones
4. filtrarPorEstado(Estado): Filtra por estado
5. contar(): Cuenta notificaciones
6. limpiar(): Limpia el almacén
7. eliminar(String): Elimina por ID

## Atributos de la Entidad Notificacion

1. id (String - UUID)
2. destinatario (String)
3. mensaje (String)
4. canal (CanalNotificacion - Enum)
5. prioridad (Prioridad - Enum)
6. estado (Estado - Enum)
7. costo (BigDecimal)
8. fechaCreacion (LocalDateTime)
9. fechaEnvio (LocalDateTime - nullable)

## Tests Implementados

### Tests de Strategy (3+)
1. EmailNotificationStrategyTest
   - Calcula costo correcto ($0.10)
   - Valida formato de email
   - Rechaza email sin @
   
2. SmsNotificationStrategyTest
   - Calcula costo correcto ($0.50)
   - Valida formato de teléfono (10 dígitos)
   - Rechaza teléfonos inválidos
   
3. PushNotificationStrategyTest
   - Calcula costo correcto ($0.05)
   - Valida formato de device ID
   - Rechaza device ID sin prefijo

### Tests de Servicio (7+)
1. Creación exitosa de notificación
2. Validación de destinatario nulo/vacío
3. Validación de longitud de mensaje
4. Envío exitoso por canal EMAIL
5. Cálculo de costos totales
6. Filtrado por estado
7. Factory genera estrategia correcta

## Herramientas de Mockito Usadas

- @Mock: Simular dependencias
- @InjectMocks: Inyectar mocks en clase bajo prueba
- when().thenReturn(): Definir comportamiento de mocks
- verify(): Verificar interacciones
- ArgumentCaptor: Capturar argumentos pasados a mocks
- times(): Verificar número de llamadas
- never(): Verificar que no se llamó
- any(): Matcher para cualquier argumento

## Tecnologías Utilizadas

- Spring Boot 3.2.0
- Java 17
- Maven
- JUnit 5
- Mockito
- Lombok
- SLF4J + Logback
- Spring Web
- Spring Validation
- Docker + Docker Compose
