package com.banco.notificaciones.service;

import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.CanalNotificacion;
import com.banco.notificaciones.model.enums.Estado;
import com.banco.notificaciones.model.enums.Prioridad;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interfaz del servicio de notificaciones.
 * Define las operaciones disponibles para gestionar notificaciones.
 */
public interface NotificacionService {
    
    /**
     * Crea una nueva notificación.
     * 
     * @param destinatario El destinatario de la notificación
     * @param mensaje El contenido del mensaje
     * @param canal El canal de envío
     * @param prioridad El nivel de urgencia
     * @return La notificación creada
     */
    Notificacion crearNotificacion(String destinatario, String mensaje, 
                                   CanalNotificacion canal, Prioridad prioridad);
    
    /**
     * Envía una notificación previamente creada.
     * 
     * @param id El identificador de la notificación
     * @return true si el envío fue exitoso, false en caso contrario
     */
    boolean enviarNotificacion(String id);
    
    /**
     * Obtiene una notificación por su ID.
     * 
     * @param id El identificador de la notificación
     * @return La notificación encontrada
     */
    Notificacion obtenerPorId(String id);
    
    /**
     * Obtiene todas las notificaciones con un estado específico.
     * 
     * @param estado El estado a filtrar
     * @return Lista de notificaciones con el estado especificado
     */
    List<Notificacion> obtenerPorEstado(Estado estado);
    
    /**
     * Calcula el costo total de todas las notificaciones.
     * 
     * @return El costo total acumulado
     */
    BigDecimal calcularCostoTotal();
}
