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
 * Criteria class for the {@link com.blocknitive.com.domain.Cinterest} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.CinterestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cinterests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CinterestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter interestName;

    private LongFilter communityId;

    private Boolean distinct;

    public CinterestCriteria() {}

    public CinterestCriteria(CinterestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.interestName = other.interestName == null ? null : other.interestName.copy();
        this.communityId = other.communityId == null ? null : other.communityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CinterestCriteria copy() {
        return new CinterestCriteria(this);
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

    public StringFilter getInterestName() {
        return interestName;
    }

    public StringFilter interestName() {
        if (interestName == null) {
            interestName = new StringFilter();
        }
        return interestName;
    }

    public void setInterestName(StringFilter interestName) {
        this.interestName = interestName;
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
        final CinterestCriteria that = (CinterestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(interestName, that.interestName) &&
            Objects.equals(communityId, that.communityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, interestName, communityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CinterestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (interestName != null ? "interestName=" + interestName + ", " : "") +
            (communityId != null ? "communityId=" + communityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
