package com.banco.notificaciones.dto;

import com.banco.notificaciones.model.enums.CanalNotificacion;
import com.banco.notificaciones.model.enums.Estado;
import com.banco.notificaciones.model.enums.Prioridad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionResponse {
    
    private String id;
    private String destinatario;
    private String mensaje;
    private CanalNotificacion canal;
    private Prioridad prioridad;
    private Estado estado;
    private BigDecimal costo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEnvio;
}
