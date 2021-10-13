package com.blocknitive.com.service.criteria;

import com.blocknitive.com.domain.enumeration.ProposalRole;
import com.blocknitive.com.domain.enumeration.ProposalType;
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
 * Criteria class for the {@link com.blocknitive.com.domain.Proposal} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.ProposalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /proposals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProposalCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ProposalType
     */
    public static class ProposalTypeFilter extends Filter<ProposalType> {

        public ProposalTypeFilter() {}

        public ProposalTypeFilter(ProposalTypeFilter filter) {
            super(filter);
        }

        @Override
        public ProposalTypeFilter copy() {
            return new ProposalTypeFilter(this);
        }
    }

    /**
     * Class for filtering ProposalRole
     */
    public static class ProposalRoleFilter extends Filter<ProposalRole> {

        public ProposalRoleFilter() {}

        public ProposalRoleFilter(ProposalRoleFilter filter) {
            super(filter);
        }

        @Override
        public ProposalRoleFilter copy() {
            return new ProposalRoleFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter proposalName;

    private ProposalTypeFilter proposalType;

    private ProposalRoleFilter proposalRole;

    private InstantFilter releaseDate;

    private BooleanFilter isOpen;

    private BooleanFilter isAccepted;

    private BooleanFilter isPaid;

    private LongFilter proposalVoteId;

    private LongFilter appuserId;

    private LongFilter postId;

    private Boolean distinct;

    public ProposalCriteria() {}

    public ProposalCriteria(ProposalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.proposalName = other.proposalName == null ? null : other.proposalName.copy();
        this.proposalType = other.proposalType == null ? null : other.proposalType.copy();
        this.proposalRole = other.proposalRole == null ? null : other.proposalRole.copy();
        this.releaseDate = other.releaseDate == null ? null : other.releaseDate.copy();
        this.isOpen = other.isOpen == null ? null : other.isOpen.copy();
        this.isAccepted = other.isAccepted == null ? null : other.isAccepted.copy();
        this.isPaid = other.isPaid == null ? null : other.isPaid.copy();
        this.proposalVoteId = other.proposalVoteId == null ? null : other.proposalVoteId.copy();
        this.appuserId = other.appuserId == null ? null : other.appuserId.copy();
        this.postId = other.postId == null ? null : other.postId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProposalCriteria copy() {
        return new ProposalCriteria(this);
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

    public StringFilter getProposalName() {
        return proposalName;
    }

    public StringFilter proposalName() {
        if (proposalName == null) {
            proposalName = new StringFilter();
        }
        return proposalName;
    }

    public void setProposalName(StringFilter proposalName) {
        this.proposalName = proposalName;
    }

    public ProposalTypeFilter getProposalType() {
        return proposalType;
    }

    public ProposalTypeFilter proposalType() {
        if (proposalType == null) {
            proposalType = new ProposalTypeFilter();
        }
        return proposalType;
    }

    public void setProposalType(ProposalTypeFilter proposalType) {
        this.proposalType = proposalType;
    }

    public ProposalRoleFilter getProposalRole() {
        return proposalRole;
    }

    public ProposalRoleFilter proposalRole() {
        if (proposalRole == null) {
            proposalRole = new ProposalRoleFilter();
        }
        return proposalRole;
    }

    public void setProposalRole(ProposalRoleFilter proposalRole) {
        this.proposalRole = proposalRole;
    }

    public InstantFilter getReleaseDate() {
        return releaseDate;
    }

    public InstantFilter releaseDate() {
        if (releaseDate == null) {
            releaseDate = new InstantFilter();
        }
        return releaseDate;
    }

    public void setReleaseDate(InstantFilter releaseDate) {
        this.releaseDate = releaseDate;
    }

    public BooleanFilter getIsOpen() {
        return isOpen;
    }

    public BooleanFilter isOpen() {
        if (isOpen == null) {
            isOpen = new BooleanFilter();
        }
        return isOpen;
    }

    public void setIsOpen(BooleanFilter isOpen) {
        this.isOpen = isOpen;
    }

    public BooleanFilter getIsAccepted() {
        return isAccepted;
    }

    public BooleanFilter isAccepted() {
        if (isAccepted == null) {
            isAccepted = new BooleanFilter();
        }
        return isAccepted;
    }

    public void setIsAccepted(BooleanFilter isAccepted) {
        this.isAccepted = isAccepted;
    }

    public BooleanFilter getIsPaid() {
        return isPaid;
    }

    public BooleanFilter isPaid() {
        if (isPaid == null) {
            isPaid = new BooleanFilter();
        }
        return isPaid;
    }

    public void setIsPaid(BooleanFilter isPaid) {
        this.isPaid = isPaid;
    }

    public LongFilter getProposalVoteId() {
        return proposalVoteId;
    }

    public LongFilter proposalVoteId() {
        if (proposalVoteId == null) {
            proposalVoteId = new LongFilter();
        }
        return proposalVoteId;
    }

    public void setProposalVoteId(LongFilter proposalVoteId) {
        this.proposalVoteId = proposalVoteId;
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

    public LongFilter getPostId() {
        return postId;
    }

    public LongFilter postId() {
        if (postId == null) {
            postId = new LongFilter();
        }
        return postId;
    }

    public void setPostId(LongFilter postId) {
        this.postId = postId;
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
        final ProposalCriteria that = (ProposalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(proposalName, that.proposalName) &&
            Objects.equals(proposalType, that.proposalType) &&
            Objects.equals(proposalRole, that.proposalRole) &&
            Objects.equals(releaseDate, that.releaseDate) &&
            Objects.equals(isOpen, that.isOpen) &&
            Objects.equals(isAccepted, that.isAccepted) &&
            Objects.equals(isPaid, that.isPaid) &&
            Objects.equals(proposalVoteId, that.proposalVoteId) &&
            Objects.equals(appuserId, that.appuserId) &&
            Objects.equals(postId, that.postId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            creationDate,
            proposalName,
            proposalType,
            proposalRole,
            releaseDate,
            isOpen,
            isAccepted,
            isPaid,
            proposalVoteId,
            appuserId,
            postId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProposalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (proposalName != null ? "proposalName=" + proposalName + ", " : "") +
            (proposalType != null ? "proposalType=" + proposalType + ", " : "") +
            (proposalRole != null ? "proposalRole=" + proposalRole + ", " : "") +
            (releaseDate != null ? "releaseDate=" + releaseDate + ", " : "") +
            (isOpen != null ? "isOpen=" + isOpen + ", " : "") +
            (isAccepted != null ? "isAccepted=" + isAccepted + ", " : "") +
            (isPaid != null ? "isPaid=" + isPaid + ", " : "") +
            (proposalVoteId != null ? "proposalVoteId=" + proposalVoteId + ", " : "") +
            (appuserId != null ? "appuserId=" + appuserId + ", " : "") +
            (postId != null ? "postId=" + postId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
