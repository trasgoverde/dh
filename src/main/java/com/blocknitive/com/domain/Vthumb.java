package com.blocknitive.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vthumb.
 */
@Entity
@Table(name = "vthumb")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "vthumb")
public class Vthumb implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "vthumb_up")
    private Boolean vthumbUp;

    @Column(name = "vthumb_down")
    private Boolean vthumbDown;

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "vanswers", "vthumbs", "appuser", "vtopic" }, allowSetters = true)
    private Vquestion vquestion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "vthumbs", "appuser", "vquestion" }, allowSetters = true)
    private Vanswer vanswer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vthumb id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Vthumb creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getVthumbUp() {
        return this.vthumbUp;
    }

    public Vthumb vthumbUp(Boolean vthumbUp) {
        this.setVthumbUp(vthumbUp);
        return this;
    }

    public void setVthumbUp(Boolean vthumbUp) {
        this.vthumbUp = vthumbUp;
    }

    public Boolean getVthumbDown() {
        return this.vthumbDown;
    }

    public Vthumb vthumbDown(Boolean vthumbDown) {
        this.setVthumbDown(vthumbDown);
        return this;
    }

    public void setVthumbDown(Boolean vthumbDown) {
        this.vthumbDown = vthumbDown;
    }

    public Appuser getAppuser() {
        return this.appuser;
    }

    public void setAppuser(Appuser appuser) {
        this.appuser = appuser;
    }

    public Vthumb appuser(Appuser appuser) {
        this.setAppuser(appuser);
        return this;
    }

    public Vquestion getVquestion() {
        return this.vquestion;
    }

    public void setVquestion(Vquestion vquestion) {
        this.vquestion = vquestion;
    }

    public Vthumb vquestion(Vquestion vquestion) {
        this.setVquestion(vquestion);
        return this;
    }

    public Vanswer getVanswer() {
        return this.vanswer;
    }

    public void setVanswer(Vanswer vanswer) {
        this.vanswer = vanswer;
    }

    public Vthumb vanswer(Vanswer vanswer) {
        this.setVanswer(vanswer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vthumb)) {
            return false;
        }
        return id != null && id.equals(((Vthumb) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vthumb{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", vthumbUp='" + getVthumbUp() + "'" +
            ", vthumbDown='" + getVthumbDown() + "'" +
            "}";
    }
}
