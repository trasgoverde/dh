package com.blocknitive.com.domain;

import com.blocknitive.com.domain.enumeration.ProposalRole;
import com.blocknitive.com.domain.enumeration.ProposalType;
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
 * A Proposal.
 */
@Entity
@Table(name = "proposal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "proposal")
public class Proposal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 250)
    @Column(name = "proposal_name", length = 250, nullable = false)
    private String proposalName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "proposal_type", nullable = false)
    private ProposalType proposalType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "proposal_role", nullable = false)
    private ProposalRole proposalRole;

    @Column(name = "release_date")
    private Instant releaseDate;

    @Column(name = "is_open")
    private Boolean isOpen;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @OneToMany(mappedBy = "proposal")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appuser", "proposal" }, allowSetters = true)
    private Set<ProposalVote> proposalVotes = new HashSet<>();

    @ManyToOne
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

    @ManyToOne
    @JsonIgnoreProperties(value = { "comments", "proposals", "appuser", "blog", "tags", "topics" }, allowSetters = true)
    private Post post;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Proposal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Proposal creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getProposalName() {
        return this.proposalName;
    }

    public Proposal proposalName(String proposalName) {
        this.setProposalName(proposalName);
        return this;
    }

    public void setProposalName(String proposalName) {
        this.proposalName = proposalName;
    }

    public ProposalType getProposalType() {
        return this.proposalType;
    }

    public Proposal proposalType(ProposalType proposalType) {
        this.setProposalType(proposalType);
        return this;
    }

    public void setProposalType(ProposalType proposalType) {
        this.proposalType = proposalType;
    }

    public ProposalRole getProposalRole() {
        return this.proposalRole;
    }

    public Proposal proposalRole(ProposalRole proposalRole) {
        this.setProposalRole(proposalRole);
        return this;
    }

    public void setProposalRole(ProposalRole proposalRole) {
        this.proposalRole = proposalRole;
    }

    public Instant getReleaseDate() {
        return this.releaseDate;
    }

    public Proposal releaseDate(Instant releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean getIsOpen() {
        return this.isOpen;
    }

    public Proposal isOpen(Boolean isOpen) {
        this.setIsOpen(isOpen);
        return this;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Boolean getIsAccepted() {
        return this.isAccepted;
    }

    public Proposal isAccepted(Boolean isAccepted) {
        this.setIsAccepted(isAccepted);
        return this;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public Boolean getIsPaid() {
        return this.isPaid;
    }

    public Proposal isPaid(Boolean isPaid) {
        this.setIsPaid(isPaid);
        return this;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Set<ProposalVote> getProposalVotes() {
        return this.proposalVotes;
    }

    public void setProposalVotes(Set<ProposalVote> proposalVotes) {
        if (this.proposalVotes != null) {
            this.proposalVotes.forEach(i -> i.setProposal(null));
        }
        if (proposalVotes != null) {
            proposalVotes.forEach(i -> i.setProposal(this));
        }
        this.proposalVotes = proposalVotes;
    }

    public Proposal proposalVotes(Set<ProposalVote> proposalVotes) {
        this.setProposalVotes(proposalVotes);
        return this;
    }

    public Proposal addProposalVote(ProposalVote proposalVote) {
        this.proposalVotes.add(proposalVote);
        proposalVote.setProposal(this);
        return this;
    }

    public Proposal removeProposalVote(ProposalVote proposalVote) {
        this.proposalVotes.remove(proposalVote);
        proposalVote.setProposal(null);
        return this;
    }

    public Appuser getAppuser() {
        return this.appuser;
    }

    public void setAppuser(Appuser appuser) {
        this.appuser = appuser;
    }

    public Proposal appuser(Appuser appuser) {
        this.setAppuser(appuser);
        return this;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Proposal post(Post post) {
        this.setPost(post);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proposal)) {
            return false;
        }
        return id != null && id.equals(((Proposal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proposal{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", proposalName='" + getProposalName() + "'" +
            ", proposalType='" + getProposalType() + "'" +
            ", proposalRole='" + getProposalRole() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", isOpen='" + getIsOpen() + "'" +
            ", isAccepted='" + getIsAccepted() + "'" +
            ", isPaid='" + getIsPaid() + "'" +
            "}";
    }
}
