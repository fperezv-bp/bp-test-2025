package com.banco.notificaciones.model.enums;

/**
 * Enumeración que representa el estado actual
 * del ciclo de vida de una notificación.
 */
public enum Estado {
    PENDIENTE("Pendiente de envío"),
    ENVIADA("Enviada exitosamente"),
    FALLIDA("Falló el envío");
    
    private final String descripcion;
    
    Estado(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
