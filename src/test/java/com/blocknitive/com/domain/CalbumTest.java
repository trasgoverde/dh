package com.blocknitive.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalbumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Calbum.class);
        Calbum calbum1 = new Calbum();
        calbum1.setId(1L);
        Calbum calbum2 = new Calbum();
        calbum2.setId(calbum1.getId());
        assertThat(calbum1).isEqualTo(calbum2);
        calbum2.setId(2L);
        assertThat(calbum1).isNotEqualTo(calbum2);
        calbum1.setId(null);
        assertThat(calbum1).isNotEqualTo(calbum2);
    }
}
