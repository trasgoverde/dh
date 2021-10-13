package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.VthumbDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vthumb} and its DTO {@link VthumbDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class, VquestionMapper.class, VanswerMapper.class })
public interface VthumbMapper extends EntityMapper<VthumbDTO, Vthumb> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    @Mapping(target = "vquestion", source = "vquestion", qualifiedByName = "id")
    @Mapping(target = "vanswer", source = "vanswer", qualifiedByName = "id")
    VthumbDTO toDto(Vthumb s);
}
