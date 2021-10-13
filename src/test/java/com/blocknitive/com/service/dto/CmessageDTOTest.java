package com.blocknitive.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CmessageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CmessageDTO.class);
        CmessageDTO cmessageDTO1 = new CmessageDTO();
        cmessageDTO1.setId(1L);
        CmessageDTO cmessageDTO2 = new CmessageDTO();
        assertThat(cmessageDTO1).isNotEqualTo(cmessageDTO2);
        cmessageDTO2.setId(cmessageDTO1.getId());
        assertThat(cmessageDTO1).isEqualTo(cmessageDTO2);
        cmessageDTO2.setId(2L);
        assertThat(cmessageDTO1).isNotEqualTo(cmessageDTO2);
        cmessageDTO1.setId(null);
        assertThat(cmessageDTO1).isNotEqualTo(cmessageDTO2);
    }
}
