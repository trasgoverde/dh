package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VthumbMapperTest {

    private VthumbMapper vthumbMapper;

    @BeforeEach
    public void setUp() {
        vthumbMapper = new VthumbMapperImpl();
    }
}
