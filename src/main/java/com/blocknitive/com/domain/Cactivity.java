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
 * A Cactivity.
 */
@Entity
@Table(name = "cactivity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cactivity")
public class Cactivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    @Column(name = "activity_name", length = 40, nullable = false)
    private String activityName;

    @ManyToMany
    @JoinTable(
        name = "rel_cactivity__community",
        joinColumns = @JoinColumn(name = "cactivity_id"),
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

    public Cactivity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public Cactivity activityName(String activityName) {
        this.setActivityName(activityName);
        return this;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Set<Community> getCommunities() {
        return this.communities;
    }

    public void setCommunities(Set<Community> communities) {
        this.communities = communities;
    }

    public Cactivity communities(Set<Community> communities) {
        this.setCommunities(communities);
        return this;
    }

    public Cactivity addCommunity(Community community) {
        this.communities.add(community);
        community.getCactivities().add(this);
        return this;
    }

    public Cactivity removeCommunity(Community community) {
        this.communities.remove(community);
        community.getCactivities().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cactivity)) {
            return false;
        }
        return id != null && id.equals(((Cactivity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cactivity{" +
            "id=" + getId() +
            ", activityName='" + getActivityName() + "'" +
            "}";
    }
}
