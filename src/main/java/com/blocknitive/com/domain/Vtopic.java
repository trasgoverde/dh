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
 * A Vtopic.
 */
@Entity
@Table(name = "vtopic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "vtopic")
public class Vtopic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "vtopic_title", length = 50, nullable = false)
    private String vtopicTitle;

    @Size(min = 2, max = 250)
    @Column(name = "vtopic_description", length = 250)
    private String vtopicDescription;

    @OneToMany(mappedBy = "vtopic")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vanswers", "vthumbs", "appuser", "vtopic" }, allowSetters = true)
    private Set<Vquestion> vquestions = new HashSet<>();

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vtopic id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Vtopic creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getVtopicTitle() {
        return this.vtopicTitle;
    }

    public Vtopic vtopicTitle(String vtopicTitle) {
        this.setVtopicTitle(vtopicTitle);
        return this;
    }

    public void setVtopicTitle(String vtopicTitle) {
        this.vtopicTitle = vtopicTitle;
    }

    public String getVtopicDescription() {
        return this.vtopicDescription;
    }

    public Vtopic vtopicDescription(String vtopicDescription) {
        this.setVtopicDescription(vtopicDescription);
        return this;
    }

    public void setVtopicDescription(String vtopicDescription) {
        this.vtopicDescription = vtopicDescription;
    }

    public Set<Vquestion> getVquestions() {
        return this.vquestions;
    }

    public void setVquestions(Set<Vquestion> vquestions) {
        if (this.vquestions != null) {
            this.vquestions.forEach(i -> i.setVtopic(null));
        }
        if (vquestions != null) {
            vquestions.forEach(i -> i.setVtopic(this));
        }
        this.vquestions = vquestions;
    }

    public Vtopic vquestions(Set<Vquestion> vquestions) {
        this.setVquestions(vquestions);
        return this;
    }

    public Vtopic addVquestion(Vquestion vquestion) {
        this.vquestions.add(vquestion);
        vquestion.setVtopic(this);
        return this;
    }

    public Vtopic removeVquestion(Vquestion vquestion) {
        this.vquestions.remove(vquestion);
        vquestion.setVtopic(null);
        return this;
    }

    public Appuser getAppuser() {
        return this.appuser;
    }

    public void setAppuser(Appuser appuser) {
        this.appuser = appuser;
    }

    public Vtopic appuser(Appuser appuser) {
        this.setAppuser(appuser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vtopic)) {
            return false;
        }
        return id != null && id.equals(((Vtopic) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vtopic{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", vtopicTitle='" + getVtopicTitle() + "'" +
            ", vtopicDescription='" + getVtopicDescription() + "'" +
            "}";
    }
}
