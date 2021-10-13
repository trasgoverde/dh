package com.blocknitive.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProposalVoteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProposalVoteDTO.class);
        ProposalVoteDTO proposalVoteDTO1 = new ProposalVoteDTO();
        proposalVoteDTO1.setId(1L);
        ProposalVoteDTO proposalVoteDTO2 = new ProposalVoteDTO();
        assertThat(proposalVoteDTO1).isNotEqualTo(proposalVoteDTO2);
        proposalVoteDTO2.setId(proposalVoteDTO1.getId());
        assertThat(proposalVoteDTO1).isEqualTo(proposalVoteDTO2);
        proposalVoteDTO2.setId(2L);
        assertThat(proposalVoteDTO1).isNotEqualTo(proposalVoteDTO2);
        proposalVoteDTO1.setId(null);
        assertThat(proposalVoteDTO1).isNotEqualTo(proposalVoteDTO2);
    }
}
