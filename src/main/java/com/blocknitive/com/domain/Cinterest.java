package com.blocknitive.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cinterest.
 */
@Entity
@Table(name = "cinterest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cinterest")
public class Cinterest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    @Column(name = "interest_name", length = 40, nullable = false)
    private String interestName;

    @ManyToMany
    @JoinTable(
        name = "rel_cinterest__community",
        joinColumns = @JoinColumn(name = "cinterest_id"),
        inverseJoinColumns = @JoinColumn(name = "community_id")
    )
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cinterest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInterestName() {
        return this.interestName;
    }

    public Cinterest interestName(String interestName) {
        this.setInterestName(interestName);
        return this;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public Set<Community> getCommunities() {
        return this.communities;
    }

    public void setCommunities(Set<Community> communities) {
        this.communities = communities;
    }

    public Cinterest communities(Set<Community> communities) {
        this.setCommunities(communities);
        return this;
    }

    public Cinterest addCommunity(Community community) {
        this.communities.add(community);
        community.getCinterests().add(this);
        return this;
    }

    public Cinterest removeCommunity(Community community) {
        this.communities.remove(community);
        community.getCinterests().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cinterest)) {
            return false;
        }
        return id != null && id.equals(((Cinterest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cinterest{" +
            "id=" + getId() +
            ", interestName='" + getInterestName() + "'" +
            "}";
    }
}
