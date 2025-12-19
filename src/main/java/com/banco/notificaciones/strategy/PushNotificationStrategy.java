package com.banco.notificaciones.strategy;

import com.banco.notificaciones.model.Notificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Estrategia de notificación Push.
 * Costo: $0.05 por notificación
 * Validación: El destinatario debe iniciar con "device_"
 */
@Component
public class PushNotificationStrategy implements CanalNotificacionStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(PushNotificationStrategy.class);
    private static final BigDecimal COSTO = new BigDecimal("0.05");
    private static final String NOMBRE_CANAL = "PUSH";
    private static final String PREFIJO_DEVICE = "device_";
    
    @Override
    public boolean enviar(Notificacion notificacion) {
        // Validar formato de device ID
        if (!validarDeviceId(notificacion.getDestinatario())) {
            logger.error("Device ID inválido: {}", notificacion.getDestinatario());
            return false;
        }
        
        // Simular envío de notificación push
        logger.info("Enviando notificación Push a: {} con mensaje: {}", 
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
     * Valida que el destinatario inicie con "device_"
     */
    private boolean validarDeviceId(String deviceId) {
        return deviceId != null && deviceId.startsWith(PREFIJO_DEVICE);
    }
}
