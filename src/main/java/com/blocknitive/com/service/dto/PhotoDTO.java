package com.blocknitive.com.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.blocknitive.com.domain.Photo} entity.
 */
public class PhotoDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @Lob
    private byte[] image;

    private String imageContentType;
    private AlbumDTO album;

    private CalbumDTO calbum;

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public AlbumDTO getAlbum() {
        return album;
    }

    public void setAlbum(AlbumDTO album) {
        this.album = album;
    }

    public CalbumDTO getCalbum() {
        return calbum;
    }

    public void setCalbum(CalbumDTO calbum) {
        this.calbum = calbum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhotoDTO)) {
            return false;
        }

        PhotoDTO photoDTO = (PhotoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, photoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhotoDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", image='" + getImage() + "'" +
            ", album=" + getAlbum() +
            ", calbum=" + getCalbum() +
            "}";
    }
}
