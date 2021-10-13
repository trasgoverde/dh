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
 * A Vquestion.
 */
@Entity
@Table(name = "vquestion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "vquestion")
public class Vquestion implements Serializable {

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
    @Column(name = "vquestion", length = 100, nullable = false)
    private String vquestion;

    @Size(min = 2, max = 250)
    @Column(name = "vquestion_description", length = 250)
    private String vquestionDescription;

    @OneToMany(mappedBy = "vquestion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vthumbs", "appuser", "vquestion" }, allowSetters = true)
    private Set<Vanswer> vanswers = new HashSet<>();

    @OneToMany(mappedBy = "vquestion")
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
    @JsonIgnoreProperties(value = { "vquestions", "appuser" }, allowSetters = true)
    private Vtopic vtopic;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vquestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Vquestion creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getVquestion() {
        return this.vquestion;
    }

    public Vquestion vquestion(String vquestion) {
        this.setVquestion(vquestion);
        return this;
    }

    public void setVquestion(String vquestion) {
        this.vquestion = vquestion;
    }

    public String getVquestionDescription() {
        return this.vquestionDescription;
    }

    public Vquestion vquestionDescription(String vquestionDescription) {
        this.setVquestionDescription(vquestionDescription);
        return this;
    }

    public void setVquestionDescription(String vquestionDescription) {
        this.vquestionDescription = vquestionDescription;
    }

    public Set<Vanswer> getVanswers() {
        return this.vanswers;
    }

    public void setVanswers(Set<Vanswer> vanswers) {
        if (this.vanswers != null) {
            this.vanswers.forEach(i -> i.setVquestion(null));
        }
        if (vanswers != null) {
            vanswers.forEach(i -> i.setVquestion(this));
        }
        this.vanswers = vanswers;
    }

    public Vquestion vanswers(Set<Vanswer> vanswers) {
        this.setVanswers(vanswers);
        return this;
    }

    public Vquestion addVanswer(Vanswer vanswer) {
        this.vanswers.add(vanswer);
        vanswer.setVquestion(this);
        return this;
    }

    public Vquestion removeVanswer(Vanswer vanswer) {
        this.vanswers.remove(vanswer);
        vanswer.setVquestion(null);
        return this;
    }

    public Set<Vthumb> getVthumbs() {
        return this.vthumbs;
    }

    public void setVthumbs(Set<Vthumb> vthumbs) {
        if (this.vthumbs != null) {
            this.vthumbs.forEach(i -> i.setVquestion(null));
        }
        if (vthumbs != null) {
            vthumbs.forEach(i -> i.setVquestion(this));
        }
        this.vthumbs = vthumbs;
    }

    public Vquestion vthumbs(Set<Vthumb> vthumbs) {
        this.setVthumbs(vthumbs);
        return this;
    }

    public Vquestion addVthumb(Vthumb vthumb) {
        this.vthumbs.add(vthumb);
        vthumb.setVquestion(this);
        return this;
    }

    public Vquestion removeVthumb(Vthumb vthumb) {
        this.vthumbs.remove(vthumb);
        vthumb.setVquestion(null);
        return this;
    }

    public Appuser getAppuser() {
        return this.appuser;
    }

    public void setAppuser(Appuser appuser) {
        this.appuser = appuser;
    }

    public Vquestion appuser(Appuser appuser) {
        this.setAppuser(appuser);
        return this;
    }

    public Vtopic getVtopic() {
        return this.vtopic;
    }

    public void setVtopic(Vtopic vtopic) {
        this.vtopic = vtopic;
    }

    public Vquestion vtopic(Vtopic vtopic) {
        this.setVtopic(vtopic);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vquestion)) {
            return false;
        }
        return id != null && id.equals(((Vquestion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vquestion{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", vquestion='" + getVquestion() + "'" +
            ", vquestionDescription='" + getVquestionDescription() + "'" +
            "}";
    }
}
