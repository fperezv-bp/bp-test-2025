package com.banco.notificaciones.service;

import com.banco.notificaciones.factory.NotificacionStrategyFactory;
import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.CanalNotificacion;
import com.banco.notificaciones.model.enums.Estado;
import com.banco.notificaciones.model.enums.Prioridad;
import com.banco.notificaciones.repository.NotificacionRepository;
import com.banco.notificaciones.strategy.CanalNotificacionStrategy;
import com.banco.notificaciones.strategy.EmailNotificationStrategy;
import com.banco.notificaciones.strategy.SmsNotificationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios del servicio de notificaciones usando Mockito.
 * Cubre las 7 categorías de tests requeridas del servicio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del Servicio de Notificaciones")
class NotificacionServiceImplTest {
    
    @Mock
    private NotificacionRepository repository;
    
    @Mock
    private NotificacionStrategyFactory strategyFactory;
    
    @Mock
    private CanalNotificacionStrategy emailStrategy;
    
    @InjectMocks
    private NotificacionServiceImpl notificacionService;
    
    @BeforeEach
    void setUp() {
        // Configuración común de mocks se hace en cada test según sea necesario
    }
    
    @Test
    @DisplayName("Test 1: Creación exitosa de notificación - Verifica datos asignados correctamente")
    void testCrearNotificacionExitosa() {
        // Given
        String destinatario = "usuario@banco.com";
        String mensaje = "Su estado de cuenta está disponible";
        CanalNotificacion canal = CanalNotificacion.EMAIL;
        Prioridad prioridad = Prioridad.ALTA;
        
        when(emailStrategy.calcularCosto()).thenReturn(new BigDecimal("0.10"));
        when(strategyFactory.getStrategy(canal)).thenReturn(emailStrategy);
        when(repository.guardar(any(Notificacion.class))).thenAnswer(i -> i.getArgument(0));
        
        // When
        Notificacion resultado = notificacionService.crearNotificacion(
                destinatario, mensaje, canal, prioridad);
        
        // Then
        assertNotNull(resultado);
        assertNotNull(resultado.getId()); // ID generado automáticamente
        assertEquals(destinatario, resultado.getDestinatario());
        assertEquals(mensaje, resultado.getMensaje());
        assertEquals(canal, resultado.getCanal());
        assertEquals(prioridad, resultado.getPrioridad());
        assertEquals(Estado.PENDIENTE, resultado.getEstado()); // Estado inicial PENDIENTE
        assertEquals(new BigDecimal("0.10"), resultado.getCosto()); // Costo calculado según canal
        assertNotNull(resultado.getFechaCreacion());
        assertNull(resultado.getFechaEnvio()); // No enviada aún
        
        // Verificar que se guardó en el repositorio
        verify(repository, times(1)).guardar(any(Notificacion.class));
        verify(strategyFactory, times(1)).getStrategy(canal);
    }
    
    @Test
    @DisplayName("Test 2: Validación de destinatario nulo - Debe rechazar")
    void testCrearNotificacionConDestinatarioNulo() {
        // Given
        String destinatario = null;
        String mensaje = "Mensaje de prueba";
        CanalNotificacion canal = CanalNotificacion.EMAIL;
        Prioridad prioridad = Prioridad.MEDIA;
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> notificacionService.crearNotificacion(destinatario, mensaje, canal, prioridad)
        );
        
        assertEquals("El destinatario no puede ser nulo o vacío", exception.getMessage());
        
