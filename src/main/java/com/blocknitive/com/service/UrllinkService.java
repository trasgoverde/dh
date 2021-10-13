package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.UrllinkDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Urllink}.
 */
public interface UrllinkService {
    /**
     * Save a urllink.
     *
     * @param urllinkDTO the entity to save.
     * @return the persisted entity.
     */
    UrllinkDTO save(UrllinkDTO urllinkDTO);

    /**
     * Partially updates a urllink.
     *
     * @param urllinkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UrllinkDTO> partialUpdate(UrllinkDTO urllinkDTO);

    /**
     * Get all the urllinks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UrllinkDTO> findAll(Pageable pageable);

    /**
     * Get the "id" urllink.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UrllinkDTO> findOne(Long id);

    /**
     * Delete the "id" urllink.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the urllink corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UrllinkDTO> search(String query, Pageable pageable);
}
