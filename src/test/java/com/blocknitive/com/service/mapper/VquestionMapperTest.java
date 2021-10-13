package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VquestionMapperTest {

    private VquestionMapper vquestionMapper;

    @BeforeEach
    public void setUp() {
        vquestionMapper = new VquestionMapperImpl();
    }
}
