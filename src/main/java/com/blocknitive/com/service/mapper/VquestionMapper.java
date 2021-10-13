package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.VquestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vquestion} and its DTO {@link VquestionDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class, VtopicMapper.class })
public interface VquestionMapper extends EntityMapper<VquestionDTO, Vquestion> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    @Mapping(target = "vtopic", source = "vtopic", qualifiedByName = "id")
    VquestionDTO toDto(Vquestion s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VquestionDTO toDtoId(Vquestion vquestion);
}
