package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalbumMapperTest {

    private CalbumMapper calbumMapper;

    @BeforeEach
    public void setUp() {
        calbumMapper = new CalbumMapperImpl();
    }
}
