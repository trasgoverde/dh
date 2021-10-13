package com.blocknitive.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalbumDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalbumDTO.class);
        CalbumDTO calbumDTO1 = new CalbumDTO();
        calbumDTO1.setId(1L);
        CalbumDTO calbumDTO2 = new CalbumDTO();
        assertThat(calbumDTO1).isNotEqualTo(calbumDTO2);
        calbumDTO2.setId(calbumDTO1.getId());
        assertThat(calbumDTO1).isEqualTo(calbumDTO2);
        calbumDTO2.setId(2L);
        assertThat(calbumDTO1).isNotEqualTo(calbumDTO2);
        calbumDTO1.setId(null);
        assertThat(calbumDTO1).isNotEqualTo(calbumDTO2);
    }
}
