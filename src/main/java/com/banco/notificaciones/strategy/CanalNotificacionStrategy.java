package com.banco.notificaciones.strategy;

import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.CanalNotificacion;

import java.math.BigDecimal;

/**
 * Patrón Strategy para canales de notificación.
 * Permite implementar diferentes estrategias de envío sin modificar el código cliente.
 */
public interface CanalNotificacionStrategy {
    
    boolean enviar(Notificacion notificacion);
    
    BigDecimal calcularCosto();
    
    CanalNotificacion getNombreCanal();
}
