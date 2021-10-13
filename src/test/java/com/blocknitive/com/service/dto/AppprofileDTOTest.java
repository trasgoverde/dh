package com.blocknitive.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppprofileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppprofileDTO.class);
        AppprofileDTO appprofileDTO1 = new AppprofileDTO();
        appprofileDTO1.setId(1L);
        AppprofileDTO appprofileDTO2 = new AppprofileDTO();
        assertThat(appprofileDTO1).isNotEqualTo(appprofileDTO2);
        appprofileDTO2.setId(appprofileDTO1.getId());
        assertThat(appprofileDTO1).isEqualTo(appprofileDTO2);
        appprofileDTO2.setId(2L);
        assertThat(appprofileDTO1).isNotEqualTo(appprofileDTO2);
        appprofileDTO1.setId(null);
        assertThat(appprofileDTO1).isNotEqualTo(appprofileDTO2);
    }
}
