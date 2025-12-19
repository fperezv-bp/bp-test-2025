package com.banco.notificaciones.strategy;

import com.banco.notificaciones.model.Notificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PushNotificationStrategy implements CanalNotificacionStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(PushNotificationStrategy.class);
    private static final String NOMBRE_CANAL = "PUSH";
    private static final String PREFIJO_DEVICE = "device_";
    
    @Value("${notificaciones.canal.push.costo}")
    private BigDecimal costo;
    
    @Override
    public boolean enviar(Notificacion notificacion) {
        if (!validarDeviceId(notificacion.getDestinatario())) {
            logger.error("Device ID inválido: {}", notificacion.getDestinatario());
            return false;
        }
        
        logger.info("Enviando notificación Push a: {} con mensaje: {}", 
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
    
    private boolean validarDeviceId(String deviceId) {
        return deviceId != null && deviceId.startsWith(PREFIJO_DEVICE);
    }
}
