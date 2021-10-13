package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppprofileMapperTest {

    private AppprofileMapper appprofileMapper;

    @BeforeEach
    public void setUp() {
        appprofileMapper = new AppprofileMapperImpl();
    }
}
