package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.ActivityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Activity}.
 */
public interface ActivityService {
    /**
     * Save a activity.
     *
     * @param activityDTO the entity to save.
     * @return the persisted entity.
     */
    ActivityDTO save(ActivityDTO activityDTO);

    /**
     * Partially updates a activity.
     *
     * @param activityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ActivityDTO> partialUpdate(ActivityDTO activityDTO);

    /**
     * Get all the activities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ActivityDTO> findAll(Pageable pageable);

    /**
     * Get all the activities with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ActivityDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" activity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ActivityDTO> findOne(Long id);

    /**
     * Delete the "id" activity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the activity corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ActivityDTO> search(String query, Pageable pageable);
}
