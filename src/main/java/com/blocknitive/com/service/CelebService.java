package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.CelebDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Celeb}.
 */
public interface CelebService {
    /**
     * Save a celeb.
     *
     * @param celebDTO the entity to save.
     * @return the persisted entity.
     */
    CelebDTO save(CelebDTO celebDTO);

    /**
     * Partially updates a celeb.
     *
     * @param celebDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CelebDTO> partialUpdate(CelebDTO celebDTO);

    /**
     * Get all the celebs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CelebDTO> findAll(Pageable pageable);

    /**
     * Get all the celebs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CelebDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" celeb.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CelebDTO> findOne(Long id);

    /**
     * Delete the "id" celeb.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the celeb corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CelebDTO> search(String query, Pageable pageable);
}
