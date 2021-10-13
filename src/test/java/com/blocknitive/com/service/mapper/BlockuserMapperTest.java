package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockuserMapperTest {

    private BlockuserMapper blockuserMapper;

    @BeforeEach
    public void setUp() {
        blockuserMapper = new BlockuserMapperImpl();
    }
}
