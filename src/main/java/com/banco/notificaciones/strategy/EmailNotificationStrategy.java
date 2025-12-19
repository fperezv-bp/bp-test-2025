package com.banco.notificaciones.strategy;

import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.CanalNotificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EmailNotificationStrategy implements CanalNotificacionStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationStrategy.class);
    
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
    public CanalNotificacion getNombreCanal() {
        return CanalNotificacion.EMAIL;
    }
    
    private boolean validarEmail(String email) {
        return email != null && email.contains("@");
    }
}
