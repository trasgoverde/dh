package com.blocknitive.com.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.blocknitive.com.domain.Vanswer} entity.
 */
public class VanswerDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 500)
    private String urlVanswer;

    private Boolean accepted;

    private AppuserDTO appuser;

    private VquestionDTO vquestion;

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

    public String getUrlVanswer() {
        return urlVanswer;
    }

    public void setUrlVanswer(String urlVanswer) {
        this.urlVanswer = urlVanswer;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VanswerDTO)) {
            return false;
        }

        VanswerDTO vanswerDTO = (VanswerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vanswerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VanswerDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", urlVanswer='" + getUrlVanswer() + "'" +
            ", accepted='" + getAccepted() + "'" +
            ", appuser=" + getAppuser() +
            ", vquestion=" + getVquestion() +
            "}";
    }
}
