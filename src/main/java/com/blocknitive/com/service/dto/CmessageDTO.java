package com.blocknitive.com.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.blocknitive.com.domain.Cmessage} entity.
 */
public class CmessageDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 8000)
    private String messageText;

    private Boolean isDelivered;

    private CommunityDTO csender;

    private CommunityDTO creceiver;

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

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Boolean getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public CommunityDTO getCsender() {
        return csender;
    }

    public void setCsender(CommunityDTO csender) {
        this.csender = csender;
    }

    public CommunityDTO getCreceiver() {
        return creceiver;
    }

    public void setCreceiver(CommunityDTO creceiver) {
        this.creceiver = creceiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CmessageDTO)) {
            return false;
        }

        CmessageDTO cmessageDTO = (CmessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cmessageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CmessageDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", messageText='" + getMessageText() + "'" +
            ", isDelivered='" + getIsDelivered() + "'" +
            ", csender=" + getCsender() +
            ", creceiver=" + getCreceiver() +
            "}";
    }
}
