package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.CmessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cmessage} and its DTO {@link CmessageDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommunityMapper.class })
public interface CmessageMapper extends EntityMapper<CmessageDTO, Cmessage> {
    @Mapping(target = "csender", source = "csender", qualifiedByName = "id")
    @Mapping(target = "creceiver", source = "creceiver", qualifiedByName = "id")
    CmessageDTO toDto(Cmessage s);
}
