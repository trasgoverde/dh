package com.blocknitive.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NewsletterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Newsletter.class);
        Newsletter newsletter1 = new Newsletter();
        newsletter1.setId(1L);
        Newsletter newsletter2 = new Newsletter();
        newsletter2.setId(newsletter1.getId());
        assertThat(newsletter1).isEqualTo(newsletter2);
        newsletter2.setId(2L);
        assertThat(newsletter1).isNotEqualTo(newsletter2);
        newsletter1.setId(null);
        assertThat(newsletter1).isNotEqualTo(newsletter2);
    }
}
