package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.AppphotoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Appphoto}.
 */
public interface AppphotoService {
    /**
     * Save a appphoto.
     *
     * @param appphotoDTO the entity to save.
     * @return the persisted entity.
     */
    AppphotoDTO save(AppphotoDTO appphotoDTO);

    /**
     * Partially updates a appphoto.
     *
     * @param appphotoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppphotoDTO> partialUpdate(AppphotoDTO appphotoDTO);

    /**
     * Get all the appphotos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppphotoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" appphoto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppphotoDTO> findOne(Long id);

    /**
     * Delete the "id" appphoto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the appphoto corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppphotoDTO> search(String query, Pageable pageable);
}
