package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.CinterestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Cinterest}.
 */
public interface CinterestService {
    /**
     * Save a cinterest.
     *
     * @param cinterestDTO the entity to save.
     * @return the persisted entity.
     */
    CinterestDTO save(CinterestDTO cinterestDTO);

    /**
     * Partially updates a cinterest.
     *
     * @param cinterestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CinterestDTO> partialUpdate(CinterestDTO cinterestDTO);

    /**
     * Get all the cinterests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CinterestDTO> findAll(Pageable pageable);

    /**
     * Get all the cinterests with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CinterestDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cinterest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CinterestDTO> findOne(Long id);

    /**
     * Delete the "id" cinterest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cinterest corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CinterestDTO> search(String query, Pageable pageable);
}
