package com.blocknitive.com.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.blocknitive.com.domain.Photo} entity. This class is used
 * in {@link com.blocknitive.com.web.rest.PhotoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /photos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PhotoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private LongFilter albumId;

    private LongFilter calbumId;

    private Boolean distinct;

    public PhotoCriteria() {}

    public PhotoCriteria(PhotoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.albumId = other.albumId == null ? null : other.albumId.copy();
        this.calbumId = other.calbumId == null ? null : other.calbumId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PhotoCriteria copy() {
        return new PhotoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public InstantFilter creationDate() {
        if (creationDate == null) {
            creationDate = new InstantFilter();
        }
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public LongFilter getAlbumId() {
        return albumId;
    }

    public LongFilter albumId() {
        if (albumId == null) {
            albumId = new LongFilter();
        }
        return albumId;
    }

    public void setAlbumId(LongFilter albumId) {
        this.albumId = albumId;
    }

    public LongFilter getCalbumId() {
        return calbumId;
    }

    public LongFilter calbumId() {
        if (calbumId == null) {
            calbumId = new LongFilter();
        }
        return calbumId;
    }

    public void setCalbumId(LongFilter calbumId) {
        this.calbumId = calbumId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PhotoCriteria that = (PhotoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(albumId, that.albumId) &&
            Objects.equals(calbumId, that.calbumId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, albumId, calbumId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhotoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (albumId != null ? "albumId=" + albumId + ", " : "") +
            (calbumId != null ? "calbumId=" + calbumId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
