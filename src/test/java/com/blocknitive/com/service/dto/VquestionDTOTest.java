package com.blocknitive.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VquestionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VquestionDTO.class);
        VquestionDTO vquestionDTO1 = new VquestionDTO();
        vquestionDTO1.setId(1L);
        VquestionDTO vquestionDTO2 = new VquestionDTO();
        assertThat(vquestionDTO1).isNotEqualTo(vquestionDTO2);
        vquestionDTO2.setId(vquestionDTO1.getId());
        assertThat(vquestionDTO1).isEqualTo(vquestionDTO2);
        vquestionDTO2.setId(2L);
        assertThat(vquestionDTO1).isNotEqualTo(vquestionDTO2);
        vquestionDTO1.setId(null);
        assertThat(vquestionDTO1).isNotEqualTo(vquestionDTO2);
    }
}
