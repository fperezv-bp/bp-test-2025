package com.banco.notificaciones.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Estado {
    PENDIENTE("Pendiente de envío"),
    ENVIADA("Enviada exitosamente"),
    FALLIDA("Falló el envío");
    
    private final String descripcion;
}
