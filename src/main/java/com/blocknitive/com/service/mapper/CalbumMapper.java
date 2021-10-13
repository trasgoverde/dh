package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.CalbumDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Calbum} and its DTO {@link CalbumDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommunityMapper.class })
public interface CalbumMapper extends EntityMapper<CalbumDTO, Calbum> {
    @Mapping(target = "community", source = "community", qualifiedByName = "communityName")
    CalbumDTO toDto(Calbum s);

    @Named("title")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    CalbumDTO toDtoTitle(Calbum calbum);
}
