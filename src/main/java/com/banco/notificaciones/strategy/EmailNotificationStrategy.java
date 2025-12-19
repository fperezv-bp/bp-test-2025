package com.banco.notificaciones.strategy;

import com.banco.notificaciones.model.Notificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EmailNotificationStrategy implements CanalNotificacionStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationStrategy.class);
    private static final String NOMBRE_CANAL = "EMAIL";
    
    @Value("${notificaciones.canal.email.costo}")
    private BigDecimal costo;
    
    @Override
    public boolean enviar(Notificacion notificacion) {
        if (!validarEmail(notificacion.getDestinatario())) {
            logger.error("Email inválido: {}", notificacion.getDestinatario());
            return false;
        }
        
        logger.info("Enviando email a: {} con mensaje: {}", 
                   notificacion.getDestinatario(), 
                   notificacion.getMensaje());
        
        return true;
    }
    
    @Override
    public BigDecimal calcularCosto() {
        return costo;
    }
    
    @Override
    public String getNombreCanal() {
        return NOMBRE_CANAL;
    }
    
    private boolean validarEmail(String email) {
        return email != null && email.contains("@");
    }
}
