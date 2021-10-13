package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InterestMapperTest {

    private InterestMapper interestMapper;

    @BeforeEach
    public void setUp() {
        interestMapper = new InterestMapperImpl();
    }
}
