package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.VtopicDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Vtopic}.
 */
public interface VtopicService {
    /**
     * Save a vtopic.
     *
     * @param vtopicDTO the entity to save.
     * @return the persisted entity.
     */
    VtopicDTO save(VtopicDTO vtopicDTO);

    /**
     * Partially updates a vtopic.
     *
     * @param vtopicDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VtopicDTO> partialUpdate(VtopicDTO vtopicDTO);

    /**
     * Get all the vtopics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VtopicDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vtopic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VtopicDTO> findOne(Long id);

    /**
     * Delete the "id" vtopic.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the vtopic corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VtopicDTO> search(String query, Pageable pageable);
}
