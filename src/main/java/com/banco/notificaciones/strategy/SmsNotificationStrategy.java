package com.banco.notificaciones.strategy;

import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.CanalNotificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SmsNotificationStrategy implements CanalNotificacionStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(SmsNotificationStrategy.class);
    
    @Value("${notificaciones.canal.sms.costo}")
    private BigDecimal costo;
    
    @Override
    public boolean enviar(Notificacion notificacion) {
        if (!validarTelefono(notificacion.getDestinatario())) {
            logger.error("Número de teléfono inválido: {}", notificacion.getDestinatario());
            return false;
        }
        
        logger.info("Enviando SMS a: {} con mensaje: {}", 
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
        return CanalNotificacion.SMS;
    }
    
    private boolean validarTelefono(String telefono) {
        if (telefono == null) {
            return false;
        }
        return telefono.matches("\\d{10}");
    }
}
