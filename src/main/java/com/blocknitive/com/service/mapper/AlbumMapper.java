package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.AlbumDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Album} and its DTO {@link AlbumDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class })
public interface AlbumMapper extends EntityMapper<AlbumDTO, Album> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    AlbumDTO toDto(Album s);

    @Named("title")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    AlbumDTO toDtoTitle(Album album);
}
