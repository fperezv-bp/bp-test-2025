package com.banco.notificaciones.repository;

import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.Estado;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class NotificacionRepository {
    
    private final Map<String, Notificacion> almacen = new HashMap<>();
    
    public Notificacion guardar(Notificacion notificacion) {
        if (notificacion == null || notificacion.getId() == null) {
            throw new IllegalArgumentException("La notificación y su ID no pueden ser nulos");
        }
        almacen.put(notificacion.getId(), notificacion);
        return notificacion;
    }
    
    public Optional<Notificacion> buscarPorId(String id) {
        return Optional.ofNullable(almacen.get(id));
    }
    
    public List<Notificacion> listarTodas() {
        return new ArrayList<>(almacen.values());
    }
    
    public List<Notificacion> filtrarPorEstado(Estado estado) {
        return almacen.values().stream()
                .filter(n -> n.getEstado() == estado)
                .collect(Collectors.toList());
    }
    
    public long contar() {
        return almacen.size();
    }
    
    public void limpiar() {
        almacen.clear();
    }
    
    public boolean eliminar(String id) {
        return almacen.remove(id) != null;
    }
}
