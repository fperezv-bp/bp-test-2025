package com.banco.notificaciones.strategy;

import com.banco.notificaciones.model.Notificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Estrategia de notificación por Email.
 * Costo: $0.10 por notificación
 * Validación: El destinatario debe contener "@"
 */
@Component
public class EmailNotificationStrategy implements CanalNotificacionStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationStrategy.class);
    private static final BigDecimal COSTO = new BigDecimal("0.10");
    private static final String NOMBRE_CANAL = "EMAIL";
    
    @Override
    public boolean enviar(Notificacion notificacion) {
        // Validar formato de email
        if (!validarEmail(notificacion.getDestinatario())) {
            logger.error("Email inválido: {}", notificacion.getDestinatario());
            return false;
        }
        
        // Simular envío de email
        logger.info("Enviando email a: {} con mensaje: {}", 
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
     * Valida que el destinatario contenga el símbolo "@"
     */
    private boolean validarEmail(String email) {
        return email != null && email.contains("@");
    }
}
