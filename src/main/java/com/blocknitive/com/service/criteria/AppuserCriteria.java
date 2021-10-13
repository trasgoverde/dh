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
 * Criteria class for the {@link com.blocknitive.com.domain.Appuser} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.AppuserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /appusers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AppuserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private LongFilter assignedVotesPoints;

    private LongFilter userId;

    private LongFilter appprofileId;

    private LongFilter appphotoId;

    private LongFilter communityId;

    private LongFilter blogId;

    private LongFilter notificationId;

    private LongFilter albumId;

    private LongFilter commentId;

    private LongFilter postId;

    private LongFilter senderId;

    private LongFilter receiverId;

    private LongFilter followedId;

    private LongFilter followingId;

    private LongFilter blockeduserId;

    private LongFilter blockinguserId;

    private LongFilter vtopicId;

    private LongFilter vquestionId;

    private LongFilter vanswerId;

    private LongFilter vthumbId;

    private LongFilter proposalId;

    private LongFilter proposalVoteId;

    private LongFilter interestId;

    private LongFilter activityId;

    private LongFilter celebId;

    private Boolean distinct;

    public AppuserCriteria() {}

    public AppuserCriteria(AppuserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.assignedVotesPoints = other.assignedVotesPoints == null ? null : other.assignedVotesPoints.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.appprofileId = other.appprofileId == null ? null : other.appprofileId.copy();
        this.appphotoId = other.appphotoId == null ? null : other.appphotoId.copy();
        this.communityId = other.communityId == null ? null : other.communityId.copy();
        this.blogId = other.blogId == null ? null : other.blogId.copy();
        this.notificationId = other.notificationId == null ? null : other.notificationId.copy();
        this.albumId = other.albumId == null ? null : other.albumId.copy();
        this.commentId = other.commentId == null ? null : other.commentId.copy();
        this.postId = other.postId == null ? null : other.postId.copy();
        this.senderId = other.senderId == null ? null : other.senderId.copy();
        this.receiverId = other.receiverId == null ? null : other.receiverId.copy();
        this.followedId = other.followedId == null ? null : other.followedId.copy();
        this.followingId = other.followingId == null ? null : other.followingId.copy();
        this.blockeduserId = other.blockeduserId == null ? null : other.blockeduserId.copy();
        this.blockinguserId = other.blockinguserId == null ? null : other.blockinguserId.copy();
        this.vtopicId = other.vtopicId == null ? null : other.vtopicId.copy();
        this.vquestionId = other.vquestionId == null ? null : other.vquestionId.copy();
        this.vanswerId = other.vanswerId == null ? null : other.vanswerId.copy();
        this.vthumbId = other.vthumbId == null ? null : other.vthumbId.copy();
        this.proposalId = other.proposalId == null ? null : other.proposalId.copy();
        this.proposalVoteId = other.proposalVoteId == null ? null : other.proposalVoteId.copy();
        this.interestId = other.interestId == null ? null : other.interestId.copy();
        this.activityId = other.activityId == null ? null : other.activityId.copy();
        this.celebId = other.celebId == null ? null : other.celebId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AppuserCriteria copy() {
        return new AppuserCriteria(this);
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

    public LongFilter getAssignedVotesPoints() {
        return assignedVotesPoints;
    }

    public LongFilter assignedVotesPoints() {
        if (assignedVotesPoints == null) {
            assignedVotesPoints = new LongFilter();
        }
        return assignedVotesPoints;
    }

    public void setAssignedVotesPoints(LongFilter assignedVotesPoints) {
        this.assignedVotesPoints = assignedVotesPoints;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getAppprofileId() {
        return appprofileId;
    }

    public LongFilter appprofileId() {
        if (appprofileId == null) {
            appprofileId = new LongFilter();
        }
        return appprofileId;
    }

    public void setAppprofileId(LongFilter appprofileId) {
        this.appprofileId = appprofileId;
    }

    public LongFilter getAppphotoId() {
        return appphotoId;
    }

    public LongFilter appphotoId() {
        if (appphotoId == null) {
            appphotoId = new LongFilter();
        }
        return appphotoId;
    }

    public void setAppphotoId(LongFilter appphotoId) {
        this.appphotoId = appphotoId;
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

    public LongFilter getNotificationId() {
        return notificationId;
    }

    public LongFilter notificationId() {
        if (notificationId == null) {
            notificationId = new LongFilter();
        }
        return notificationId;
    }

    public void setNotificationId(LongFilter notificationId) {
        this.notificationId = notificationId;
    }

    public LongFilter getAlbumId() {
        return albumId;
    }

    public LongFilter albumId() {
        if (albumId == null) {
            albumId = new LongFilter();
        }
        return albumId;
    }

    public void setAlbumId(LongFilter albumId) {
        this.albumId = albumId;
    }

    public LongFilter getCommentId() {
        return commentId;
    }

    public LongFilter commentId() {
        if (commentId == null) {
            commentId = new LongFilter();
        }
        return commentId;
    }

    public void setCommentId(LongFilter commentId) {
        this.commentId = commentId;
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

    public LongFilter getFollowedId() {
        return followedId;
    }

    public LongFilter followedId() {
        if (followedId == null) {
            followedId = new LongFilter();
        }
        return followedId;
    }

    public void setFollowedId(LongFilter followedId) {
        this.followedId = followedId;
    }

    public LongFilter getFollowingId() {
        return followingId;
    }

    public LongFilter followingId() {
        if (followingId == null) {
            followingId = new LongFilter();
        }
        return followingId;
    }

    public void setFollowingId(LongFilter followingId) {
        this.followingId = followingId;
    }

    public LongFilter getBlockeduserId() {
        return blockeduserId;
    }

    public LongFilter blockeduserId() {
        if (blockeduserId == null) {
            blockeduserId = new LongFilter();
        }
        return blockeduserId;
    }

    public void setBlockeduserId(LongFilter blockeduserId) {
        this.blockeduserId = blockeduserId;
    }

    public LongFilter getBlockinguserId() {
        return blockinguserId;
    }

    public LongFilter blockinguserId() {
        if (blockinguserId == null) {
            blockinguserId = new LongFilter();
        }
        return blockinguserId;
    }

    public void setBlockinguserId(LongFilter blockinguserId) {
        this.blockinguserId = blockinguserId;
    }

    public LongFilter getVtopicId() {
        return vtopicId;
    }

    public LongFilter vtopicId() {
        if (vtopicId == null) {
            vtopicId = new LongFilter();
        }
        return vtopicId;
    }

    public void setVtopicId(LongFilter vtopicId) {
        this.vtopicId = vtopicId;
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

    public LongFilter getInterestId() {
        return interestId;
    }

    public LongFilter interestId() {
        if (interestId == null) {
            interestId = new LongFilter();
        }
        return interestId;
    }

    public void setInterestId(LongFilter interestId) {
        this.interestId = interestId;
    }

    public LongFilter getActivityId() {
        return activityId;
    }

    public LongFilter activityId() {
        if (activityId == null) {
            activityId = new LongFilter();
        }
        return activityId;
    }

    public void setActivityId(LongFilter activityId) {
        this.activityId = activityId;
    }

    public LongFilter getCelebId() {
        return celebId;
    }

    public LongFilter celebId() {
        if (celebId == null) {
            celebId = new LongFilter();
        }
        return celebId;
    }

    public void setCelebId(LongFilter celebId) {
        this.celebId = celebId;
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
        final AppuserCriteria that = (AppuserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(assignedVotesPoints, that.assignedVotesPoints) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(appprofileId, that.appprofileId) &&
            Objects.equals(appphotoId, that.appphotoId) &&
            Objects.equals(communityId, that.communityId) &&
            Objects.equals(blogId, that.blogId) &&
            Objects.equals(notificationId, that.notificationId) &&
            Objects.equals(albumId, that.albumId) &&
            Objects.equals(commentId, that.commentId) &&
            Objects.equals(postId, that.postId) &&
            Objects.equals(senderId, that.senderId) &&
            Objects.equals(receiverId, that.receiverId) &&
            Objects.equals(followedId, that.followedId) &&
            Objects.equals(followingId, that.followingId) &&
            Objects.equals(blockeduserId, that.blockeduserId) &&
            Objects.equals(blockinguserId, that.blockinguserId) &&
            Objects.equals(vtopicId, that.vtopicId) &&
            Objects.equals(vquestionId, that.vquestionId) &&
            Objects.equals(vanswerId, that.vanswerId) &&
            Objects.equals(vthumbId, that.vthumbId) &&
            Objects.equals(proposalId, that.proposalId) &&
            Objects.equals(proposalVoteId, that.proposalVoteId) &&
            Objects.equals(interestId, that.interestId) &&
            Objects.equals(activityId, that.activityId) &&
            Objects.equals(celebId, that.celebId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            creationDate,
            assignedVotesPoints,
            userId,
            appprofileId,
            appphotoId,
            communityId,
            blogId,
            notificationId,
            albumId,
            commentId,
            postId,
            senderId,
            receiverId,
            followedId,
            followingId,
            blockeduserId,
            blockinguserId,
            vtopicId,
            vquestionId,
            vanswerId,
            vthumbId,
            proposalId,
            proposalVoteId,
            interestId,
            activityId,
            celebId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppuserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (assignedVotesPoints != null ? "assignedVotesPoints=" + assignedVotesPoints + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (appprofileId != null ? "appprofileId=" + appprofileId + ", " : "") +
            (appphotoId != null ? "appphotoId=" + appphotoId + ", " : "") +
            (communityId != null ? "communityId=" + communityId + ", " : "") +
            (blogId != null ? "blogId=" + blogId + ", " : "") +
            (notificationId != null ? "notificationId=" + notificationId + ", " : "") +
            (albumId != null ? "albumId=" + albumId + ", " : "") +
            (commentId != null ? "commentId=" + commentId + ", " : "") +
            (postId != null ? "postId=" + postId + ", " : "") +
            (senderId != null ? "senderId=" + senderId + ", " : "") +
            (receiverId != null ? "receiverId=" + receiverId + ", " : "") +
            (followedId != null ? "followedId=" + followedId + ", " : "") +
            (followingId != null ? "followingId=" + followingId + ", " : "") +
            (blockeduserId != null ? "blockeduserId=" + blockeduserId + ", " : "") +
            (blockinguserId != null ? "blockinguserId=" + blockinguserId + ", " : "") +
            (vtopicId != null ? "vtopicId=" + vtopicId + ", " : "") +
            (vquestionId != null ? "vquestionId=" + vquestionId + ", " : "") +
            (vanswerId != null ? "vanswerId=" + vanswerId + ", " : "") +
            (vthumbId != null ? "vthumbId=" + vthumbId + ", " : "") +
            (proposalId != null ? "proposalId=" + proposalId + ", " : "") +
            (proposalVoteId != null ? "proposalVoteId=" + proposalVoteId + ", " : "") +
            (interestId != null ? "interestId=" + interestId + ", " : "") +
            (activityId != null ? "activityId=" + activityId + ", " : "") +
            (celebId != null ? "celebId=" + celebId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
