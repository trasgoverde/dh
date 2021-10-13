package com.blocknitive.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Appuser.
 */
@Entity
@Table(name = "appuser")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "appuser")
public class Appuser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "assigned_votes_points")
    private Long assignedVotesPoints;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @JsonIgnoreProperties(value = { "appuser" }, allowSetters = true)
    @OneToOne(mappedBy = "appuser")
    private Appprofile appprofile;

    @JsonIgnoreProperties(value = { "appuser" }, allowSetters = true)
    @OneToOne(mappedBy = "appuser")
    private Appphoto appphoto;

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "blogs",
            "csenders",
            "creceivers",
            "cfolloweds",
            "cfollowings",
            "cblockedusers",
            "cblockingusers",
            "appuser",
            "calbums",
            "cinterests",
            "cactivities",
            "ccelebs",
        },
        allowSetters = true
    )
    private Set<Community> communities = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "posts", "appuser", "community" }, allowSetters = true)
    private Set<Blog> blogs = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appuser" }, allowSetters = true)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "photos", "appuser" }, allowSetters = true)
    private Set<Album> albums = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appuser", "post" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comments", "proposals", "appuser", "blog", "tags", "topics" }, allowSetters = true)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sender", "receiver" }, allowSetters = true)
    private Set<Message> senders = new HashSet<>();

    @OneToMany(mappedBy = "receiver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sender", "receiver" }, allowSetters = true)
    private Set<Message> receivers = new HashSet<>();

    @OneToMany(mappedBy = "followed")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "followed", "following", "cfollowed", "cfollowing" }, allowSetters = true)
    private Set<Follow> followeds = new HashSet<>();

    @OneToMany(mappedBy = "following")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "followed", "following", "cfollowed", "cfollowing" }, allowSetters = true)
    private Set<Follow> followings = new HashSet<>();

    @OneToMany(mappedBy = "blockeduser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "blockeduser", "blockinguser", "cblockeduser", "cblockinguser" }, allowSetters = true)
    private Set<Blockuser> blockedusers = new HashSet<>();

    @OneToMany(mappedBy = "blockinguser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "blockeduser", "blockinguser", "cblockeduser", "cblockinguser" }, allowSetters = true)
    private Set<Blockuser> blockingusers = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vquestions", "appuser" }, allowSetters = true)
    private Set<Vtopic> vtopics = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vanswers", "vthumbs", "appuser", "vtopic" }, allowSetters = true)
    private Set<Vquestion> vquestions = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vthumbs", "appuser", "vquestion" }, allowSetters = true)
    private Set<Vanswer> vanswers = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appuser", "vquestion", "vanswer" }, allowSetters = true)
    private Set<Vthumb> vthumbs = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "proposalVotes", "appuser", "post" }, allowSetters = true)
    private Set<Proposal> proposals = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appuser", "proposal" }, allowSetters = true)
    private Set<ProposalVote> proposalVotes = new HashSet<>();

    @ManyToMany(mappedBy = "appusers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appusers" }, allowSetters = true)
    private Set<Interest> interests = new HashSet<>();

    @ManyToMany(mappedBy = "appusers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appusers" }, allowSetters = true)
    private Set<Activity> activities = new HashSet<>();

    @ManyToMany(mappedBy = "appusers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appusers" }, allowSetters = true)
    private Set<Celeb> celebs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Appuser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Appuser creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Long getAssignedVotesPoints() {
        return this.assignedVotesPoints;
    }

    public Appuser assignedVotesPoints(Long assignedVotesPoints) {
        this.setAssignedVotesPoints(assignedVotesPoints);
        return this;
    }

    public void setAssignedVotesPoints(Long assignedVotesPoints) {
        this.assignedVotesPoints = assignedVotesPoints;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Appuser user(User user) {
        this.setUser(user);
        return this;
    }

    public Appprofile getAppprofile() {
        return this.appprofile;
    }

    public void setAppprofile(Appprofile appprofile) {
        if (this.appprofile != null) {
            this.appprofile.setAppuser(null);
        }
        if (appprofile != null) {
            appprofile.setAppuser(this);
        }
        this.appprofile = appprofile;
    }

    public Appuser appprofile(Appprofile appprofile) {
        this.setAppprofile(appprofile);
        return this;
    }

    public Appphoto getAppphoto() {
        return this.appphoto;
    }

    public void setAppphoto(Appphoto appphoto) {
        if (this.appphoto != null) {
            this.appphoto.setAppuser(null);
        }
        if (appphoto != null) {
            appphoto.setAppuser(this);
        }
        this.appphoto = appphoto;
    }

    public Appuser appphoto(Appphoto appphoto) {
        this.setAppphoto(appphoto);
        return this;
    }

    public Set<Community> getCommunities() {
        return this.communities;
    }

    public void setCommunities(Set<Community> communities) {
        if (this.communities != null) {
            this.communities.forEach(i -> i.setAppuser(null));
        }
        if (communities != null) {
            communities.forEach(i -> i.setAppuser(this));
        }
        this.communities = communities;
    }

    public Appuser communities(Set<Community> communities) {
        this.setCommunities(communities);
        return this;
    }

    public Appuser addCommunity(Community community) {
        this.communities.add(community);
        community.setAppuser(this);
        return this;
    }

    public Appuser removeCommunity(Community community) {
        this.communities.remove(community);
        community.setAppuser(null);
        return this;
    }

    public Set<Blog> getBlogs() {
        return this.blogs;
    }

    public void setBlogs(Set<Blog> blogs) {
        if (this.blogs != null) {
            this.blogs.forEach(i -> i.setAppuser(null));
        }
        if (blogs != null) {
            blogs.forEach(i -> i.setAppuser(this));
        }
        this.blogs = blogs;
    }

    public Appuser blogs(Set<Blog> blogs) {
        this.setBlogs(blogs);
        return this;
    }

    public Appuser addBlog(Blog blog) {
        this.blogs.add(blog);
        blog.setAppuser(this);
        return this;
    }

    public Appuser removeBlog(Blog blog) {
        this.blogs.remove(blog);
        blog.setAppuser(null);
        return this;
    }

    public Set<Notification> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        if (this.notifications != null) {
            this.notifications.forEach(i -> i.setAppuser(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setAppuser(this));
        }
        this.notifications = notifications;
    }

    public Appuser notifications(Set<Notification> notifications) {
        this.setNotifications(notifications);
        return this;
    }

    public Appuser addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setAppuser(this);
        return this;
    }

    public Appuser removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.setAppuser(null);
        return this;
    }

    public Set<Album> getAlbums() {
        return this.albums;
    }

    public void setAlbums(Set<Album> albums) {
        if (this.albums != null) {
            this.albums.forEach(i -> i.setAppuser(null));
        }
        if (albums != null) {
            albums.forEach(i -> i.setAppuser(this));
        }
        this.albums = albums;
    }

    public Appuser albums(Set<Album> albums) {
        this.setAlbums(albums);
        return this;
    }

    public Appuser addAlbum(Album album) {
        this.albums.add(album);
        album.setAppuser(this);
        return this;
    }

    public Appuser removeAlbum(Album album) {
        this.albums.remove(album);
        album.setAppuser(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setAppuser(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setAppuser(this));
        }
        this.comments = comments;
    }

    public Appuser comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Appuser addComment(Comment comment) {
        this.comments.add(comment);
        comment.setAppuser(this);
        return this;
    }

    public Appuser removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setAppuser(null);
        return this;
    }

    public Set<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(Set<Post> posts) {
        if (this.posts != null) {
            this.posts.forEach(i -> i.setAppuser(null));
        }
        if (posts != null) {
            posts.forEach(i -> i.setAppuser(this));
        }
        this.posts = posts;
    }

    public Appuser posts(Set<Post> posts) {
        this.setPosts(posts);
        return this;
    }

    public Appuser addPost(Post post) {
        this.posts.add(post);
        post.setAppuser(this);
        return this;
    }

    public Appuser removePost(Post post) {
        this.posts.remove(post);
        post.setAppuser(null);
        return this;
    }

    public Set<Message> getSenders() {
        return this.senders;
    }

    public void setSenders(Set<Message> messages) {
        if (this.senders != null) {
            this.senders.forEach(i -> i.setSender(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setSender(this));
        }
        this.senders = messages;
    }

    public Appuser senders(Set<Message> messages) {
        this.setSenders(messages);
        return this;
    }

    public Appuser addSender(Message message) {
        this.senders.add(message);
        message.setSender(this);
        return this;
    }

    public Appuser removeSender(Message message) {
        this.senders.remove(message);
        message.setSender(null);
        return this;
    }

    public Set<Message> getReceivers() {
        return this.receivers;
    }

    public void setReceivers(Set<Message> messages) {
        if (this.receivers != null) {
            this.receivers.forEach(i -> i.setReceiver(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setReceiver(this));
        }
        this.receivers = messages;
    }

    public Appuser receivers(Set<Message> messages) {
        this.setReceivers(messages);
        return this;
    }

    public Appuser addReceiver(Message message) {
        this.receivers.add(message);
        message.setReceiver(this);
        return this;
    }

    public Appuser removeReceiver(Message message) {
        this.receivers.remove(message);
        message.setReceiver(null);
        return this;
    }

    public Set<Follow> getFolloweds() {
        return this.followeds;
    }

    public void setFolloweds(Set<Follow> follows) {
        if (this.followeds != null) {
            this.followeds.forEach(i -> i.setFollowed(null));
        }
        if (follows != null) {
            follows.forEach(i -> i.setFollowed(this));
        }
        this.followeds = follows;
    }

    public Appuser followeds(Set<Follow> follows) {
        this.setFolloweds(follows);
        return this;
    }

    public Appuser addFollowed(Follow follow) {
        this.followeds.add(follow);
        follow.setFollowed(this);
        return this;
    }

    public Appuser removeFollowed(Follow follow) {
        this.followeds.remove(follow);
        follow.setFollowed(null);
        return this;
    }

    public Set<Follow> getFollowings() {
        return this.followings;
    }

    public void setFollowings(Set<Follow> follows) {
        if (this.followings != null) {
            this.followings.forEach(i -> i.setFollowing(null));
        }
        if (follows != null) {
            follows.forEach(i -> i.setFollowing(this));
        }
        this.followings = follows;
    }

    public Appuser followings(Set<Follow> follows) {
        this.setFollowings(follows);
        return this;
    }

    public Appuser addFollowing(Follow follow) {
        this.followings.add(follow);
        follow.setFollowing(this);
        return this;
    }

    public Appuser removeFollowing(Follow follow) {
        this.followings.remove(follow);
        follow.setFollowing(null);
        return this;
    }

    public Set<Blockuser> getBlockedusers() {
        return this.blockedusers;
    }

    public void setBlockedusers(Set<Blockuser> blockusers) {
        if (this.blockedusers != null) {
            this.blockedusers.forEach(i -> i.setBlockeduser(null));
        }
        if (blockusers != null) {
            blockusers.forEach(i -> i.setBlockeduser(this));
        }
        this.blockedusers = blockusers;
    }

    public Appuser blockedusers(Set<Blockuser> blockusers) {
        this.setBlockedusers(blockusers);
        return this;
    }

    public Appuser addBlockeduser(Blockuser blockuser) {
        this.blockedusers.add(blockuser);
        blockuser.setBlockeduser(this);
        return this;
    }

    public Appuser removeBlockeduser(Blockuser blockuser) {
        this.blockedusers.remove(blockuser);
        blockuser.setBlockeduser(null);
        return this;
    }

    public Set<Blockuser> getBlockingusers() {
        return this.blockingusers;
    }

    public void setBlockingusers(Set<Blockuser> blockusers) {
        if (this.blockingusers != null) {
            this.blockingusers.forEach(i -> i.setBlockinguser(null));
        }
        if (blockusers != null) {
            blockusers.forEach(i -> i.setBlockinguser(this));
        }
        this.blockingusers = blockusers;
    }

    public Appuser blockingusers(Set<Blockuser> blockusers) {
        this.setBlockingusers(blockusers);
        return this;
    }

    public Appuser addBlockinguser(Blockuser blockuser) {
        this.blockingusers.add(blockuser);
        blockuser.setBlockinguser(this);
        return this;
    }

    public Appuser removeBlockinguser(Blockuser blockuser) {
        this.blockingusers.remove(blockuser);
        blockuser.setBlockinguser(null);
        return this;
    }

    public Set<Vtopic> getVtopics() {
        return this.vtopics;
    }

    public void setVtopics(Set<Vtopic> vtopics) {
        if (this.vtopics != null) {
            this.vtopics.forEach(i -> i.setAppuser(null));
        }
        if (vtopics != null) {
            vtopics.forEach(i -> i.setAppuser(this));
        }
        this.vtopics = vtopics;
    }

    public Appuser vtopics(Set<Vtopic> vtopics) {
        this.setVtopics(vtopics);
        return this;
    }

    public Appuser addVtopic(Vtopic vtopic) {
        this.vtopics.add(vtopic);
        vtopic.setAppuser(this);
        return this;
    }

    public Appuser removeVtopic(Vtopic vtopic) {
        this.vtopics.remove(vtopic);
        vtopic.setAppuser(null);
        return this;
    }

    public Set<Vquestion> getVquestions() {
        return this.vquestions;
    }

    public void setVquestions(Set<Vquestion> vquestions) {
        if (this.vquestions != null) {
            this.vquestions.forEach(i -> i.setAppuser(null));
        }
        if (vquestions != null) {
            vquestions.forEach(i -> i.setAppuser(this));
        }
        this.vquestions = vquestions;
    }

    public Appuser vquestions(Set<Vquestion> vquestions) {
        this.setVquestions(vquestions);
        return this;
    }

    public Appuser addVquestion(Vquestion vquestion) {
        this.vquestions.add(vquestion);
        vquestion.setAppuser(this);
        return this;
    }

    public Appuser removeVquestion(Vquestion vquestion) {
        this.vquestions.remove(vquestion);
        vquestion.setAppuser(null);
        return this;
    }

    public Set<Vanswer> getVanswers() {
        return this.vanswers;
    }

    public void setVanswers(Set<Vanswer> vanswers) {
        if (this.vanswers != null) {
            this.vanswers.forEach(i -> i.setAppuser(null));
        }
        if (vanswers != null) {
            vanswers.forEach(i -> i.setAppuser(this));
        }
        this.vanswers = vanswers;
    }

    public Appuser vanswers(Set<Vanswer> vanswers) {
        this.setVanswers(vanswers);
        return this;
    }

    public Appuser addVanswer(Vanswer vanswer) {
        this.vanswers.add(vanswer);
        vanswer.setAppuser(this);
        return this;
    }

    public Appuser removeVanswer(Vanswer vanswer) {
        this.vanswers.remove(vanswer);
        vanswer.setAppuser(null);
        return this;
    }

    public Set<Vthumb> getVthumbs() {
        return this.vthumbs;
    }

    public void setVthumbs(Set<Vthumb> vthumbs) {
        if (this.vthumbs != null) {
            this.vthumbs.forEach(i -> i.setAppuser(null));
        }
        if (vthumbs != null) {
            vthumbs.forEach(i -> i.setAppuser(this));
        }
        this.vthumbs = vthumbs;
    }

    public Appuser vthumbs(Set<Vthumb> vthumbs) {
        this.setVthumbs(vthumbs);
        return this;
    }

    public Appuser addVthumb(Vthumb vthumb) {
        this.vthumbs.add(vthumb);
        vthumb.setAppuser(this);
        return this;
    }

    public Appuser removeVthumb(Vthumb vthumb) {
        this.vthumbs.remove(vthumb);
        vthumb.setAppuser(null);
        return this;
    }

    public Set<Proposal> getProposals() {
        return this.proposals;
    }

    public void setProposals(Set<Proposal> proposals) {
        if (this.proposals != null) {
            this.proposals.forEach(i -> i.setAppuser(null));
        }
        if (proposals != null) {
            proposals.forEach(i -> i.setAppuser(this));
        }
        this.proposals = proposals;
    }

    public Appuser proposals(Set<Proposal> proposals) {
        this.setProposals(proposals);
        return this;
    }

    public Appuser addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setAppuser(this);
        return this;
    }

    public Appuser removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.setAppuser(null);
        return this;
    }

    public Set<ProposalVote> getProposalVotes() {
        return this.proposalVotes;
    }

    public void setProposalVotes(Set<ProposalVote> proposalVotes) {
        if (this.proposalVotes != null) {
            this.proposalVotes.forEach(i -> i.setAppuser(null));
        }
        if (proposalVotes != null) {
            proposalVotes.forEach(i -> i.setAppuser(this));
        }
        this.proposalVotes = proposalVotes;
    }

    public Appuser proposalVotes(Set<ProposalVote> proposalVotes) {
        this.setProposalVotes(proposalVotes);
        return this;
    }

    public Appuser addProposalVote(ProposalVote proposalVote) {
        this.proposalVotes.add(proposalVote);
        proposalVote.setAppuser(this);
        return this;
    }

    public Appuser removeProposalVote(ProposalVote proposalVote) {
        this.proposalVotes.remove(proposalVote);
        proposalVote.setAppuser(null);
        return this;
    }

    public Set<Interest> getInterests() {
        return this.interests;
    }

    public void setInterests(Set<Interest> interests) {
        if (this.interests != null) {
            this.interests.forEach(i -> i.removeAppuser(this));
        }
        if (interests != null) {
            interests.forEach(i -> i.addAppuser(this));
        }
        this.interests = interests;
    }

    public Appuser interests(Set<Interest> interests) {
        this.setInterests(interests);
        return this;
    }

    public Appuser addInterest(Interest interest) {
        this.interests.add(interest);
        interest.getAppusers().add(this);
        return this;
    }

    public Appuser removeInterest(Interest interest) {
        this.interests.remove(interest);
        interest.getAppusers().remove(this);
        return this;
    }

    public Set<Activity> getActivities() {
        return this.activities;
    }

    public void setActivities(Set<Activity> activities) {
        if (this.activities != null) {
            this.activities.forEach(i -> i.removeAppuser(this));
        }
        if (activities != null) {
            activities.forEach(i -> i.addAppuser(this));
        }
        this.activities = activities;
    }

    public Appuser activities(Set<Activity> activities) {
        this.setActivities(activities);
        return this;
    }

    public Appuser addActivity(Activity activity) {
        this.activities.add(activity);
        activity.getAppusers().add(this);
        return this;
    }

    public Appuser removeActivity(Activity activity) {
        this.activities.remove(activity);
        activity.getAppusers().remove(this);
        return this;
    }

    public Set<Celeb> getCelebs() {
        return this.celebs;
    }

    public void setCelebs(Set<Celeb> celebs) {
        if (this.celebs != null) {
            this.celebs.forEach(i -> i.removeAppuser(this));
        }
        if (celebs != null) {
            celebs.forEach(i -> i.addAppuser(this));
        }
        this.celebs = celebs;
    }

    public Appuser celebs(Set<Celeb> celebs) {
        this.setCelebs(celebs);
        return this;
    }

    public Appuser addCeleb(Celeb celeb) {
        this.celebs.add(celeb);
        celeb.getAppusers().add(this);
        return this;
    }

    public Appuser removeCeleb(Celeb celeb) {
        this.celebs.remove(celeb);
        celeb.getAppusers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appuser)) {
            return false;
        }
        return id != null && id.equals(((Appuser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appuser{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", assignedVotesPoints=" + getAssignedVotesPoints() +
            "}";
    }
}
