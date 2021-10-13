package com.blocknitive.com.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.blocknitive.com.domain.Vthumb} entity.
 */
public class VthumbDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    private Boolean vthumbUp;

    private Boolean vthumbDown;

    private AppuserDTO appuser;

    private VquestionDTO vquestion;

    private VanswerDTO vanswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getVthumbUp() {
        return vthumbUp;
    }

    public void setVthumbUp(Boolean vthumbUp) {
        this.vthumbUp = vthumbUp;
    }

    public Boolean getVthumbDown() {
        return vthumbDown;
    }

    public void setVthumbDown(Boolean vthumbDown) {
        this.vthumbDown = vthumbDown;
    }

    public AppuserDTO getAppuser() {
        return appuser;
    }

    public void setAppuser(AppuserDTO appuser) {
        this.appuser = appuser;
    }

    public VquestionDTO getVquestion() {
        return vquestion;
    }

    public void setVquestion(VquestionDTO vquestion) {
        this.vquestion = vquestion;
    }

    public VanswerDTO getVanswer() {
        return vanswer;
    }

    public void setVanswer(VanswerDTO vanswer) {
        this.vanswer = vanswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VthumbDTO)) {
            return false;
        }

        VthumbDTO vthumbDTO = (VthumbDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vthumbDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VthumbDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", vthumbUp='" + getVthumbUp() + "'" +
            ", vthumbDown='" + getVthumbDown() + "'" +
            ", appuser=" + getAppuser() +
            ", vquestion=" + getVquestion() +
            ", vanswer=" + getVanswer() +
            "}";
    }
}
