package com.banco.notificaciones.controller;

import com.banco.notificaciones.dto.CrearNotificacionRequest;
import com.banco.notificaciones.dto.NotificacionResponse;
import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.Estado;
import com.banco.notificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar notificaciones bancarias.
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    
    private final NotificacionService notificacionService;
    
    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }
    
    /**
     * Crea una nueva notificación.
     */
    @PostMapping
    public ResponseEntity<NotificacionResponse> crearNotificacion(
            @Valid @RequestBody CrearNotificacionRequest request) {
        
        Notificacion notificacion = notificacionService.crearNotificacion(
                request.getDestinatario(),
                request.getMensaje(),
                request.getCanal(),
                request.getPrioridad()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertirAResponse(notificacion));
    }
    
    /**
     * Envía una notificación por su ID.
     */
    @PostMapping("/{id}/enviar")
    public ResponseEntity<Map<String, Object>> enviarNotificacion(@PathVariable String id) {
        boolean exitoso = notificacionService.enviarNotificacion(id);
        Notificacion notificacion = notificacionService.obtenerPorId(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("exitoso", exitoso);
        response.put("notificacion", convertirAResponse(notificacion));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtiene una notificación por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponse> obtenerPorId(@PathVariable String id) {
        Notificacion notificacion = notificacionService.obtenerPorId(id);
        return ResponseEntity.ok(convertirAResponse(notificacion));
    }
    
    /**
     * Obtiene notificaciones filtradas por estado.
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<NotificacionResponse>> obtenerPorEstado(
            @PathVariable Estado estado) {
        
        List<NotificacionResponse> notificaciones = notificacionService.obtenerPorEstado(estado)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(notificaciones);
    }
    
    /**
     * Calcula el costo total de todas las notificaciones.
     */
    @GetMapping("/costo-total")
    public ResponseEntity<Map<String, BigDecimal>> obtenerCostoTotal() {
        BigDecimal costoTotal = notificacionService.calcularCostoTotal();
        
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("costoTotal", costoTotal);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint de salud para verificar que el servicio está activo.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Sistema de Notificaciones Bancarias");
        return ResponseEntity.ok(response);
    }
    
    // Método helper para convertir entidad a DTO
    private NotificacionResponse convertirAResponse(Notificacion notificacion) {
        return NotificacionResponse.builder()
                .id(notificacion.getId())
                .destinatario(notificacion.getDestinatario())
                .mensaje(notificacion.getMensaje())
                .canal(notificacion.getCanal())
                .prioridad(notificacion.getPrioridad())
                .estado(notificacion.getEstado())
                .costo(notificacion.getCosto())
                .fechaCreacion(notificacion.getFechaCreacion())
                .fechaEnvio(notificacion.getFechaEnvio())
                .build();
    }
}
