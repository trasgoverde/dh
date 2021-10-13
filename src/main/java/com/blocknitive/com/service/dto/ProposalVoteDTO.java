package com.blocknitive.com.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.blocknitive.com.domain.ProposalVote} entity.
 */
public class ProposalVoteDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    private Long votePoints;

    private AppuserDTO appuser;

    private ProposalDTO proposal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Long getVotePoints() {
        return votePoints;
    }

    public void setVotePoints(Long votePoints) {
        this.votePoints = votePoints;
    }

    public AppuserDTO getAppuser() {
        return appuser;
    }

    public void setAppuser(AppuserDTO appuser) {
        this.appuser = appuser;
    }

    public ProposalDTO getProposal() {
        return proposal;
    }

    public void setProposal(ProposalDTO proposal) {
        this.proposal = proposal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProposalVoteDTO)) {
            return false;
        }

        ProposalVoteDTO proposalVoteDTO = (ProposalVoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proposalVoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProposalVoteDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", votePoints=" + getVotePoints() +
            ", appuser=" + getAppuser() +
            ", proposal=" + getProposal() +
            "}";
    }
}
