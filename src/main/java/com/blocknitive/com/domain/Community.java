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
 * A Community.
 */
@Entity
@Table(name = "community")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "community")
public class Community implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "community_name", length = 100, nullable = false)
    private String communityName;

    @NotNull
    @Size(min = 2, max = 7500)
    @Column(name = "community_description", length = 7500, nullable = false)
    private String communityDescription;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "community")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "posts", "appuser", "community" }, allowSetters = true)
    private Set<Blog> blogs = new HashSet<>();

    @OneToMany(mappedBy = "csender")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "csender", "creceiver" }, allowSetters = true)
    private Set<Cmessage> csenders = new HashSet<>();

    @OneToMany(mappedBy = "creceiver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "csender", "creceiver" }, allowSetters = true)
    private Set<Cmessage> creceivers = new HashSet<>();

    @OneToMany(mappedBy = "cfollowed")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "followed", "following", "cfollowed", "cfollowing" }, allowSetters = true)
    private Set<Follow> cfolloweds = new HashSet<>();

    @OneToMany(mappedBy = "cfollowing")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "followed", "following", "cfollowed", "cfollowing" }, allowSetters = true)
    private Set<Follow> cfollowings = new HashSet<>();

    @OneToMany(mappedBy = "cblockeduser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "blockeduser", "blockinguser", "cblockeduser", "cblockinguser" }, allowSetters = true)
    private Set<Blockuser> cblockedusers = new HashSet<>();

    @OneToMany(mappedBy = "cblockinguser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "blockeduser", "blockinguser", "cblockeduser", "cblockinguser" }, allowSetters = true)
    private Set<Blockuser> cblockingusers = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "user",
            "appprofile",
            "appphoto",
            "communities",
            "blogs",
            "notifications",
            "albums",
            "comments",
            "posts",
            "senders",
            "receivers",
            "followeds",
            "followings",
            "blockedusers",
            "blockingusers",
            "vtopics",
            "vquestions",
            "vanswers",
            "vthumbs",
            "proposals",
            "proposalVotes",
            "interests",
            "activities",
            "celebs",
        },
        allowSetters = true
    )
    private Appuser appuser;

    @OneToMany(mappedBy = "community")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "photos", "community" }, allowSetters = true)
    private Set<Calbum> calbums = new HashSet<>();

    @ManyToMany(mappedBy = "communities")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "communities" }, allowSetters = true)
    private Set<Cinterest> cinterests = new HashSet<>();

    @ManyToMany(mappedBy = "communities")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "communities" }, allowSetters = true)
    private Set<Cactivity> cactivities = new HashSet<>();

    @ManyToMany(mappedBy = "communities")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "communities" }, allowSetters = true)
    private Set<Cceleb> ccelebs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Community id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Community creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCommunityName() {
        return this.communityName;
    }

    public Community communityName(String communityName) {
        this.setCommunityName(communityName);
        return this;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityDescription() {
        return this.communityDescription;
    }

    public Community communityDescription(String communityDescription) {
        this.setCommunityDescription(communityDescription);
        return this;
    }

    public void setCommunityDescription(String communityDescription) {
        this.communityDescription = communityDescription;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Community image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Community imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Community isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Blog> getBlogs() {
        return this.blogs;
    }

    public void setBlogs(Set<Blog> blogs) {
        if (this.blogs != null) {
            this.blogs.forEach(i -> i.setCommunity(null));
        }
        if (blogs != null) {
            blogs.forEach(i -> i.setCommunity(this));
        }
        this.blogs = blogs;
    }

    public Community blogs(Set<Blog> blogs) {
        this.setBlogs(blogs);
        return this;
    }

    public Community addBlog(Blog blog) {
        this.blogs.add(blog);
        blog.setCommunity(this);
        return this;
    }

    public Community removeBlog(Blog blog) {
        this.blogs.remove(blog);
        blog.setCommunity(null);
        return this;
    }

    public Set<Cmessage> getCsenders() {
        return this.csenders;
    }

    public void setCsenders(Set<Cmessage> cmessages) {
        if (this.csenders != null) {
            this.csenders.forEach(i -> i.setCsender(null));
        }
        if (cmessages != null) {
            cmessages.forEach(i -> i.setCsender(this));
        }
        this.csenders = cmessages;
    }

    public Community csenders(Set<Cmessage> cmessages) {
        this.setCsenders(cmessages);
        return this;
    }

    public Community addCsender(Cmessage cmessage) {
        this.csenders.add(cmessage);
        cmessage.setCsender(this);
        return this;
    }

    public Community removeCsender(Cmessage cmessage) {
        this.csenders.remove(cmessage);
        cmessage.setCsender(null);
        return this;
    }

    public Set<Cmessage> getCreceivers() {
        return this.creceivers;
    }

    public void setCreceivers(Set<Cmessage> cmessages) {
        if (this.creceivers != null) {
            this.creceivers.forEach(i -> i.setCreceiver(null));
        }
        if (cmessages != null) {
            cmessages.forEach(i -> i.setCreceiver(this));
        }
        this.creceivers = cmessages;
    }

    public Community creceivers(Set<Cmessage> cmessages) {
        this.setCreceivers(cmessages);
        return this;
    }

    public Community addCreceiver(Cmessage cmessage) {
        this.creceivers.add(cmessage);
        cmessage.setCreceiver(this);
        return this;
    }

    public Community removeCreceiver(Cmessage cmessage) {
        this.creceivers.remove(cmessage);
        cmessage.setCreceiver(null);
        return this;
    }

    public Set<Follow> getCfolloweds() {
        return this.cfolloweds;
    }

    public void setCfolloweds(Set<Follow> follows) {
        if (this.cfolloweds != null) {
            this.cfolloweds.forEach(i -> i.setCfollowed(null));
        }
        if (follows != null) {
            follows.forEach(i -> i.setCfollowed(this));
        }
        this.cfolloweds = follows;
    }

    public Community cfolloweds(Set<Follow> follows) {
        this.setCfolloweds(follows);
        return this;
    }

    public Community addCfollowed(Follow follow) {
        this.cfolloweds.add(follow);
        follow.setCfollowed(this);
        return this;
    }

    public Community removeCfollowed(Follow follow) {
        this.cfolloweds.remove(follow);
        follow.setCfollowed(null);
        return this;
    }

    public Set<Follow> getCfollowings() {
        return this.cfollowings;
    }

    public void setCfollowings(Set<Follow> follows) {
        if (this.cfollowings != null) {
            this.cfollowings.forEach(i -> i.setCfollowing(null));
        }
        if (follows != null) {
            follows.forEach(i -> i.setCfollowing(this));
        }
        this.cfollowings = follows;
    }

    public Community cfollowings(Set<Follow> follows) {
        this.setCfollowings(follows);
        return this;
    }

    public Community addCfollowing(Follow follow) {
        this.cfollowings.add(follow);
        follow.setCfollowing(this);
        return this;
    }

    public Community removeCfollowing(Follow follow) {
        this.cfollowings.remove(follow);
        follow.setCfollowing(null);
        return this;
    }

    public Set<Blockuser> getCblockedusers() {
        return this.cblockedusers;
    }

    public void setCblockedusers(Set<Blockuser> blockusers) {
        if (this.cblockedusers != null) {
            this.cblockedusers.forEach(i -> i.setCblockeduser(null));
        }
        if (blockusers != null) {
            blockusers.forEach(i -> i.setCblockeduser(this));
        }
        this.cblockedusers = blockusers;
    }

    public Community cblockedusers(Set<Blockuser> blockusers) {
        this.setCblockedusers(blockusers);
        return this;
    }

    public Community addCblockeduser(Blockuser blockuser) {
        this.cblockedusers.add(blockuser);
        blockuser.setCblockeduser(this);
        return this;
    }

    public Community removeCblockeduser(Blockuser blockuser) {
        this.cblockedusers.remove(blockuser);
        blockuser.setCblockeduser(null);
        return this;
    }

    public Set<Blockuser> getCblockingusers() {
        return this.cblockingusers;
    }

    public void setCblockingusers(Set<Blockuser> blockusers) {
        if (this.cblockingusers != null) {
            this.cblockingusers.forEach(i -> i.setCblockinguser(null));
        }
        if (blockusers != null) {
            blockusers.forEach(i -> i.setCblockinguser(this));
        }
        this.cblockingusers = blockusers;
    }

    public Community cblockingusers(Set<Blockuser> blockusers) {
        this.setCblockingusers(blockusers);
        return this;
    }

    public Community addCblockinguser(Blockuser blockuser) {
        this.cblockingusers.add(blockuser);
        blockuser.setCblockinguser(this);
        return this;
    }

    public Community removeCblockinguser(Blockuser blockuser) {
        this.cblockingusers.remove(blockuser);
        blockuser.setCblockinguser(null);
        return this;
    }

    public Appuser getAppuser() {
        return this.appuser;
    }

    public void setAppuser(Appuser appuser) {
        this.appuser = appuser;
    }

    public Community appuser(Appuser appuser) {
        this.setAppuser(appuser);
        return this;
    }

    public Set<Calbum> getCalbums() {
        return this.calbums;
    }

    public void setCalbums(Set<Calbum> calbums) {
        if (this.calbums != null) {
            this.calbums.forEach(i -> i.setCommunity(null));
        }
        if (calbums != null) {
            calbums.forEach(i -> i.setCommunity(this));
        }
        this.calbums = calbums;
    }

    public Community calbums(Set<Calbum> calbums) {
        this.setCalbums(calbums);
        return this;
    }

    public Community addCalbum(Calbum calbum) {
        this.calbums.add(calbum);
        calbum.setCommunity(this);
        return this;
    }

    public Community removeCalbum(Calbum calbum) {
        this.calbums.remove(calbum);
        calbum.setCommunity(null);
        return this;
    }

    public Set<Cinterest> getCinterests() {
        return this.cinterests;
    }

    public void setCinterests(Set<Cinterest> cinterests) {
        if (this.cinterests != null) {
            this.cinterests.forEach(i -> i.removeCommunity(this));
        }
        if (cinterests != null) {
            cinterests.forEach(i -> i.addCommunity(this));
        }
        this.cinterests = cinterests;
    }

    public Community cinterests(Set<Cinterest> cinterests) {
        this.setCinterests(cinterests);
        return this;
    }

    public Community addCinterest(Cinterest cinterest) {
        this.cinterests.add(cinterest);
        cinterest.getCommunities().add(this);
        return this;
    }

    public Community removeCinterest(Cinterest cinterest) {
        this.cinterests.remove(cinterest);
        cinterest.getCommunities().remove(this);
        return this;
    }

    public Set<Cactivity> getCactivities() {
        return this.cactivities;
    }

    public void setCactivities(Set<Cactivity> cactivities) {
        if (this.cactivities != null) {
            this.cactivities.forEach(i -> i.removeCommunity(this));
        }
        if (cactivities != null) {
            cactivities.forEach(i -> i.addCommunity(this));
        }
        this.cactivities = cactivities;
    }

    public Community cactivities(Set<Cactivity> cactivities) {
        this.setCactivities(cactivities);
        return this;
    }

    public Community addCactivity(Cactivity cactivity) {
        this.cactivities.add(cactivity);
        cactivity.getCommunities().add(this);
        return this;
    }

    public Community removeCactivity(Cactivity cactivity) {
        this.cactivities.remove(cactivity);
        cactivity.getCommunities().remove(this);
        return this;
    }

    public Set<Cceleb> getCcelebs() {
        return this.ccelebs;
    }

    public void setCcelebs(Set<Cceleb> ccelebs) {
        if (this.ccelebs != null) {
            this.ccelebs.forEach(i -> i.removeCommunity(this));
        }
        if (ccelebs != null) {
            ccelebs.forEach(i -> i.addCommunity(this));
        }
        this.ccelebs = ccelebs;
    }

    public Community ccelebs(Set<Cceleb> ccelebs) {
        this.setCcelebs(ccelebs);
        return this;
    }

    public Community addCceleb(Cceleb cceleb) {
        this.ccelebs.add(cceleb);
        cceleb.getCommunities().add(this);
        return this;
    }

    public Community removeCceleb(Cceleb cceleb) {
        this.ccelebs.remove(cceleb);
        cceleb.getCommunities().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Community)) {
            return false;
        }
        return id != null && id.equals(((Community) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Community{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", communityName='" + getCommunityName() + "'" +
            ", communityDescription='" + getCommunityDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
