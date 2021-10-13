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
 * A Celeb.
 */
@Entity
@Table(name = "celeb")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "celeb")
public class Celeb implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    @Column(name = "celeb_name", length = 40, nullable = false)
    private String celebName;

    @ManyToMany
    @JoinTable(
        name = "rel_celeb__appuser",
        joinColumns = @JoinColumn(name = "celeb_id"),
        inverseJoinColumns = @JoinColumn(name = "appuser_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Appuser> appusers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Celeb id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCelebName() {
        return this.celebName;
    }

    public Celeb celebName(String celebName) {
        this.setCelebName(celebName);
        return this;
    }

    public void setCelebName(String celebName) {
        this.celebName = celebName;
    }

    public Set<Appuser> getAppusers() {
        return this.appusers;
    }

    public void setAppusers(Set<Appuser> appusers) {
        this.appusers = appusers;
    }

    public Celeb appusers(Set<Appuser> appusers) {
        this.setAppusers(appusers);
        return this;
    }

    public Celeb addAppuser(Appuser appuser) {
        this.appusers.add(appuser);
        appuser.getCelebs().add(this);
        return this;
    }

    public Celeb removeAppuser(Appuser appuser) {
        this.appusers.remove(appuser);
        appuser.getCelebs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Celeb)) {
            return false;
        }
        return id != null && id.equals(((Celeb) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Celeb{" +
            "id=" + getId() +
            ", celebName='" + getCelebName() + "'" +
            "}";
    }
}
