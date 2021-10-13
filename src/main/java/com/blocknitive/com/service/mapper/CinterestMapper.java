package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.CinterestDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cinterest} and its DTO {@link CinterestDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommunityMapper.class })
public interface CinterestMapper extends EntityMapper<CinterestDTO, Cinterest> {
    @Mapping(target = "communities", source = "communities", qualifiedByName = "idSet")
    CinterestDTO toDto(Cinterest s);

    @Mapping(target = "removeCommunity", ignore = true)
    Cinterest toEntity(CinterestDTO cinterestDTO);
}
