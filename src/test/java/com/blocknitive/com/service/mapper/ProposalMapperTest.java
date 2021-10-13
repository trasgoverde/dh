package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProposalMapperTest {

    private ProposalMapper proposalMapper;

    @BeforeEach
    public void setUp() {
        proposalMapper = new ProposalMapperImpl();
    }
}
