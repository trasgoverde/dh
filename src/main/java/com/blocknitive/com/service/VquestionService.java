package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.VquestionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Vquestion}.
 */
public interface VquestionService {
    /**
     * Save a vquestion.
     *
     * @param vquestionDTO the entity to save.
     * @return the persisted entity.
     */
    VquestionDTO save(VquestionDTO vquestionDTO);

    /**
     * Partially updates a vquestion.
     *
     * @param vquestionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VquestionDTO> partialUpdate(VquestionDTO vquestionDTO);

    /**
     * Get all the vquestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VquestionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vquestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VquestionDTO> findOne(Long id);

    /**
     * Delete the "id" vquestion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the vquestion corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VquestionDTO> search(String query, Pageable pageable);
}
