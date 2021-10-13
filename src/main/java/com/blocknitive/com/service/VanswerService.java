package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.VanswerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Vanswer}.
 */
public interface VanswerService {
    /**
     * Save a vanswer.
     *
     * @param vanswerDTO the entity to save.
     * @return the persisted entity.
     */
    VanswerDTO save(VanswerDTO vanswerDTO);

    /**
     * Partially updates a vanswer.
     *
     * @param vanswerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VanswerDTO> partialUpdate(VanswerDTO vanswerDTO);

    /**
     * Get all the vanswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VanswerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vanswer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VanswerDTO> findOne(Long id);

    /**
     * Delete the "id" vanswer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the vanswer corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VanswerDTO> search(String query, Pageable pageable);
}
