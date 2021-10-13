package com.blocknitive.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppprofileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appprofile.class);
        Appprofile appprofile1 = new Appprofile();
        appprofile1.setId(1L);
        Appprofile appprofile2 = new Appprofile();
        appprofile2.setId(appprofile1.getId());
        assertThat(appprofile1).isEqualTo(appprofile2);
        appprofile2.setId(2L);
        assertThat(appprofile1).isNotEqualTo(appprofile2);
        appprofile1.setId(null);
        assertThat(appprofile1).isNotEqualTo(appprofile2);
    }
}
