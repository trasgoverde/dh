package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.CactivityDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cactivity} and its DTO {@link CactivityDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommunityMapper.class })
public interface CactivityMapper extends EntityMapper<CactivityDTO, Cactivity> {
    @Mapping(target = "communities", source = "communities", qualifiedByName = "idSet")
    CactivityDTO toDto(Cactivity s);

    @Mapping(target = "removeCommunity", ignore = true)
    Cactivity toEntity(CactivityDTO cactivityDTO);
}
