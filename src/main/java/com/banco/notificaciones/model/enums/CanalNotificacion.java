package com.banco.notificaciones.model.enums;

/**
 * Enumeración que representa los canales de notificación disponibles
 * con sus costos operativos asociados.
 */
public enum CanalNotificacion {
    EMAIL("Email", 0.10),
    SMS("SMS", 0.50),
    PUSH("Push Notification", 0.05);
    
    private final String descripcion;
    private final double costo;
    
    CanalNotificacion(String descripcion, double costo) {
        this.descripcion = descripcion;
        this.costo = costo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public double getCosto() {
        return costo;
    }
}
