package com.banco.notificaciones.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CanalNotificacion {
    EMAIL("Email", 0.10),
    SMS("SMS", 0.50),
    PUSH("Push Notification", 0.05);
    
    private final String descripcion;
    private final double costo;
}
