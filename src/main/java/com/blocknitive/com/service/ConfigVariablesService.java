package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.ConfigVariablesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.ConfigVariables}.
 */
public interface ConfigVariablesService {
    /**
     * Save a configVariables.
     *
     * @param configVariablesDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigVariablesDTO save(ConfigVariablesDTO configVariablesDTO);

    /**
     * Partially updates a configVariables.
     *
     * @param configVariablesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConfigVariablesDTO> partialUpdate(ConfigVariablesDTO configVariablesDTO);

    /**
     * Get all the configVariables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigVariablesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" configVariables.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigVariablesDTO> findOne(Long id);

    /**
     * Delete the "id" configVariables.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the configVariables corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigVariablesDTO> search(String query, Pageable pageable);
}
