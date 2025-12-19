package com.banco.notificaciones.model.enums;

/**
 * Enumeración que representa los niveles de prioridad
 * para las notificaciones bancarias.
 */
public enum Prioridad {
    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja");
    
    private final String descripcion;
    
    Prioridad(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
