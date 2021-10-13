package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.AppprofileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appprofile} and its DTO {@link AppprofileDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class })
public interface AppprofileMapper extends EntityMapper<AppprofileDTO, Appprofile> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    AppprofileDTO toDto(Appprofile s);
}
