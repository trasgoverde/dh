package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class })
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    NotificationDTO toDto(Notification s);
}
