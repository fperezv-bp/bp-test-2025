package com.banco.notificaciones.strategy;

import com.banco.notificaciones.model.Notificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Estrategia de notificación por SMS.
 * Costo: $0.50 por notificación
 * Validación: El destinatario debe tener exactamente 10 dígitos numéricos
 */
@Component
public class SmsNotificationStrategy implements CanalNotificacionStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(SmsNotificationStrategy.class);
    private static final BigDecimal COSTO = new BigDecimal("0.50");
    private static final String NOMBRE_CANAL = "SMS";
    
    @Override
    public boolean enviar(Notificacion notificacion) {
        // Validar formato de teléfono
        if (!validarTelefono(notificacion.getDestinatario())) {
            logger.error("Número de teléfono inválido: {}", notificacion.getDestinatario());
            return false;
        }
        
        // Simular envío de SMS
        logger.info("Enviando SMS a: {} con mensaje: {}", 
                   notificacion.getDestinatario(), 
                   notificacion.getMensaje());
        
        return true;
    }
    
    @Override
    public BigDecimal calcularCosto() {
        return COSTO;
    }
    
    @Override
    public String getNombreCanal() {
        return NOMBRE_CANAL;
    }
    
    /**
     * Valida que el destinatario tenga exactamente 10 dígitos numéricos
     */
    private boolean validarTelefono(String telefono) {
        if (telefono == null) {
            return false;
        }
        return telefono.matches("\\d{10}");
    }
}
