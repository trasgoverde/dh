package com.blocknitive.com.service.dto;

import com.blocknitive.com.domain.enumeration.ProposalRole;
import com.blocknitive.com.domain.enumeration.ProposalType;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.blocknitive.com.domain.Proposal} entity.
 */
public class ProposalDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 250)
    private String proposalName;

    @NotNull
    private ProposalType proposalType;

    @NotNull
    private ProposalRole proposalRole;

    private Instant releaseDate;

    private Boolean isOpen;

    private Boolean isAccepted;

    private Boolean isPaid;

    private AppuserDTO appuser;

    private PostDTO post;

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

    public String getProposalName() {
        return proposalName;
    }

    public void setProposalName(String proposalName) {
        this.proposalName = proposalName;
    }

    public ProposalType getProposalType() {
        return proposalType;
    }

    public void setProposalType(ProposalType proposalType) {
        this.proposalType = proposalType;
    }

    public ProposalRole getProposalRole() {
        return proposalRole;
    }

    public void setProposalRole(ProposalRole proposalRole) {
        this.proposalRole = proposalRole;
    }

    public Instant getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public AppuserDTO getAppuser() {
        return appuser;
    }

    public void setAppuser(AppuserDTO appuser) {
        this.appuser = appuser;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProposalDTO)) {
            return false;
        }

        ProposalDTO proposalDTO = (ProposalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proposalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProposalDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", proposalName='" + getProposalName() + "'" +
            ", proposalType='" + getProposalType() + "'" +
            ", proposalRole='" + getProposalRole() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", isOpen='" + getIsOpen() + "'" +
            ", isAccepted='" + getIsAccepted() + "'" +
            ", isPaid='" + getIsPaid() + "'" +
            ", appuser=" + getAppuser() +
            ", post=" + getPost() +
            "}";
    }
}
