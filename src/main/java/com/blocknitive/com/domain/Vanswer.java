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
 * A Vanswer.
 */
@Entity
@Table(name = "vanswer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "vanswer")
public class Vanswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 500)
    @Column(name = "url_vanswer", length = 500, nullable = false)
    private String urlVanswer;

    @Column(name = "accepted")
    private Boolean accepted;

    @OneToMany(mappedBy = "vanswer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appuser", "vquestion", "vanswer" }, allowSetters = true)
    private Set<Vthumb> vthumbs = new HashSet<>();

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "vanswers", "vthumbs", "appuser", "vtopic" }, allowSetters = true)
    private Vquestion vquestion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vanswer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Vanswer creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getUrlVanswer() {
        return this.urlVanswer;
    }

    public Vanswer urlVanswer(String urlVanswer) {
        this.setUrlVanswer(urlVanswer);
        return this;
    }

    public void setUrlVanswer(String urlVanswer) {
        this.urlVanswer = urlVanswer;
    }

    public Boolean getAccepted() {
        return this.accepted;
    }

    public Vanswer accepted(Boolean accepted) {
        this.setAccepted(accepted);
        return this;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Set<Vthumb> getVthumbs() {
        return this.vthumbs;
    }

    public void setVthumbs(Set<Vthumb> vthumbs) {
        if (this.vthumbs != null) {
            this.vthumbs.forEach(i -> i.setVanswer(null));
        }
        if (vthumbs != null) {
            vthumbs.forEach(i -> i.setVanswer(this));
        }
        this.vthumbs = vthumbs;
    }

    public Vanswer vthumbs(Set<Vthumb> vthumbs) {
        this.setVthumbs(vthumbs);
        return this;
    }

    public Vanswer addVthumb(Vthumb vthumb) {
        this.vthumbs.add(vthumb);
        vthumb.setVanswer(this);
        return this;
    }

    public Vanswer removeVthumb(Vthumb vthumb) {
        this.vthumbs.remove(vthumb);
        vthumb.setVanswer(null);
        return this;
    }

    public Appuser getAppuser() {
        return this.appuser;
    }

    public void setAppuser(Appuser appuser) {
        this.appuser = appuser;
    }

    public Vanswer appuser(Appuser appuser) {
        this.setAppuser(appuser);
        return this;
    }

    public Vquestion getVquestion() {
        return this.vquestion;
    }

    public void setVquestion(Vquestion vquestion) {
        this.vquestion = vquestion;
    }

    public Vanswer vquestion(Vquestion vquestion) {
        this.setVquestion(vquestion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vanswer)) {
            return false;
        }
        return id != null && id.equals(((Vanswer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vanswer{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", urlVanswer='" + getUrlVanswer() + "'" +
            ", accepted='" + getAccepted() + "'" +
            "}";
    }
}
