package com.banco.notificaciones.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CanalNotificacion {
    EMAIL("Email"),
    SMS("SMS"),
    PUSH("Push Notification");
    
    private final String descripcion;
}
