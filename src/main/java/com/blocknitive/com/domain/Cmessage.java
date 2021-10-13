package com.blocknitive.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cmessage.
 */
@Entity
@Table(name = "cmessage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cmessage")
public class Cmessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 8000)
    @Column(name = "message_text", length = 8000, nullable = false)
    private String messageText;

    @Column(name = "is_delivered")
    private Boolean isDelivered;

    @ManyToOne
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
    private Community csender;

    @ManyToOne
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
    private Community creceiver;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cmessage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Cmessage creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getMessageText() {
        return this.messageText;
    }

    public Cmessage messageText(String messageText) {
        this.setMessageText(messageText);
        return this;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Boolean getIsDelivered() {
        return this.isDelivered;
    }

    public Cmessage isDelivered(Boolean isDelivered) {
        this.setIsDelivered(isDelivered);
        return this;
    }

    public void setIsDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public Community getCsender() {
        return this.csender;
    }

    public void setCsender(Community community) {
        this.csender = community;
    }

    public Cmessage csender(Community community) {
        this.setCsender(community);
        return this;
    }

    public Community getCreceiver() {
        return this.creceiver;
    }

    public void setCreceiver(Community community) {
        this.creceiver = community;
    }

    public Cmessage creceiver(Community community) {
        this.setCreceiver(community);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cmessage)) {
            return false;
        }
        return id != null && id.equals(((Cmessage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cmessage{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", messageText='" + getMessageText() + "'" +
            ", isDelivered='" + getIsDelivered() + "'" +
            "}";
    }
}
