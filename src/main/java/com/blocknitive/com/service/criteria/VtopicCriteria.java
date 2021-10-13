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
 * Criteria class for the {@link com.blocknitive.com.domain.Vtopic} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.VtopicResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vtopics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VtopicCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter vtopicTitle;

    private StringFilter vtopicDescription;

    private LongFilter vquestionId;

    private LongFilter appuserId;

    private Boolean distinct;

    public VtopicCriteria() {}

    public VtopicCriteria(VtopicCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.vtopicTitle = other.vtopicTitle == null ? null : other.vtopicTitle.copy();
        this.vtopicDescription = other.vtopicDescription == null ? null : other.vtopicDescription.copy();
        this.vquestionId = other.vquestionId == null ? null : other.vquestionId.copy();
        this.appuserId = other.appuserId == null ? null : other.appuserId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VtopicCriteria copy() {
        return new VtopicCriteria(this);
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

    public StringFilter getVtopicTitle() {
        return vtopicTitle;
    }

    public StringFilter vtopicTitle() {
        if (vtopicTitle == null) {
            vtopicTitle = new StringFilter();
        }
        return vtopicTitle;
    }

    public void setVtopicTitle(StringFilter vtopicTitle) {
        this.vtopicTitle = vtopicTitle;
    }

    public StringFilter getVtopicDescription() {
        return vtopicDescription;
    }

    public StringFilter vtopicDescription() {
        if (vtopicDescription == null) {
            vtopicDescription = new StringFilter();
        }
        return vtopicDescription;
    }

    public void setVtopicDescription(StringFilter vtopicDescription) {
        this.vtopicDescription = vtopicDescription;
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
        final VtopicCriteria that = (VtopicCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(vtopicTitle, that.vtopicTitle) &&
            Objects.equals(vtopicDescription, that.vtopicDescription) &&
            Objects.equals(vquestionId, that.vquestionId) &&
            Objects.equals(appuserId, that.appuserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, vtopicTitle, vtopicDescription, vquestionId, appuserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VtopicCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (vtopicTitle != null ? "vtopicTitle=" + vtopicTitle + ", " : "") +
            (vtopicDescription != null ? "vtopicDescription=" + vtopicDescription + ", " : "") +
            (vquestionId != null ? "vquestionId=" + vquestionId + ", " : "") +
            (appuserId != null ? "appuserId=" + appuserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
