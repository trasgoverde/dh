package com.blocknitive.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VanswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vanswer.class);
        Vanswer vanswer1 = new Vanswer();
        vanswer1.setId(1L);
        Vanswer vanswer2 = new Vanswer();
        vanswer2.setId(vanswer1.getId());
        assertThat(vanswer1).isEqualTo(vanswer2);
        vanswer2.setId(2L);
        assertThat(vanswer1).isNotEqualTo(vanswer2);
        vanswer1.setId(null);
        assertThat(vanswer1).isNotEqualTo(vanswer2);
    }
}
