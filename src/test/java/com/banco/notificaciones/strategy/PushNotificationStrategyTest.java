package com.banco.notificaciones.strategy;

import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.CanalNotificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para PushNotificationStrategy.
 * Valida el comportamiento de la estrategia de Push.
 */
@DisplayName("Tests de PushNotificationStrategy")
class PushNotificationStrategyTest {
    
    private PushNotificationStrategy strategy;
    
    @BeforeEach
    void setUp() {
        strategy = new PushNotificationStrategy();
    }
    
    @Test
    @DisplayName("Test 10: Estrategia de Push - Calcula el costo correcto")
    void testCalcularCosto() {
        // Given & When
        BigDecimal costo = strategy.calcularCosto();
        
        // Then
        assertNotNull(costo);
        assertEquals(new BigDecimal("0.05"), costo);
        assertEquals(0, new BigDecimal("0.05").compareTo(costo));
    }
    
    @Test
    @DisplayName("Test 10: Estrategia de Push - Valida formato de device ID correcto")
    void testEnviarPushValido() {
        // Given
        Notificacion notificacion = Notificacion.builder()
                .destinatario("device_abc123xyz")
                .mensaje("Tienes una promoción especial")
                .canal(CanalNotificacion.PUSH)
                .build();
        
        // When
        boolean resultado = strategy.enviar(notificacion);
        
        // Then
        assertTrue(resultado);
    }
    
    @Test
    @DisplayName("Test 10: Estrategia de Push - Rechaza device ID sin prefijo device_")
    void testEnviarPushInvalido() {
        // Given
        Notificacion notificacion = Notificacion.builder()
                .destinatario("abc123xyz")
                .mensaje("Tienes una promoción especial")
                .canal(CanalNotificacion.PUSH)
                .build();
        
        // When
        boolean resultado = strategy.enviar(notificacion);
        
        // Then
        assertFalse(resultado);
    }
    
    @Test
    @DisplayName("Estrategia de Push - Rechaza device ID nulo")
    void testEnviarPushNulo() {
        // Given
        Notificacion notificacion = Notificacion.builder()
                .destinatario(null)
                .mensaje("Tienes una promoción especial")
                .canal(CanalNotificacion.PUSH)
                .build();
        
        // When
        boolean resultado = strategy.enviar(notificacion);
        
        // Then
        assertFalse(resultado);
    }
    
    @Test
    @DisplayName("Estrategia de Push - Verifica nombre del canal")
    void testGetNombreCanal() {
        // When
        String nombreCanal = strategy.getNombreCanal();
        
        // Then
        assertEquals("PUSH", nombreCanal);
    }
}
