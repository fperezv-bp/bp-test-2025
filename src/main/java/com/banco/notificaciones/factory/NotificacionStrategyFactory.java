package com.banco.notificaciones.factory;

import com.banco.notificaciones.model.enums.CanalNotificacion;
import com.banco.notificaciones.strategy.CanalNotificacionStrategy;
import com.banco.notificaciones.strategy.EmailNotificationStrategy;
import com.banco.notificaciones.strategy.PushNotificationStrategy;
import com.banco.notificaciones.strategy.SmsNotificationStrategy;
import org.springframework.stereotype.Component;

/**
 * Factory para crear instancias de estrategias de canal de notificación.
 * Aplica el principio Open/Closed: abierto para extensión, cerrado para modificación.
 */
@Component
public class NotificacionStrategyFactory {
    
    private final EmailNotificationStrategy emailStrategy;
    private final SmsNotificationStrategy smsStrategy;
    private final PushNotificationStrategy pushStrategy;
    
    public NotificacionStrategyFactory(
            EmailNotificationStrategy emailStrategy,
            SmsNotificationStrategy smsStrategy,
            PushNotificationStrategy pushStrategy) {
        this.emailStrategy = emailStrategy;
        this.smsStrategy = smsStrategy;
        this.pushStrategy = pushStrategy;
    }
    
    /**
     * Crea y retorna la estrategia correspondiente según el tipo de canal.
     * 
     * @param canal El tipo de canal de notificación
     * @return La estrategia correspondiente al canal
     * @throws IllegalArgumentException si el canal no está soportado
     */
    public CanalNotificacionStrategy getStrategy(CanalNotificacion canal) {
        if (canal == null) {
            throw new IllegalArgumentException("El canal no puede ser nulo");
        }
        
        return switch (canal) {
            case EMAIL -> emailStrategy;
            case SMS -> smsStrategy;
            case PUSH -> pushStrategy;
        };
    }
}
