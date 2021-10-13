package com.blocknitive.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VthumbTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vthumb.class);
        Vthumb vthumb1 = new Vthumb();
        vthumb1.setId(1L);
        Vthumb vthumb2 = new Vthumb();
        vthumb2.setId(vthumb1.getId());
        assertThat(vthumb1).isEqualTo(vthumb2);
        vthumb2.setId(2L);
        assertThat(vthumb1).isNotEqualTo(vthumb2);
        vthumb1.setId(null);
        assertThat(vthumb1).isNotEqualTo(vthumb2);
    }
}
