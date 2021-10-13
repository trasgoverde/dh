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
 * Criteria class for the {@link com.blocknitive.com.domain.ProposalVote} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.ProposalVoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /proposal-votes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProposalVoteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private LongFilter votePoints;

    private LongFilter appuserId;

    private LongFilter proposalId;

    private Boolean distinct;

    public ProposalVoteCriteria() {}

    public ProposalVoteCriteria(ProposalVoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.votePoints = other.votePoints == null ? null : other.votePoints.copy();
        this.appuserId = other.appuserId == null ? null : other.appuserId.copy();
        this.proposalId = other.proposalId == null ? null : other.proposalId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProposalVoteCriteria copy() {
        return new ProposalVoteCriteria(this);
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

    public LongFilter getVotePoints() {
        return votePoints;
    }

    public LongFilter votePoints() {
        if (votePoints == null) {
            votePoints = new LongFilter();
        }
        return votePoints;
    }

    public void setVotePoints(LongFilter votePoints) {
        this.votePoints = votePoints;
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

    public LongFilter getProposalId() {
        return proposalId;
    }

    public LongFilter proposalId() {
        if (proposalId == null) {
            proposalId = new LongFilter();
        }
        return proposalId;
    }

    public void setProposalId(LongFilter proposalId) {
        this.proposalId = proposalId;
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
        final ProposalVoteCriteria that = (ProposalVoteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(votePoints, that.votePoints) &&
            Objects.equals(appuserId, that.appuserId) &&
            Objects.equals(proposalId, that.proposalId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, votePoints, appuserId, proposalId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProposalVoteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (votePoints != null ? "votePoints=" + votePoints + ", " : "") +
            (appuserId != null ? "appuserId=" + appuserId + ", " : "") +
            (proposalId != null ? "proposalId=" + proposalId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
