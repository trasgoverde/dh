package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CmessageMapperTest {

    private CmessageMapper cmessageMapper;

    @BeforeEach
    public void setUp() {
        cmessageMapper = new CmessageMapperImpl();
    }
}
