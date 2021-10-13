package com.blocknitive.com.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.blocknitive.com.domain.Message} entity.
 */
public class MessageDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 8000)
    private String messageText;

    private Boolean isDelivered;

    private AppuserDTO sender;

    private AppuserDTO receiver;

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

    public AppuserDTO getSender() {
        return sender;
    }

    public void setSender(AppuserDTO sender) {
        this.sender = sender;
    }

    public AppuserDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(AppuserDTO receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageDTO)) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", messageText='" + getMessageText() + "'" +
            ", isDelivered='" + getIsDelivered() + "'" +
            ", sender=" + getSender() +
            ", receiver=" + getReceiver() +
            "}";
    }
}
