package com.banco.notificaciones.service;

import com.banco.notificaciones.factory.NotificacionStrategyFactory;
import com.banco.notificaciones.model.Notificacion;
import com.banco.notificaciones.model.enums.CanalNotificacion;
import com.banco.notificaciones.model.enums.Estado;
import com.banco.notificaciones.model.enums.Prioridad;
import com.banco.notificaciones.repository.NotificacionRepository;
import com.banco.notificaciones.strategy.CanalNotificacionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de notificaciones.
 * Aplica los principios SOLID:
 * - SRP: Solo gestiona lógica de notificaciones
 * - OCP: Extensible a nuevos canales sin modificar código base
 * - DIP: Depende de abstracciones (Strategy, Repository)
 */
@Service
public class NotificacionServiceImpl implements NotificacionService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificacionServiceImpl.class);
    private static final int MAX_LONGITUD_MENSAJE = 500;
    
    private final NotificacionRepository repository;
    private final NotificacionStrategyFactory strategyFactory;
    
    public NotificacionServiceImpl(NotificacionRepository repository, 
                                  NotificacionStrategyFactory strategyFactory) {
        this.repository = repository;
        this.strategyFactory = strategyFactory;
    }
    
    @Override
    public Notificacion crearNotificacion(String destinatario, String mensaje, 
                                         CanalNotificacion canal, Prioridad prioridad) {
        logger.debug("Creando notificación para destinatario: {}", destinatario);
        
        // Validaciones DRY
        validarDestinatario(destinatario);
        validarMensaje(mensaje);
        
        // Obtener estrategia y calcular costo
        CanalNotificacionStrategy strategy = strategyFactory.getStrategy(canal);
        BigDecimal costo = strategy.calcularCosto();
        
        // Construir notificación
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
        
        // Persistir
        repository.guardar(notificacion);
        
        logger.info("Notificación creada exitosamente con ID: {}", notificacion.getId());
        return notificacion;
    }
    
    @Override
    public boolean enviarNotificacion(String id) {
        logger.debug("Enviando notificación con ID: {}", id);
        
        Notificacion notificacion = obtenerPorId(id);
        
        if (notificacion.getEstado() == Estado.ENVIADA) {
            logger.warn("La notificación {} ya fue enviada", id);
            return true;
        }
        
        // Obtener estrategia correspondiente
        CanalNotificacionStrategy strategy = strategyFactory.getStrategy(notificacion.getCanal());
        
        // Intentar enviar
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
        return exitoso;
    }
    
    @Override
    public Notificacion obtenerPorId(String id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una notificación con ID: " + id));
    }
    
    @Override
    public List<Notificacion> obtenerPorEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        return repository.filtrarPorEstado(estado);
    }
    
    @Override
    public BigDecimal calcularCostoTotal() {
        List<Notificacion> notificaciones = repository.listarTodas();
        
        return notificaciones.stream()
                .map(Notificacion::getCosto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // ========== MÉTODOS PRIVADOS PARA VALIDACIONES DRY ==========
    
    /**
     * Valida que el destinatario no sea nulo ni vacío.
     * Principio DRY: Método reutilizable para validación de destinatario.
     * 
     * @param destinatario El destinatario a validar
     * @throws IllegalArgumentException si el destinatario es inválido
     */
    private void validarDestinatario(String destinatario) {
        if (destinatario == null || destinatario.trim().isEmpty()) {
            throw new IllegalArgumentException("El destinatario no puede ser nulo o vacío");
        }
    }
    
    /**
     * Valida que el mensaje no sea nulo, vacío, ni exceda el límite de caracteres.
     * Principio DRY: Método reutilizable para validación de mensaje.
     * 
     * @param mensaje El mensaje a validar
     * @throws IllegalArgumentException si el mensaje es inválido
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
     * Genera un identificador único usando UUID.
     * Principio DRY: Método reutilizable para generación de IDs.
     * 
     * @return Un identificador único en formato String
     */
    private String generarId() {
        return UUID.randomUUID().toString();
    }
}
