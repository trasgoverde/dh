package com.blocknitive.com.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.blocknitive.com.domain.Newsletter} entity.
 */
public class NewsletterDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NewsletterDTO)) {
            return false;
        }

        NewsletterDTO newsletterDTO = (NewsletterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, newsletterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NewsletterDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
