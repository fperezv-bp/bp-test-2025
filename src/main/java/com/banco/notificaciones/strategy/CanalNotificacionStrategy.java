package com.banco.notificaciones.strategy;

import com.banco.notificaciones.model.Notificacion;

import java.math.BigDecimal;

/**
 * Interfaz Strategy que define el contrato para diferentes canales de notificación.
 * Permite implementar diferentes estrategias de envío sin modificar el código cliente.
 */
public interface CanalNotificacionStrategy {
    
    /**
     * Envía una notificación a través del canal específico.
     * 
     * @param notificacion La notificación a enviar
     * @return true si el envío fue exitoso, false en caso contrario
     */
    boolean enviar(Notificacion notificacion);
    
    /**
     * Calcula el costo del envío para este canal.
     * 
     * @return El costo operativo del envío
     */
    BigDecimal calcularCosto();
    
    /**
     * Obtiene el nombre identificador del canal.
     * 
     * @return El nombre del canal
     */
    String getNombreCanal();
}
