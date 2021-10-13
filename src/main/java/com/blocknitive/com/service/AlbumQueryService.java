package com.blocknitive.com.service;

import com.blocknitive.com.domain.*; // for static metamodels
import com.blocknitive.com.domain.Album;
import com.blocknitive.com.repository.AlbumRepository;
import com.blocknitive.com.repository.search.AlbumSearchRepository;
import com.blocknitive.com.service.criteria.AlbumCriteria;
import com.blocknitive.com.service.dto.AlbumDTO;
import com.blocknitive.com.service.mapper.AlbumMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Album} entities in the database.
 * The main input is a {@link AlbumCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AlbumDTO} or a {@link Page} of {@link AlbumDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlbumQueryService extends QueryService<Album> {

    private final Logger log = LoggerFactory.getLogger(AlbumQueryService.class);

    private final AlbumRepository albumRepository;

    private final AlbumMapper albumMapper;

    private final AlbumSearchRepository albumSearchRepository;

    public AlbumQueryService(AlbumRepository albumRepository, AlbumMapper albumMapper, AlbumSearchRepository albumSearchRepository) {
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
        this.albumSearchRepository = albumSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AlbumDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AlbumDTO> findByCriteria(AlbumCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Album> specification = createSpecification(criteria);
        return albumMapper.toDto(albumRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AlbumDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlbumDTO> findByCriteria(AlbumCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Album> specification = createSpecification(criteria);
        return albumRepository.findAll(specification, page).map(albumMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlbumCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Album> specification = createSpecification(criteria);
        return albumRepository.count(specification);
    }

    /**
     * Function to convert {@link AlbumCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Album> createSpecification(AlbumCriteria criteria) {
        Specification<Album> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Album_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Album_.creationDate));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Album_.title));
            }
            if (criteria.getPhotoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPhotoId(), root -> root.join(Album_.photos, JoinType.LEFT).get(Photo_.id))
                    );
            }
            if (criteria.getAppuserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppuserId(), root -> root.join(Album_.appuser, JoinType.LEFT).get(Appuser_.id))
                    );
            }
        }
        return specification;
    }
}
