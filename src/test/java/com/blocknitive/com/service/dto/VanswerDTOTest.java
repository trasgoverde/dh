package com.blocknitive.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VanswerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VanswerDTO.class);
        VanswerDTO vanswerDTO1 = new VanswerDTO();
        vanswerDTO1.setId(1L);
        VanswerDTO vanswerDTO2 = new VanswerDTO();
        assertThat(vanswerDTO1).isNotEqualTo(vanswerDTO2);
        vanswerDTO2.setId(vanswerDTO1.getId());
        assertThat(vanswerDTO1).isEqualTo(vanswerDTO2);
        vanswerDTO2.setId(2L);
        assertThat(vanswerDTO1).isNotEqualTo(vanswerDTO2);
        vanswerDTO1.setId(null);
        assertThat(vanswerDTO1).isNotEqualTo(vanswerDTO2);
    }
}
