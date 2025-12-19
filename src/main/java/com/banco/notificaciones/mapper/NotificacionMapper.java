package com.banco.notificaciones.mapper;

import com.banco.notificaciones.dto.NotificacionResponse;
import com.banco.notificaciones.model.Notificacion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificacionMapper {
    
    NotificacionResponse toResponse(Notificacion notificacion);
}
