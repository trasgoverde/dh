package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppphotoMapperTest {

    private AppphotoMapper appphotoMapper;

    @BeforeEach
    public void setUp() {
        appphotoMapper = new AppphotoMapperImpl();
    }
}
