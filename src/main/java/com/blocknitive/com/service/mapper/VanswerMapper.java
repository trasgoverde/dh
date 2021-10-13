package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.VanswerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vanswer} and its DTO {@link VanswerDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class, VquestionMapper.class })
public interface VanswerMapper extends EntityMapper<VanswerDTO, Vanswer> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    @Mapping(target = "vquestion", source = "vquestion", qualifiedByName = "id")
    VanswerDTO toDto(Vanswer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VanswerDTO toDtoId(Vanswer vanswer);
}
