package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.VthumbDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Vthumb}.
 */
public interface VthumbService {
    /**
     * Save a vthumb.
     *
     * @param vthumbDTO the entity to save.
     * @return the persisted entity.
     */
    VthumbDTO save(VthumbDTO vthumbDTO);

    /**
     * Partially updates a vthumb.
     *
     * @param vthumbDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VthumbDTO> partialUpdate(VthumbDTO vthumbDTO);

    /**
     * Get all the vthumbs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VthumbDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vthumb.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VthumbDTO> findOne(Long id);

    /**
     * Delete the "id" vthumb.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the vthumb corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VthumbDTO> search(String query, Pageable pageable);
}
