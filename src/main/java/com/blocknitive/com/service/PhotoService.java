package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.PhotoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Photo}.
 */
public interface PhotoService {
    /**
     * Save a photo.
     *
     * @param photoDTO the entity to save.
     * @return the persisted entity.
     */
    PhotoDTO save(PhotoDTO photoDTO);

    /**
     * Partially updates a photo.
     *
     * @param photoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PhotoDTO> partialUpdate(PhotoDTO photoDTO);

    /**
     * Get all the photos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PhotoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" photo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PhotoDTO> findOne(Long id);

    /**
     * Delete the "id" photo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the photo corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PhotoDTO> search(String query, Pageable pageable);
}
