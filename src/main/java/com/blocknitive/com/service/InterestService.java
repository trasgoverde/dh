package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.InterestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Interest}.
 */
public interface InterestService {
    /**
     * Save a interest.
     *
     * @param interestDTO the entity to save.
     * @return the persisted entity.
     */
    InterestDTO save(InterestDTO interestDTO);

    /**
     * Partially updates a interest.
     *
     * @param interestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InterestDTO> partialUpdate(InterestDTO interestDTO);

    /**
     * Get all the interests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InterestDTO> findAll(Pageable pageable);

    /**
     * Get all the interests with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InterestDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" interest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InterestDTO> findOne(Long id);

    /**
     * Delete the "id" interest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the interest corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InterestDTO> search(String query, Pageable pageable);
}
