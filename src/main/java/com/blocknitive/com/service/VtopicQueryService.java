package com.blocknitive.com.service;

import com.blocknitive.com.domain.*; // for static metamodels
import com.blocknitive.com.domain.Vtopic;
import com.blocknitive.com.repository.VtopicRepository;
import com.blocknitive.com.repository.search.VtopicSearchRepository;
import com.blocknitive.com.service.criteria.VtopicCriteria;
import com.blocknitive.com.service.dto.VtopicDTO;
import com.blocknitive.com.service.mapper.VtopicMapper;
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
 * Service for executing complex queries for {@link Vtopic} entities in the database.
 * The main input is a {@link VtopicCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VtopicDTO} or a {@link Page} of {@link VtopicDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VtopicQueryService extends QueryService<Vtopic> {

    private final Logger log = LoggerFactory.getLogger(VtopicQueryService.class);

    private final VtopicRepository vtopicRepository;

    private final VtopicMapper vtopicMapper;

    private final VtopicSearchRepository vtopicSearchRepository;

    public VtopicQueryService(VtopicRepository vtopicRepository, VtopicMapper vtopicMapper, VtopicSearchRepository vtopicSearchRepository) {
        this.vtopicRepository = vtopicRepository;
        this.vtopicMapper = vtopicMapper;
        this.vtopicSearchRepository = vtopicSearchRepository;
    }

    /**
     * Return a {@link List} of {@link VtopicDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VtopicDTO> findByCriteria(VtopicCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vtopic> specification = createSpecification(criteria);
        return vtopicMapper.toDto(vtopicRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VtopicDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VtopicDTO> findByCriteria(VtopicCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vtopic> specification = createSpecification(criteria);
        return vtopicRepository.findAll(specification, page).map(vtopicMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VtopicCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vtopic> specification = createSpecification(criteria);
        return vtopicRepository.count(specification);
    }

    /**
     * Function to convert {@link VtopicCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vtopic> createSpecification(VtopicCriteria criteria) {
        Specification<Vtopic> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vtopic_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Vtopic_.creationDate));
            }
            if (criteria.getVtopicTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVtopicTitle(), Vtopic_.vtopicTitle));
            }
            if (criteria.getVtopicDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVtopicDescription(), Vtopic_.vtopicDescription));
            }
            if (criteria.getVquestionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVquestionId(),
                            root -> root.join(Vtopic_.vquestions, JoinType.LEFT).get(Vquestion_.id)
                        )
                    );
            }
            if (criteria.getAppuserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppuserId(), root -> root.join(Vtopic_.appuser, JoinType.LEFT).get(Appuser_.id))
                    );
            }
        }
        return specification;
    }
}
