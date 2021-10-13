package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.CmessageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Cmessage}.
 */
public interface CmessageService {
    /**
     * Save a cmessage.
     *
     * @param cmessageDTO the entity to save.
     * @return the persisted entity.
     */
    CmessageDTO save(CmessageDTO cmessageDTO);

    /**
     * Partially updates a cmessage.
     *
     * @param cmessageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CmessageDTO> partialUpdate(CmessageDTO cmessageDTO);

    /**
     * Get all the cmessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CmessageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cmessage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CmessageDTO> findOne(Long id);

    /**
     * Delete the "id" cmessage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cmessage corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CmessageDTO> search(String query, Pageable pageable);
}
