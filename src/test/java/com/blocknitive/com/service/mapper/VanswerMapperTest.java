package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VanswerMapperTest {

    private VanswerMapper vanswerMapper;

    @BeforeEach
    public void setUp() {
        vanswerMapper = new VanswerMapperImpl();
    }
}
