package com.blocknitive.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProposalVote.
 */
@Entity
@Table(name = "proposal_vote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "proposalvote")
public class ProposalVote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Column(name = "vote_points", nullable = false)
    private Long votePoints;

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
    @JsonIgnoreProperties(value = { "proposalVotes", "appuser", "post" }, allowSetters = true)
    private Proposal proposal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProposalVote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public ProposalVote creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Long getVotePoints() {
        return this.votePoints;
    }

    public ProposalVote votePoints(Long votePoints) {
        this.setVotePoints(votePoints);
        return this;
    }

    public void setVotePoints(Long votePoints) {
        this.votePoints = votePoints;
    }

    public Appuser getAppuser() {
        return this.appuser;
    }

    public void setAppuser(Appuser appuser) {
        this.appuser = appuser;
    }

    public ProposalVote appuser(Appuser appuser) {
        this.setAppuser(appuser);
        return this;
    }

    public Proposal getProposal() {
        return this.proposal;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public ProposalVote proposal(Proposal proposal) {
        this.setProposal(proposal);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProposalVote)) {
            return false;
        }
        return id != null && id.equals(((ProposalVote) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProposalVote{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", votePoints=" + getVotePoints() +
            "}";
    }
}
