package com.banco.notificaciones.service;

import com.banco.notificaciones.dto.CostoTotalResponse;
import com.banco.notificaciones.dto.EnvioResponse;
import com.banco.notificaciones.dto.NotificacionResponse;
import com.banco.notificaciones.model.enums.CanalNotificacion;
import com.banco.notificaciones.model.enums.Estado;
import com.banco.notificaciones.model.enums.Prioridad;

import java.util.List;

public interface NotificacionService {
    
    NotificacionResponse crearNotificacion(String destinatario, String mensaje, 
                                   CanalNotificacion canal, Prioridad prioridad);
    
    EnvioResponse enviarNotificacion(String id);
    
    NotificacionResponse obtenerPorId(String id);
    
    List<NotificacionResponse> obtenerPorEstado(Estado estado);
    
    CostoTotalResponse calcularCostoTotal();
}
