package com.banco.notificaciones.controller;

import com.banco.notificaciones.dto.CostoTotalResponse;
import com.banco.notificaciones.dto.CrearNotificacionRequest;
import com.banco.notificaciones.dto.EnvioResponse;
import com.banco.notificaciones.dto.HealthResponse;
import com.banco.notificaciones.dto.NotificacionResponse;
import com.banco.notificaciones.model.enums.Estado;
import com.banco.notificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {
    
    private final NotificacionService notificacionService;
    
    @PostMapping
    public ResponseEntity<NotificacionResponse> crearNotificacion(
            @Valid @RequestBody CrearNotificacionRequest request) {
        
        NotificacionResponse response = notificacionService.crearNotificacion(
                request.getDestinatario(),
                request.getMensaje(),
                request.getCanal(),
                request.getPrioridad()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/{id}/enviar")
    public ResponseEntity<EnvioResponse> enviarNotificacion(@PathVariable String id) {
        EnvioResponse response = notificacionService.enviarNotificacion(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponse> obtenerPorId(@PathVariable String id) {
        NotificacionResponse response = notificacionService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<NotificacionResponse>> obtenerPorEstado(
            @PathVariable Estado estado) {
        
        List<NotificacionResponse> notificaciones = notificacionService.obtenerPorEstado(estado);
        return ResponseEntity.ok(notificaciones);
    }
    
    @GetMapping("/costo-total")
    public ResponseEntity<CostoTotalResponse> obtenerCostoTotal() {
        CostoTotalResponse response = notificacionService.calcularCostoTotal();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        HealthResponse response = HealthResponse.builder()
                .status("UP")
                .service("Sistema de Notificaciones Bancarias")
                .build();
        return ResponseEntity.ok(response);
    }
}
