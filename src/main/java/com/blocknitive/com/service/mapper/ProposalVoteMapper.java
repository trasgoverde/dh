package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.ProposalVoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProposalVote} and its DTO {@link ProposalVoteDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class, ProposalMapper.class })
public interface ProposalVoteMapper extends EntityMapper<ProposalVoteDTO, ProposalVote> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    @Mapping(target = "proposal", source = "proposal", qualifiedByName = "id")
    ProposalVoteDTO toDto(ProposalVote s);
}
