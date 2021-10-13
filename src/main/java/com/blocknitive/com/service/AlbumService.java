package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.AlbumDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Album}.
 */
public interface AlbumService {
    /**
     * Save a album.
     *
     * @param albumDTO the entity to save.
     * @return the persisted entity.
     */
    AlbumDTO save(AlbumDTO albumDTO);

    /**
     * Partially updates a album.
     *
     * @param albumDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlbumDTO> partialUpdate(AlbumDTO albumDTO);

    /**
     * Get all the albums.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlbumDTO> findAll(Pageable pageable);

    /**
     * Get the "id" album.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlbumDTO> findOne(Long id);

    /**
     * Delete the "id" album.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the album corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlbumDTO> search(String query, Pageable pageable);
}
