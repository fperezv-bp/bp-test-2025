package com.banco.notificaciones.service;

import com.banco.notificaciones.dto.CostoTotalResponse;
import com.banco.notificaciones.dto.EnvioResponse;
import com.banco.notificaciones.dto.NotificacionResponse;
import com.banco.notificaciones.factory.NotificacionStrategyFactory;
import com.banco.notificaciones.mapper.NotificacionMapper;
import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.CanalNotificacion;
import com.banco.notificaciones.model.enums.Estado;
import com.banco.notificaciones.model.enums.Prioridad;
import com.banco.notificaciones.repository.NotificacionRepository;
import com.banco.notificaciones.strategy.CanalNotificacionStrategy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Aplica principios SOLID:
 * - SRP: Solo gestiona lógica de notificaciones
 * - OCP: Extensible a nuevos canales sin modificar código base
 * - DIP: Depende de abstracciones (Strategy, Repository)
 */
@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificacionServiceImpl.class);
    private static final int MAX_LONGITUD_MENSAJE = 500;
    
    private final NotificacionRepository repository;
    private final NotificacionStrategyFactory strategyFactory;
    private final NotificacionMapper mapper;
    
    @Override
    public NotificacionResponse crearNotificacion(String destinatario, String mensaje, 
                                         CanalNotificacion canal, Prioridad prioridad) {
        logger.debug("Creando notificación para destinatario: {}", destinatario);
        
        validarDestinatario(destinatario);
        validarMensaje(mensaje);
        
        CanalNotificacionStrategy strategy = strategyFactory.getStrategy(canal);
        BigDecimal costo = strategy.calcularCosto();
        
        Notificacion notificacion = Notificacion.builder()
                .id(generarId())
                .destinatario(destinatario)
                .mensaje(mensaje)
                .canal(canal)
                .prioridad(prioridad)
                .estado(Estado.PENDIENTE)
                .costo(costo)
                .fechaCreacion(LocalDateTime.now())
                .build();
        
        repository.guardar(notificacion);
        
        logger.info("Notificación creada exitosamente con ID: {}", notificacion.getId());
        return mapper.toResponse(notificacion);
    }
    
    @Override
    public EnvioResponse enviarNotificacion(String id) {
        logger.debug("Enviando notificación con ID: {}", id);
        
        Notificacion notificacion = buscarPorId(id);
        
        if (notificacion.getEstado() == Estado.ENVIADA) {
            logger.warn("La notificación {} ya fue enviada", id);
            return EnvioResponse.builder()
                    .exitoso(true)
                    .notificacion(mapper.toResponse(notificacion))
                    .build();
        }
        
        CanalNotificacionStrategy strategy = strategyFactory.getStrategy(notificacion.getCanal());
        
        boolean exitoso = strategy.enviar(notificacion);
        
        if (exitoso) {
            notificacion.setEstado(Estado.ENVIADA);
            notificacion.setFechaEnvio(LocalDateTime.now());
            logger.info("Notificación {} enviada exitosamente por {}", id, strategy.getNombreCanal());
        } else {
            notificacion.setEstado(Estado.FALLIDA);
            logger.error("Falló el envío de la notificación {}", id);
        }
        
        repository.guardar(notificacion);
        
        return EnvioResponse.builder()
                .exitoso(exitoso)
                .notificacion(mapper.toResponse(notificacion))
                .build();
    }
    
    @Override
    public NotificacionResponse obtenerPorId(String id) {
        Notificacion notificacion = buscarPorId(id);
        return mapper.toResponse(notificacion);
    }
    
    private Notificacion buscarPorId(String id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una notificación con ID: " + id));
    }
    
    @Override
    public List<NotificacionResponse> obtenerPorEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        return repository.filtrarPorEstado(estado)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public CostoTotalResponse calcularCostoTotal() {
        List<Notificacion> notificaciones = repository.listarTodas();
        
        BigDecimal costoTotal = notificaciones.stream()
                .map(Notificacion::getCosto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return CostoTotalResponse.builder()
                .costoTotal(costoTotal)
                .build();
    }
    
    /**
     * Principio DRY: Método reutilizable para validación de destinatario.
     */
    private void validarDestinatario(String destinatario) {
        if (destinatario == null || destinatario.trim().isEmpty()) {
            throw new IllegalArgumentException("El destinatario no puede ser nulo o vacío");
        }
    }
    
    /**
     * Principio DRY: Método reutilizable para validación de mensaje.
     */
    private void validarMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede ser nulo o vacío");
        }
        if (mensaje.length() > MAX_LONGITUD_MENSAJE) {
            throw new IllegalArgumentException(
                    String.format("El mensaje no puede exceder %d caracteres", MAX_LONGITUD_MENSAJE));
        }
    }
    
    /**
     * Principio DRY: Método reutilizable para generación de IDs.
     */
    private String generarId() {
        return UUID.randomUUID().toString();
    }
}
