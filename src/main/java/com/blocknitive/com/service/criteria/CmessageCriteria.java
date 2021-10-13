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
 * Criteria class for the {@link com.blocknitive.com.domain.Cmessage} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.CmessageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cmessages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CmessageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter messageText;

    private BooleanFilter isDelivered;

    private LongFilter csenderId;

    private LongFilter creceiverId;

    private Boolean distinct;

    public CmessageCriteria() {}

    public CmessageCriteria(CmessageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.messageText = other.messageText == null ? null : other.messageText.copy();
        this.isDelivered = other.isDelivered == null ? null : other.isDelivered.copy();
        this.csenderId = other.csenderId == null ? null : other.csenderId.copy();
        this.creceiverId = other.creceiverId == null ? null : other.creceiverId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CmessageCriteria copy() {
        return new CmessageCriteria(this);
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

    public LongFilter getCsenderId() {
        return csenderId;
    }

    public LongFilter csenderId() {
        if (csenderId == null) {
            csenderId = new LongFilter();
        }
        return csenderId;
    }

    public void setCsenderId(LongFilter csenderId) {
        this.csenderId = csenderId;
    }

    public LongFilter getCreceiverId() {
        return creceiverId;
    }

    public LongFilter creceiverId() {
        if (creceiverId == null) {
            creceiverId = new LongFilter();
        }
        return creceiverId;
    }

    public void setCreceiverId(LongFilter creceiverId) {
        this.creceiverId = creceiverId;
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
        final CmessageCriteria that = (CmessageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(messageText, that.messageText) &&
            Objects.equals(isDelivered, that.isDelivered) &&
            Objects.equals(csenderId, that.csenderId) &&
            Objects.equals(creceiverId, that.creceiverId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, messageText, isDelivered, csenderId, creceiverId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CmessageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (messageText != null ? "messageText=" + messageText + ", " : "") +
            (isDelivered != null ? "isDelivered=" + isDelivered + ", " : "") +
            (csenderId != null ? "csenderId=" + csenderId + ", " : "") +
            (creceiverId != null ? "creceiverId=" + creceiverId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
