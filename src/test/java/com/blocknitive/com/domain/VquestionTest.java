package com.blocknitive.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.blocknitive.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VquestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vquestion.class);
        Vquestion vquestion1 = new Vquestion();
        vquestion1.setId(1L);
        Vquestion vquestion2 = new Vquestion();
        vquestion2.setId(vquestion1.getId());
        assertThat(vquestion1).isEqualTo(vquestion2);
        vquestion2.setId(2L);
        assertThat(vquestion1).isNotEqualTo(vquestion2);
        vquestion1.setId(null);
        assertThat(vquestion1).isNotEqualTo(vquestion2);
    }
}
