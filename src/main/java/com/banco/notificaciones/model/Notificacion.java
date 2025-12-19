package com.banco.notificaciones.model;

import com.banco.notificaciones.model.enums.CanalNotificacion;
import com.banco.notificaciones.model.enums.Estado;
import com.banco.notificaciones.model.enums.Prioridad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa una notificación bancaria.
 * Contiene toda la información necesaria para el envío y trazabilidad.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {
    
    /**
     * Identificador único de la notificación (UUID)
     */
    private String id;
    
    /**
     * Destinatario de la notificación (email, teléfono o device ID)
     */
    private String destinatario;
    
    /**
     * Contenido del mensaje a enviar
     */
    private String mensaje;
    
    /**
     * Canal por el cual se enviará la notificación
     */
    private CanalNotificacion canal;
    
    /**
     * Nivel de urgencia de la notificación
     */
    private Prioridad prioridad;
    
    /**
     * Estado actual de la notificación en su ciclo de vida
     */
    private Estado estado;
    
    /**
     * Costo operativo del envío de la notificación
     */
    private BigDecimal costo;
    
    /**
     * Timestamp de creación de la notificación
     */
    private LocalDateTime fechaCreacion;
    
    /**
     * Timestamp de envío de la notificación (null si está pendiente)
     */
    private LocalDateTime fechaEnvio;
}
