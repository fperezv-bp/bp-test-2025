package com.banco.notificaciones.dto;

import com.banco.notificaciones.model.enums.CanalNotificacion;
import com.banco.notificaciones.model.enums.Prioridad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para la creación de notificaciones.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearNotificacionRequest {
    
    @NotBlank(message = "El destinatario es obligatorio")
    private String destinatario;
    
    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 500, message = "El mensaje no puede exceder 500 caracteres")
    private String mensaje;
    
    @NotNull(message = "El canal es obligatorio")
    private CanalNotificacion canal;
    
    @NotNull(message = "La prioridad es obligatoria")
    private Prioridad prioridad;
}
