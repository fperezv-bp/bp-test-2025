package com.banco.notificaciones.repository;

import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.Estado;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Repositorio en memoria para almacenar notificaciones.
 * Utiliza un HashMap para almacenamiento temporal sin base de datos.
 */
@Repository
public class NotificacionRepository {
    
    private final Map<String, Notificacion> almacen = new HashMap<>();
    
    /**
     * Guarda una notificación en el almacén.
     * 
     * @param notificacion La notificación a guardar
     * @return La notificación guardada
     */
    public Notificacion guardar(Notificacion notificacion) {
        if (notificacion == null || notificacion.getId() == null) {
            throw new IllegalArgumentException("La notificación y su ID no pueden ser nulos");
        }
        almacen.put(notificacion.getId(), notificacion);
        return notificacion;
    }
    
    /**
     * Busca una notificación por su ID.
     * 
     * @param id El identificador de la notificación
     * @return Un Optional con la notificación si existe, vacío en caso contrario
     */
    public Optional<Notificacion> buscarPorId(String id) {
        return Optional.ofNullable(almacen.get(id));
    }
    
    /**
     * Lista todas las notificaciones almacenadas.
     * 
     * @return Lista de todas las notificaciones
     */
    public List<Notificacion> listarTodas() {
        return new ArrayList<>(almacen.values());
    }
    
    /**
     * Filtra notificaciones por estado.
     * 
     * @param estado El estado por el cual filtrar
     * @return Lista de notificaciones con el estado especificado
     */
    public List<Notificacion> filtrarPorEstado(Estado estado) {
        return almacen.values().stream()
                .filter(n -> n.getEstado() == estado)
                .collect(Collectors.toList());
    }
    
    /**
     * Cuenta el número total de notificaciones almacenadas.
     * 
     * @return El número de notificaciones
     */
    public long contar() {
        return almacen.size();
    }
    
    /**
     * Limpia el almacén eliminando todas las notificaciones.
     */
    public void limpiar() {
        almacen.clear();
    }
    
    /**
     * Elimina una notificación por su ID.
     * 
     * @param id El identificador de la notificación a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminar(String id) {
        return almacen.remove(id) != null;
    }
}
