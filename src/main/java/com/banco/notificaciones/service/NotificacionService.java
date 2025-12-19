package com.banco.notificaciones.service;

import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.CanalNotificacion;
import com.banco.notificaciones.model.enums.Estado;
import com.banco.notificaciones.model.enums.Prioridad;

import java.math.BigDecimal;
import java.util.List;

public interface NotificacionService {
    
    Notificacion crearNotificacion(String destinatario, String mensaje, 
                                   CanalNotificacion canal, Prioridad prioridad);
    
    boolean enviarNotificacion(String id);
    
    Notificacion obtenerPorId(String id);
    
    List<Notificacion> obtenerPorEstado(Estado estado);
    
    BigDecimal calcularCostoTotal();
}
