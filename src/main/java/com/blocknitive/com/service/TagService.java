package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.TagDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Tag}.
 */
public interface TagService {
    /**
     * Save a tag.
     *
     * @param tagDTO the entity to save.
     * @return the persisted entity.
     */
    TagDTO save(TagDTO tagDTO);

    /**
     * Partially updates a tag.
     *
     * @param tagDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TagDTO> partialUpdate(TagDTO tagDTO);

    /**
     * Get all the tags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TagDTO> findAll(Pageable pageable);

    /**
     * Get all the tags with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TagDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" tag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TagDTO> findOne(Long id);

    /**
     * Delete the "id" tag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the tag corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TagDTO> search(String query, Pageable pageable);
}
