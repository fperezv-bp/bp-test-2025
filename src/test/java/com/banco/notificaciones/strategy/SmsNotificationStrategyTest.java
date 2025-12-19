package com.banco.notificaciones.strategy;

import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.CanalNotificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para SmsNotificationStrategy.
 * Valida el comportamiento de la estrategia de SMS.
 */
@DisplayName("Tests de SmsNotificationStrategy")
class SmsNotificationStrategyTest {
    
    private SmsNotificationStrategy strategy;
    
    @BeforeEach
    void setUp() {
        strategy = new SmsNotificationStrategy();
        ReflectionTestUtils.setField(strategy, "costo", new BigDecimal("0.50"));
    }
    
    @Test
    @DisplayName("Test 9: Estrategia de SMS - Calcula el costo correcto")
    void testCalcularCosto() {
        // Given & When
        BigDecimal costo = strategy.calcularCosto();
        
        // Then
        assertNotNull(costo);
        assertEquals(new BigDecimal("0.50"), costo);
        assertEquals(0, new BigDecimal("0.50").compareTo(costo));
    }
    
    @Test
    @DisplayName("Test 9: Estrategia de SMS - Valida formato de teléfono correcto (10 dígitos)")
    void testEnviarSmsValido() {
        // Given
        Notificacion notificacion = Notificacion.builder()
                .destinatario("5512345678")
                .mensaje("Código de verificación: 123456")
                .canal(CanalNotificacion.SMS)
                .build();
        
        // When
        boolean resultado = strategy.enviar(notificacion);
        
        // Then
        assertTrue(resultado);
    }
    
    @Test
    @DisplayName("Test 9: Estrategia de SMS - Rechaza teléfono con menos de 10 dígitos")
    void testEnviarSmsInvalidoMenosDe10Digitos() {
        // Given
        Notificacion notificacion = Notificacion.builder()
                .destinatario("551234567")
                .mensaje("Código de verificación: 123456")
                .canal(CanalNotificacion.SMS)
                .build();
        
        // When
        boolean resultado = strategy.enviar(notificacion);
        
        // Then
        assertFalse(resultado);
    }
    
    @Test
    @DisplayName("Estrategia de SMS - Rechaza teléfono con caracteres no numéricos")
    void testEnviarSmsInvalidoConLetras() {
        // Given
        Notificacion notificacion = Notificacion.builder()
                .destinatario("55123456AB")
                .mensaje("Código de verificación: 123456")
                .canal(CanalNotificacion.SMS)
                .build();
        
        // When
        boolean resultado = strategy.enviar(notificacion);
        
        // Then
        assertFalse(resultado);
    }
    
    @Test
    @DisplayName("Estrategia de SMS - Verifica nombre del canal")
    void testGetNombreCanal() {
        // When
        CanalNotificacion canal = strategy.getNombreCanal();
        
        // Then
        assertEquals(CanalNotificacion.SMS, canal);
    }
}
