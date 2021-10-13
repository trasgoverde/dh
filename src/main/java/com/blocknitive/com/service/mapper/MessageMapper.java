package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.MessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class })
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "sender", source = "sender", qualifiedByName = "id")
    @Mapping(target = "receiver", source = "receiver", qualifiedByName = "id")
    MessageDTO toDto(Message s);
}
