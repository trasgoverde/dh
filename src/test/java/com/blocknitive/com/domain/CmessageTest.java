package com.blocknitive.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CmessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cmessage.class);
        Cmessage cmessage1 = new Cmessage();
        cmessage1.setId(1L);
        Cmessage cmessage2 = new Cmessage();
        cmessage2.setId(cmessage1.getId());
        assertThat(cmessage1).isEqualTo(cmessage2);
        cmessage2.setId(2L);
        assertThat(cmessage1).isNotEqualTo(cmessage2);
        cmessage1.setId(null);
        assertThat(cmessage1).isNotEqualTo(cmessage2);
    }
}
