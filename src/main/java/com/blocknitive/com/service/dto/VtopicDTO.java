package com.blocknitive.com.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.blocknitive.com.domain.Vtopic} entity.
 */
public class VtopicDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 50)
    private String vtopicTitle;

    @Size(min = 2, max = 250)
    private String vtopicDescription;

    private AppuserDTO appuser;

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

    public String getVtopicTitle() {
        return vtopicTitle;
    }

    public void setVtopicTitle(String vtopicTitle) {
        this.vtopicTitle = vtopicTitle;
    }

    public String getVtopicDescription() {
        return vtopicDescription;
    }

    public void setVtopicDescription(String vtopicDescription) {
        this.vtopicDescription = vtopicDescription;
    }

    public AppuserDTO getAppuser() {
        return appuser;
    }

    public void setAppuser(AppuserDTO appuser) {
        this.appuser = appuser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VtopicDTO)) {
            return false;
        }

        VtopicDTO vtopicDTO = (VtopicDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vtopicDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VtopicDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", vtopicTitle='" + getVtopicTitle() + "'" +
            ", vtopicDescription='" + getVtopicDescription() + "'" +
            ", appuser=" + getAppuser() +
            "}";
    }
}
