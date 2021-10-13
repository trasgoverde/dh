package com.blocknitive.com.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.blocknitive.com.domain.Vquestion} entity.
 */
public class VquestionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 100)
    private String vquestion;

    @Size(min = 2, max = 250)
    private String vquestionDescription;

    private AppuserDTO appuser;

    private VtopicDTO vtopic;

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

    public String getVquestion() {
        return vquestion;
    }

    public void setVquestion(String vquestion) {
        this.vquestion = vquestion;
    }

    public String getVquestionDescription() {
        return vquestionDescription;
    }

    public void setVquestionDescription(String vquestionDescription) {
        this.vquestionDescription = vquestionDescription;
    }

    public AppuserDTO getAppuser() {
        return appuser;
    }

    public void setAppuser(AppuserDTO appuser) {
        this.appuser = appuser;
    }

    public VtopicDTO getVtopic() {
        return vtopic;
    }

    public void setVtopic(VtopicDTO vtopic) {
        this.vtopic = vtopic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VquestionDTO)) {
            return false;
        }

        VquestionDTO vquestionDTO = (VquestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vquestionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VquestionDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", vquestion='" + getVquestion() + "'" +
            ", vquestionDescription='" + getVquestionDescription() + "'" +
            ", appuser=" + getAppuser() +
            ", vtopic=" + getVtopic() +
            "}";
    }
}
