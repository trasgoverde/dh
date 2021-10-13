package com.blocknitive.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NewsletterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NewsletterDTO.class);
        NewsletterDTO newsletterDTO1 = new NewsletterDTO();
        newsletterDTO1.setId(1L);
        NewsletterDTO newsletterDTO2 = new NewsletterDTO();
        assertThat(newsletterDTO1).isNotEqualTo(newsletterDTO2);
        newsletterDTO2.setId(newsletterDTO1.getId());
        assertThat(newsletterDTO1).isEqualTo(newsletterDTO2);
        newsletterDTO2.setId(2L);
        assertThat(newsletterDTO1).isNotEqualTo(newsletterDTO2);
        newsletterDTO1.setId(null);
        assertThat(newsletterDTO1).isNotEqualTo(newsletterDTO2);
    }
}
