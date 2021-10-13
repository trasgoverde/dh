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
 * Criteria class for the {@link com.blocknitive.com.domain.Vanswer} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.VanswerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vanswers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VanswerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter urlVanswer;

    private BooleanFilter accepted;

    private LongFilter vthumbId;

    private LongFilter appuserId;

    private LongFilter vquestionId;

    private Boolean distinct;

    public VanswerCriteria() {}

    public VanswerCriteria(VanswerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.urlVanswer = other.urlVanswer == null ? null : other.urlVanswer.copy();
        this.accepted = other.accepted == null ? null : other.accepted.copy();
        this.vthumbId = other.vthumbId == null ? null : other.vthumbId.copy();
        this.appuserId = other.appuserId == null ? null : other.appuserId.copy();
        this.vquestionId = other.vquestionId == null ? null : other.vquestionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VanswerCriteria copy() {
        return new VanswerCriteria(this);
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

    public StringFilter getUrlVanswer() {
        return urlVanswer;
    }

    public StringFilter urlVanswer() {
        if (urlVanswer == null) {
            urlVanswer = new StringFilter();
        }
        return urlVanswer;
    }

    public void setUrlVanswer(StringFilter urlVanswer) {
        this.urlVanswer = urlVanswer;
    }

    public BooleanFilter getAccepted() {
        return accepted;
    }

    public BooleanFilter accepted() {
        if (accepted == null) {
            accepted = new BooleanFilter();
        }
        return accepted;
    }

    public void setAccepted(BooleanFilter accepted) {
        this.accepted = accepted;
    }

    public LongFilter getVthumbId() {
        return vthumbId;
    }

    public LongFilter vthumbId() {
        if (vthumbId == null) {
            vthumbId = new LongFilter();
        }
        return vthumbId;
    }

    public void setVthumbId(LongFilter vthumbId) {
        this.vthumbId = vthumbId;
    }

    public LongFilter getAppuserId() {
        return appuserId;
    }

    public LongFilter appuserId() {
        if (appuserId == null) {
            appuserId = new LongFilter();
        }
        return appuserId;
    }

    public void setAppuserId(LongFilter appuserId) {
        this.appuserId = appuserId;
    }

    public LongFilter getVquestionId() {
        return vquestionId;
    }

    public LongFilter vquestionId() {
        if (vquestionId == null) {
            vquestionId = new LongFilter();
        }
        return vquestionId;
    }

    public void setVquestionId(LongFilter vquestionId) {
        this.vquestionId = vquestionId;
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
        final VanswerCriteria that = (VanswerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(urlVanswer, that.urlVanswer) &&
            Objects.equals(accepted, that.accepted) &&
            Objects.equals(vthumbId, that.vthumbId) &&
            Objects.equals(appuserId, that.appuserId) &&
            Objects.equals(vquestionId, that.vquestionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, urlVanswer, accepted, vthumbId, appuserId, vquestionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VanswerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (urlVanswer != null ? "urlVanswer=" + urlVanswer + ", " : "") +
            (accepted != null ? "accepted=" + accepted + ", " : "") +
            (vthumbId != null ? "vthumbId=" + vthumbId + ", " : "") +
            (appuserId != null ? "appuserId=" + appuserId + ", " : "") +
            (vquestionId != null ? "vquestionId=" + vquestionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
