package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.InterestDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Interest} and its DTO {@link InterestDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class })
public interface InterestMapper extends EntityMapper<InterestDTO, Interest> {
    @Mapping(target = "appusers", source = "appusers", qualifiedByName = "idSet")
    InterestDTO toDto(Interest s);

    @Mapping(target = "removeAppuser", ignore = true)
    Interest toEntity(InterestDTO interestDTO);
}
