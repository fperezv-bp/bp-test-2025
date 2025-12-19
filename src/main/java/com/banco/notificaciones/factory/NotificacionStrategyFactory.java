package com.banco.notificaciones.factory;

import com.banco.notificaciones.model.enums.CanalNotificacion;
import com.banco.notificaciones.strategy.CanalNotificacionStrategy;
import com.banco.notificaciones.strategy.EmailNotificationStrategy;
import com.banco.notificaciones.strategy.PushNotificationStrategy;
import com.banco.notificaciones.strategy.SmsNotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory para crear estrategias de notificación.
 * Aplica el principio Open/Closed.
 */
@Component
@RequiredArgsConstructor
public class NotificacionStrategyFactory {
    
    private final EmailNotificationStrategy emailStrategy;
    private final SmsNotificationStrategy smsStrategy;
    private final PushNotificationStrategy pushStrategy;
    
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
