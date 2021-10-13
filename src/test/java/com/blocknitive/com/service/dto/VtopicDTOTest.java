package com.blocknitive.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VtopicDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VtopicDTO.class);
        VtopicDTO vtopicDTO1 = new VtopicDTO();
        vtopicDTO1.setId(1L);
        VtopicDTO vtopicDTO2 = new VtopicDTO();
        assertThat(vtopicDTO1).isNotEqualTo(vtopicDTO2);
        vtopicDTO2.setId(vtopicDTO1.getId());
        assertThat(vtopicDTO1).isEqualTo(vtopicDTO2);
        vtopicDTO2.setId(2L);
        assertThat(vtopicDTO1).isNotEqualTo(vtopicDTO2);
        vtopicDTO1.setId(null);
        assertThat(vtopicDTO1).isNotEqualTo(vtopicDTO2);
    }
}
