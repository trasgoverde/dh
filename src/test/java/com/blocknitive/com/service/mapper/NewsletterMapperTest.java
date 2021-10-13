package com.blocknitive.com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NewsletterMapperTest {

    private NewsletterMapper newsletterMapper;

    @BeforeEach
    public void setUp() {
        newsletterMapper = new NewsletterMapperImpl();
    }
}
