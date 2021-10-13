package com.blocknitive.com.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Urllink.
 */
@Entity
@Table(name = "urllink")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "urllink")
public class Urllink implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "link_text", nullable = false)
    private String linkText;

    @NotNull
    @Column(name = "link_url", nullable = false)
    private String linkURL;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Urllink id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinkText() {
        return this.linkText;
    }

    public Urllink linkText(String linkText) {
        this.setLinkText(linkText);
        return this;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkURL() {
        return this.linkURL;
    }

    public Urllink linkURL(String linkURL) {
        this.setLinkURL(linkURL);
        return this;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Urllink)) {
            return false;
        }
        return id != null && id.equals(((Urllink) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Urllink{" +
            "id=" + getId() +
            ", linkText='" + getLinkText() + "'" +
            ", linkURL='" + getLinkURL() + "'" +
            "}";
    }
}
