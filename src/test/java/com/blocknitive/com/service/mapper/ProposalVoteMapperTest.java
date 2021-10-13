package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProposalVoteMapperTest {

    private ProposalVoteMapper proposalVoteMapper;

    @BeforeEach
    public void setUp() {
        proposalVoteMapper = new ProposalVoteMapperImpl();
    }
}
