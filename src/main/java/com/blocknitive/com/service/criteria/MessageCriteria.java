package com.blocknitive.com.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.blocknitive.com.domain.Message} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.MessageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /messages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MessageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter messageText;

    private BooleanFilter isDelivered;

    private LongFilter senderId;

    private LongFilter receiverId;

    private Boolean distinct;

    public MessageCriteria() {}

    public MessageCriteria(MessageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.messageText = other.messageText == null ? null : other.messageText.copy();
        this.isDelivered = other.isDelivered == null ? null : other.isDelivered.copy();
        this.senderId = other.senderId == null ? null : other.senderId.copy();
        this.receiverId = other.receiverId == null ? null : other.receiverId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MessageCriteria copy() {
        return new MessageCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public InstantFilter creationDate() {
        if (creationDate == null) {
            creationDate = new InstantFilter();
        }
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public StringFilter getMessageText() {
        return messageText;
    }

    public StringFilter messageText() {
        if (messageText == null) {
            messageText = new StringFilter();
        }
        return messageText;
    }

    public void setMessageText(StringFilter messageText) {
        this.messageText = messageText;
    }

    public BooleanFilter getIsDelivered() {
        return isDelivered;
    }

    public BooleanFilter isDelivered() {
        if (isDelivered == null) {
            isDelivered = new BooleanFilter();
        }
        return isDelivered;
    }

    public void setIsDelivered(BooleanFilter isDelivered) {
        this.isDelivered = isDelivered;
    }

    public LongFilter getSenderId() {
        return senderId;
    }

    public LongFilter senderId() {
        if (senderId == null) {
            senderId = new LongFilter();
        }
        return senderId;
    }

    public void setSenderId(LongFilter senderId) {
        this.senderId = senderId;
    }

    public LongFilter getReceiverId() {
        return receiverId;
    }

    public LongFilter receiverId() {
        if (receiverId == null) {
            receiverId = new LongFilter();
        }
        return receiverId;
    }

    public void setReceiverId(LongFilter receiverId) {
        this.receiverId = receiverId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MessageCriteria that = (MessageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(messageText, that.messageText) &&
            Objects.equals(isDelivered, that.isDelivered) &&
            Objects.equals(senderId, that.senderId) &&
            Objects.equals(receiverId, that.receiverId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, messageText, isDelivered, senderId, receiverId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (messageText != null ? "messageText=" + messageText + ", " : "") +
            (isDelivered != null ? "isDelivered=" + isDelivered + ", " : "") +
            (senderId != null ? "senderId=" + senderId + ", " : "") +
            (receiverId != null ? "receiverId=" + receiverId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