        // Verificar que NO se persistió
        verify(repository, never()).guardar(any(Notificacion.class));
    }
    
    @Test
    @DisplayName("Test 2: Validación de destinatario vacío - Debe rechazar")
    void testCrearNotificacionConDestinatarioVacio() {
        // Given
        String destinatario = "   ";
        String mensaje = "Mensaje de prueba";
        CanalNotificacion canal = CanalNotificacion.SMS;
        Prioridad prioridad = Prioridad.BAJA;
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> notificacionService.crearNotificacion(destinatario, mensaje, canal, prioridad)
        );
        
        assertEquals("El destinatario no puede ser nulo o vacío", exception.getMessage());
        verify(repository, never()).guardar(any(Notificacion.class));
    }
    
    @Test
    @DisplayName("Test 3: Validación de longitud de mensaje - Rechaza mensajes > 500 caracteres")
    void testCrearNotificacionConMensajeMuyLargo() {
        // Given
        String destinatario = "usuario@banco.com";
        String mensaje = "A".repeat(501); // 501 caracteres
        CanalNotificacion canal = CanalNotificacion.EMAIL;
        Prioridad prioridad = Prioridad.MEDIA;
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> notificacionService.crearNotificacion(destinatario, mensaje, canal, prioridad)
        );
        
        assertTrue(exception.getMessage().contains("500 caracteres"));
        verify(repository, never()).guardar(any(Notificacion.class));
    }
    
    @Test
    @DisplayName("Test 4: Envío exitoso por canal EMAIL - Cambia estado a ENVIADA")
    void testEnviarNotificacionExitosaPorEmail() {
        // Given
        String id = "test-id-123";
        Notificacion notificacion = Notificacion.builder()
                .id(id)
                .destinatario("usuario@banco.com")
                .mensaje("Prueba")
                .canal(CanalNotificacion.EMAIL)
                .prioridad(Prioridad.ALTA)
                .estado(Estado.PENDIENTE)
                .costo(new BigDecimal("0.10"))
                .fechaCreacion(LocalDateTime.now())
                .build();
        
        when(repository.buscarPorId(id)).thenReturn(Optional.of(notificacion));
        when(strategyFactory.getStrategy(CanalNotificacion.EMAIL)).thenReturn(emailStrategy);
        when(emailStrategy.enviar(notificacion)).thenReturn(true);
        when(emailStrategy.getNombreCanal()).thenReturn("EMAIL");
        when(repository.guardar(any(Notificacion.class))).thenAnswer(i -> i.getArgument(0));
        
        // When
        boolean resultado = notificacionService.enviarNotificacion(id);
        
        // Then
        assertTrue(resultado);
        
        // Capturar la notificación guardada
        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(repository, times(1)).guardar(captor.capture());
        
        Notificacion notificacionGuardada = captor.getValue();
        assertEquals(Estado.ENVIADA, notificacionGuardada.getEstado());
        assertNotNull(notificacionGuardada.getFechaEnvio()); // Timestamp registrado
        
        verify(emailStrategy, times(1)).enviar(notificacion);
    }
    
    @Test
    @DisplayName("Test 5: Cálculo de costos totales - Suma correctamente múltiples notificaciones")
    void testCalcularCostoTotal() {
        // Given
        Notificacion notif1 = Notificacion.builder()
                .id("1")
                .costo(new BigDecimal("0.10"))
                .build();
        
        Notificacion notif2 = Notificacion.builder()
                .id("2")
                .costo(new BigDecimal("0.50"))
                .build();
        
        Notificacion notif3 = Notificacion.builder()
                .id("3")
                .costo(new BigDecimal("0.05"))
                .build();
        
        when(repository.listarTodas()).thenReturn(Arrays.asList(notif1, notif2, notif3));
        
        // When
        BigDecimal costoTotal = notificacionService.calcularCostoTotal();
        
        // Then
        assertEquals(new BigDecimal("0.65"), costoTotal);
        verify(repository, times(1)).listarTodas();
    }
    
    @Test
    @DisplayName("Test 5: Cálculo de costos totales - Maneja diferentes canales")
    void testCalcularCostoTotalConDiferentesCanales() {
        // Given - Simular notificaciones de diferentes canales
        List<Notificacion> notificaciones = Arrays.asList(
                Notificacion.builder().id("1").canal(CanalNotificacion.EMAIL)
                        .costo(new BigDecimal("0.10")).build(),
                Notificacion.builder().id("2").canal(CanalNotificacion.SMS)
                        .costo(new BigDecimal("0.50")).build(),
                Notificacion.builder().id("3").canal(CanalNotificacion.PUSH)
                        .costo(new BigDecimal("0.05")).build()
        );
        
        when(repository.listarTodas()).thenReturn(notificaciones);
        
        // When
        BigDecimal costoTotal = notificacionService.calcularCostoTotal();
        
        // Then
        assertEquals(0, new BigDecimal("0.65").compareTo(costoTotal));
    }
    
    @Test
    @DisplayName("Test 6: Filtrado por estado - Recupera solo notificaciones PENDIENTES")
    void testObtenerPorEstadoPendiente() {
        // Given
        Notificacion notif1 = Notificacion.builder()
                .id("1")
                .estado(Estado.PENDIENTE)
                .build();
        
        Notificacion notif2 = Notificacion.builder()
                .id("2")
                .estado(Estado.PENDIENTE)
                .build();
        
        when(repository.filtrarPorEstado(Estado.PENDIENTE))
                .thenReturn(Arrays.asList(notif1, notif2));
        
        // When
        List<Notificacion> resultado = notificacionService.obtenerPorEstado(Estado.PENDIENTE);
        
        // Then
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(n -> n.getEstado() == Estado.PENDIENTE));
        verify(repository, times(1)).filtrarPorEstado(Estado.PENDIENTE);
    }
    
    @Test
    @DisplayName("Test 6: Filtrado por estado - Maneja listas vacías correctamente")
    void testObtenerPorEstadoListaVacia() {
        // Given
        when(repository.filtrarPorEstado(Estado.FALLIDA)).thenReturn(List.of());
        
        // When
        List<Notificacion> resultado = notificacionService.obtenerPorEstado(Estado.FALLIDA);
        
        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    @DisplayName("Test 7: Factory genera estrategia correcta - Verifica creación de EmailStrategy")
    void testFactoryGeneraEstrategiaEmailCorrecta() {
        // Given
        EmailNotificationStrategy realEmailStrategy = new EmailNotificationStrategy();
        when(strategyFactory.getStrategy(CanalNotificacion.EMAIL)).thenReturn(realEmailStrategy);
        when(repository.guardar(any(Notificacion.class))).thenAnswer(i -> i.getArgument(0));
        
        // When
        Notificacion notificacion = notificacionService.crearNotificacion(
                "test@banco.com", "Mensaje", CanalNotificacion.EMAIL, Prioridad.ALTA);
        
        // Then
        assertNotNull(notificacion);
        assertEquals(new BigDecimal("0.10"), notificacion.getCosto());
        verify(strategyFactory, times(1)).getStrategy(CanalNotificacion.EMAIL);
    }
    
    @Test
    @DisplayName("Test 7: Factory genera estrategia correcta - Verifica creación de SmsStrategy")
    void testFactoryGeneraEstrategiaSmsCorrecta() {
        // Given
        SmsNotificationStrategy smsStrategy = new SmsNotificationStrategy();
        when(strategyFactory.getStrategy(CanalNotificacion.SMS)).thenReturn(smsStrategy);
        when(repository.guardar(any(Notificacion.class))).thenAnswer(i -> i.getArgument(0));
        
        // When
        Notificacion notificacion = notificacionService.crearNotificacion(
                "5512345678", "Código: 123456", CanalNotificacion.SMS, Prioridad.ALTA);
        
        // Then
        assertNotNull(notificacion);
        assertEquals(new BigDecimal("0.50"), notificacion.getCosto());
        verify(strategyFactory, times(1)).getStrategy(CanalNotificacion.SMS);
    }
    
    @Test
    @DisplayName("Envío de notificación - Verifica estado FALLIDA cuando falla el envío")
    void testEnviarNotificacionFallida() {
        // Given
        String id = "test-id-fail";
        Notificacion notificacion = Notificacion.builder()
                .id(id)
                .destinatario("invalid")
                .mensaje("Prueba")
                .canal(CanalNotificacion.EMAIL)
                .estado(Estado.PENDIENTE)
                .build();
        
        when(repository.buscarPorId(id)).thenReturn(Optional.of(notificacion));
        when(strategyFactory.getStrategy(CanalNotificacion.EMAIL)).thenReturn(emailStrategy);
        when(emailStrategy.enviar(notificacion)).thenReturn(false);
        when(repository.guardar(any(Notificacion.class))).thenAnswer(i -> i.getArgument(0));
        
        // When
        boolean resultado = notificacionService.enviarNotificacion(id);
        
        // Then
        assertFalse(resultado);
        
        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(repository).guardar(captor.capture());
        
        assertEquals(Estado.FALLIDA, captor.getValue().getEstado());
    }
    
    @Test
    @DisplayName("Obtener por ID - Lanza excepción cuando no existe")
    void testObtenerPorIdNoExistente() {
        // Given
        String idInexistente = "id-que-no-existe";
        when(repository.buscarPorId(idInexistente)).thenReturn(Optional.empty());
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> notificacionService.obtenerPorId(idInexistente)
        );
        
        assertTrue(exception.getMessage().contains("No existe una notificación con ID"));
    }
}
