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
 * Criteria class for the {@link com.blocknitive.com.domain.Vthumb} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.VthumbResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vthumbs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VthumbCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private BooleanFilter vthumbUp;

    private BooleanFilter vthumbDown;

    private LongFilter appuserId;

    private LongFilter vquestionId;

    private LongFilter vanswerId;

    private Boolean distinct;

    public VthumbCriteria() {}

    public VthumbCriteria(VthumbCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.vthumbUp = other.vthumbUp == null ? null : other.vthumbUp.copy();
        this.vthumbDown = other.vthumbDown == null ? null : other.vthumbDown.copy();
        this.appuserId = other.appuserId == null ? null : other.appuserId.copy();
        this.vquestionId = other.vquestionId == null ? null : other.vquestionId.copy();
        this.vanswerId = other.vanswerId == null ? null : other.vanswerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VthumbCriteria copy() {
        return new VthumbCriteria(this);
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

    public BooleanFilter getVthumbUp() {
        return vthumbUp;
    }

    public BooleanFilter vthumbUp() {
        if (vthumbUp == null) {
            vthumbUp = new BooleanFilter();
        }
        return vthumbUp;
    }

    public void setVthumbUp(BooleanFilter vthumbUp) {
        this.vthumbUp = vthumbUp;
    }

    public BooleanFilter getVthumbDown() {
        return vthumbDown;
    }

    public BooleanFilter vthumbDown() {
        if (vthumbDown == null) {
            vthumbDown = new BooleanFilter();
        }
        return vthumbDown;
    }

    public void setVthumbDown(BooleanFilter vthumbDown) {
        this.vthumbDown = vthumbDown;
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

    public LongFilter getVanswerId() {
        return vanswerId;
    }

    public LongFilter vanswerId() {
        if (vanswerId == null) {
            vanswerId = new LongFilter();
        }
        return vanswerId;
    }

    public void setVanswerId(LongFilter vanswerId) {
        this.vanswerId = vanswerId;
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
        final VthumbCriteria that = (VthumbCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(vthumbUp, that.vthumbUp) &&
            Objects.equals(vthumbDown, that.vthumbDown) &&
            Objects.equals(appuserId, that.appuserId) &&
            Objects.equals(vquestionId, that.vquestionId) &&
            Objects.equals(vanswerId, that.vanswerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, vthumbUp, vthumbDown, appuserId, vquestionId, vanswerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VthumbCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (vthumbUp != null ? "vthumbUp=" + vthumbUp + ", " : "") +
            (vthumbDown != null ? "vthumbDown=" + vthumbDown + ", " : "") +
            (appuserId != null ? "appuserId=" + appuserId + ", " : "") +
            (vquestionId != null ? "vquestionId=" + vquestionId + ", " : "") +
            (vanswerId != null ? "vanswerId=" + vanswerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
