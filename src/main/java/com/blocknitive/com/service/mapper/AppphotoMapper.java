package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.AppphotoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appphoto} and its DTO {@link AppphotoDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class })
public interface AppphotoMapper extends EntityMapper<AppphotoDTO, Appphoto> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    AppphotoDTO toDto(Appphoto s);
}
