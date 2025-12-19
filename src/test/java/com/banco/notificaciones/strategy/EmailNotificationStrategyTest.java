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
 * Tests para EmailNotificationStrategy.
 * Valida el comportamiento de la estrategia de Email.
 */
@DisplayName("Tests de EmailNotificationStrategy")
class EmailNotificationStrategyTest {
    
    private EmailNotificationStrategy strategy;
    
    @BeforeEach
    void setUp() {
        strategy = new EmailNotificationStrategy();
        ReflectionTestUtils.setField(strategy, "costo", new BigDecimal("0.10"));
    }
    
    @Test
    @DisplayName("Test 8: Estrategia de Email - Calcula el costo correcto")
    void testCalcularCosto() {
        // Given & When
        BigDecimal costo = strategy.calcularCosto();
        
        // Then
        assertNotNull(costo);
        assertEquals(new BigDecimal("0.10"), costo);
        assertEquals(0, new BigDecimal("0.10").compareTo(costo));
    }
    
    @Test
    @DisplayName("Test 8: Estrategia de Email - Valida formato de email correcto")
    void testEnviarEmailValido() {
        // Given
        Notificacion notificacion = Notificacion.builder()
                .destinatario("usuario@banco.com")
                .mensaje("Estado de cuenta disponible")
                .canal(CanalNotificacion.EMAIL)
                .build();
        
        // When
        boolean resultado = strategy.enviar(notificacion);
        
        // Then
        assertTrue(resultado);
    }
    
    @Test
    @DisplayName("Test 8: Estrategia de Email - Rechaza email sin @")
    void testEnviarEmailInvalido() {
        // Given
        Notificacion notificacion = Notificacion.builder()
                .destinatario("usuariobanco.com")
                .mensaje("Estado de cuenta disponible")
                .canal(CanalNotificacion.EMAIL)
                .build();
        
        // When
        boolean resultado = strategy.enviar(notificacion);
        
        // Then
        assertFalse(resultado);
    }
    
    @Test
    @DisplayName("Estrategia de Email - Verifica nombre del canal")
    void testGetNombreCanal() {
        // When
        CanalNotificacion canal = strategy.getNombreCanal();
        
        // Then
        assertEquals(CanalNotificacion.EMAIL, canal);
    }
}
