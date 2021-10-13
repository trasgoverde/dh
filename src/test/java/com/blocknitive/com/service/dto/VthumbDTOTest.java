package com.blocknitive.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VthumbDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VthumbDTO.class);
        VthumbDTO vthumbDTO1 = new VthumbDTO();
        vthumbDTO1.setId(1L);
        VthumbDTO vthumbDTO2 = new VthumbDTO();
        assertThat(vthumbDTO1).isNotEqualTo(vthumbDTO2);
        vthumbDTO2.setId(vthumbDTO1.getId());
        assertThat(vthumbDTO1).isEqualTo(vthumbDTO2);
        vthumbDTO2.setId(2L);
        assertThat(vthumbDTO1).isNotEqualTo(vthumbDTO2);
        vthumbDTO1.setId(null);
        assertThat(vthumbDTO1).isNotEqualTo(vthumbDTO2);
    }
}
