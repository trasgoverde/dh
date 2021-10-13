package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.ProposalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proposal} and its DTO {@link ProposalDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class, PostMapper.class })
public interface ProposalMapper extends EntityMapper<ProposalDTO, Proposal> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    @Mapping(target = "post", source = "post", qualifiedByName = "id")
    ProposalDTO toDto(Proposal s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProposalDTO toDtoId(Proposal proposal);
}
