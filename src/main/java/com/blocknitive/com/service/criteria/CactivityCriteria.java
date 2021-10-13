package com.blocknitive.com.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.blocknitive.com.domain.Cactivity} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.CactivityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cactivities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CactivityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter activityName;

    private LongFilter communityId;

    private Boolean distinct;

    public CactivityCriteria() {}

    public CactivityCriteria(CactivityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.activityName = other.activityName == null ? null : other.activityName.copy();
        this.communityId = other.communityId == null ? null : other.communityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CactivityCriteria copy() {
        return new CactivityCriteria(this);
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

    public StringFilter getActivityName() {
        return activityName;
    }

    public StringFilter activityName() {
        if (activityName == null) {
            activityName = new StringFilter();
        }
        return activityName;
    }

    public void setActivityName(StringFilter activityName) {
        this.activityName = activityName;
    }

    public LongFilter getCommunityId() {
        return communityId;
    }

    public LongFilter communityId() {
        if (communityId == null) {
            communityId = new LongFilter();
        }
        return communityId;
    }

    public void setCommunityId(LongFilter communityId) {
        this.communityId = communityId;
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
        final CactivityCriteria that = (CactivityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(activityName, that.activityName) &&
            Objects.equals(communityId, that.communityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, activityName, communityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CactivityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (activityName != null ? "activityName=" + activityName + ", " : "") +
            (communityId != null ? "communityId=" + communityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
