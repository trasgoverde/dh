package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VtopicMapperTest {

    private VtopicMapper vtopicMapper;

    @BeforeEach
    public void setUp() {
        vtopicMapper = new VtopicMapperImpl();
    }
}
