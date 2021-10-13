package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.VtopicDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vtopic} and its DTO {@link VtopicDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class })
public interface VtopicMapper extends EntityMapper<VtopicDTO, Vtopic> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    VtopicDTO toDto(Vtopic s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VtopicDTO toDtoId(Vtopic vtopic);
}
