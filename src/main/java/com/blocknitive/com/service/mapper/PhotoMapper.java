package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.PhotoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Photo} and its DTO {@link PhotoDTO}.
 */
@Mapper(componentModel = "spring", uses = { AlbumMapper.class, CalbumMapper.class })
public interface PhotoMapper extends EntityMapper<PhotoDTO, Photo> {
    @Mapping(target = "album", source = "album", qualifiedByName = "title")
    @Mapping(target = "calbum", source = "calbum", qualifiedByName = "title")
    PhotoDTO toDto(Photo s);
}
