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
 * Criteria class for the {@link com.blocknitive.com.domain.Community} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.CommunityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /communities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommunityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter communityName;

    private StringFilter communityDescription;

    private BooleanFilter isActive;

    private LongFilter blogId;

    private LongFilter csenderId;

    private LongFilter creceiverId;

    private LongFilter cfollowedId;

    private LongFilter cfollowingId;

    private LongFilter cblockeduserId;

    private LongFilter cblockinguserId;

    private LongFilter appuserId;

    private LongFilter calbumId;

    private LongFilter cinterestId;

    private LongFilter cactivityId;

    private LongFilter ccelebId;

    private Boolean distinct;

    public CommunityCriteria() {}

    public CommunityCriteria(CommunityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.communityName = other.communityName == null ? null : other.communityName.copy();
        this.communityDescription = other.communityDescription == null ? null : other.communityDescription.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.blogId = other.blogId == null ? null : other.blogId.copy();
        this.csenderId = other.csenderId == null ? null : other.csenderId.copy();
        this.creceiverId = other.creceiverId == null ? null : other.creceiverId.copy();
        this.cfollowedId = other.cfollowedId == null ? null : other.cfollowedId.copy();
        this.cfollowingId = other.cfollowingId == null ? null : other.cfollowingId.copy();
        this.cblockeduserId = other.cblockeduserId == null ? null : other.cblockeduserId.copy();
        this.cblockinguserId = other.cblockinguserId == null ? null : other.cblockinguserId.copy();
        this.appuserId = other.appuserId == null ? null : other.appuserId.copy();
        this.calbumId = other.calbumId == null ? null : other.calbumId.copy();
        this.cinterestId = other.cinterestId == null ? null : other.cinterestId.copy();
        this.cactivityId = other.cactivityId == null ? null : other.cactivityId.copy();
        this.ccelebId = other.ccelebId == null ? null : other.ccelebId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CommunityCriteria copy() {
        return new CommunityCriteria(this);
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

    public StringFilter getCommunityName() {
        return communityName;
    }

    public StringFilter communityName() {
        if (communityName == null) {
            communityName = new StringFilter();
        }
        return communityName;
    }

    public void setCommunityName(StringFilter communityName) {
        this.communityName = communityName;
    }

    public StringFilter getCommunityDescription() {
        return communityDescription;
    }

    public StringFilter communityDescription() {
        if (communityDescription == null) {
            communityDescription = new StringFilter();
        }
        return communityDescription;
    }

    public void setCommunityDescription(StringFilter communityDescription) {
        this.communityDescription = communityDescription;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getBlogId() {
        return blogId;
    }

    public LongFilter blogId() {
        if (blogId == null) {
            blogId = new LongFilter();
        }
        return blogId;
    }

    public void setBlogId(LongFilter blogId) {
        this.blogId = blogId;
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

    public LongFilter getCfollowedId() {
        return cfollowedId;
    }

    public LongFilter cfollowedId() {
        if (cfollowedId == null) {
            cfollowedId = new LongFilter();
        }
        return cfollowedId;
    }

    public void setCfollowedId(LongFilter cfollowedId) {
        this.cfollowedId = cfollowedId;
    }

    public LongFilter getCfollowingId() {
        return cfollowingId;
    }

    public LongFilter cfollowingId() {
        if (cfollowingId == null) {
            cfollowingId = new LongFilter();
        }
        return cfollowingId;
    }

    public void setCfollowingId(LongFilter cfollowingId) {
        this.cfollowingId = cfollowingId;
    }

    public LongFilter getCblockeduserId() {
        return cblockeduserId;
    }

    public LongFilter cblockeduserId() {
        if (cblockeduserId == null) {
            cblockeduserId = new LongFilter();
        }
        return cblockeduserId;
    }

    public void setCblockeduserId(LongFilter cblockeduserId) {
        this.cblockeduserId = cblockeduserId;
    }

    public LongFilter getCblockinguserId() {
        return cblockinguserId;
    }

    public LongFilter cblockinguserId() {
        if (cblockinguserId == null) {
            cblockinguserId = new LongFilter();
        }
        return cblockinguserId;
    }

    public void setCblockinguserId(LongFilter cblockinguserId) {
        this.cblockinguserId = cblockinguserId;
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

    public LongFilter getCalbumId() {
        return calbumId;
    }

    public LongFilter calbumId() {
        if (calbumId == null) {
            calbumId = new LongFilter();
        }
        return calbumId;
    }

    public void setCalbumId(LongFilter calbumId) {
        this.calbumId = calbumId;
    }

    public LongFilter getCinterestId() {
        return cinterestId;
    }

    public LongFilter cinterestId() {
        if (cinterestId == null) {
            cinterestId = new LongFilter();
        }
        return cinterestId;
    }

    public void setCinterestId(LongFilter cinterestId) {
        this.cinterestId = cinterestId;
    }

    public LongFilter getCactivityId() {
        return cactivityId;
    }

    public LongFilter cactivityId() {
        if (cactivityId == null) {
            cactivityId = new LongFilter();
        }
        return cactivityId;
    }

    public void setCactivityId(LongFilter cactivityId) {
        this.cactivityId = cactivityId;
    }

    public LongFilter getCcelebId() {
        return ccelebId;
    }

    public LongFilter ccelebId() {
        if (ccelebId == null) {
            ccelebId = new LongFilter();
        }
        return ccelebId;
    }

    public void setCcelebId(LongFilter ccelebId) {
        this.ccelebId = ccelebId;
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
        final CommunityCriteria that = (CommunityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(communityName, that.communityName) &&
            Objects.equals(communityDescription, that.communityDescription) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(blogId, that.blogId) &&
            Objects.equals(csenderId, that.csenderId) &&
            Objects.equals(creceiverId, that.creceiverId) &&
            Objects.equals(cfollowedId, that.cfollowedId) &&
            Objects.equals(cfollowingId, that.cfollowingId) &&
            Objects.equals(cblockeduserId, that.cblockeduserId) &&
            Objects.equals(cblockinguserId, that.cblockinguserId) &&
            Objects.equals(appuserId, that.appuserId) &&
            Objects.equals(calbumId, that.calbumId) &&
            Objects.equals(cinterestId, that.cinterestId) &&
            Objects.equals(cactivityId, that.cactivityId) &&
            Objects.equals(ccelebId, that.ccelebId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            creationDate,
            communityName,
            communityDescription,
            isActive,
            blogId,
            csenderId,
            creceiverId,
            cfollowedId,
            cfollowingId,
            cblockeduserId,
            cblockinguserId,
            appuserId,
            calbumId,
            cinterestId,
            cactivityId,
            ccelebId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (communityName != null ? "communityName=" + communityName + ", " : "") +
            (communityDescription != null ? "communityDescription=" + communityDescription + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (blogId != null ? "blogId=" + blogId + ", " : "") +
            (csenderId != null ? "csenderId=" + csenderId + ", " : "") +
            (creceiverId != null ? "creceiverId=" + creceiverId + ", " : "") +
            (cfollowedId != null ? "cfollowedId=" + cfollowedId + ", " : "") +
            (cfollowingId != null ? "cfollowingId=" + cfollowingId + ", " : "") +
            (cblockeduserId != null ? "cblockeduserId=" + cblockeduserId + ", " : "") +
            (cblockinguserId != null ? "cblockinguserId=" + cblockinguserId + ", " : "") +
            (appuserId != null ? "appuserId=" + appuserId + ", " : "") +
            (calbumId != null ? "calbumId=" + calbumId + ", " : "") +
            (cinterestId != null ? "cinterestId=" + cinterestId + ", " : "") +
            (cactivityId != null ? "cactivityId=" + cactivityId + ", " : "") +
            (ccelebId != null ? "ccelebId=" + ccelebId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
