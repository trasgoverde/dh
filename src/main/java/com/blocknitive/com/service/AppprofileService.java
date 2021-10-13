package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.AppprofileDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Appprofile}.
 */
public interface AppprofileService {
    /**
     * Save a appprofile.
     *
     * @param appprofileDTO the entity to save.
     * @return the persisted entity.
     */
    AppprofileDTO save(AppprofileDTO appprofileDTO);

    /**
     * Partially updates a appprofile.
     *
     * @param appprofileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppprofileDTO> partialUpdate(AppprofileDTO appprofileDTO);

    /**
     * Get all the appprofiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppprofileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" appprofile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppprofileDTO> findOne(Long id);

    /**
     * Delete the "id" appprofile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the appprofile corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppprofileDTO> search(String query, Pageable pageable);
}
