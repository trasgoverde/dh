package com.blocknitive.com.service;

import com.blocknitive.com.domain.*; // for static metamodels
import com.blocknitive.com.domain.Cceleb;
import com.blocknitive.com.repository.CcelebRepository;
import com.blocknitive.com.repository.search.CcelebSearchRepository;
import com.blocknitive.com.service.criteria.CcelebCriteria;
import com.blocknitive.com.service.dto.CcelebDTO;
import com.blocknitive.com.service.mapper.CcelebMapper;
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
 * Service for executing complex queries for {@link Cceleb} entities in the database.
 * The main input is a {@link CcelebCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CcelebDTO} or a {@link Page} of {@link CcelebDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CcelebQueryService extends QueryService<Cceleb> {

    private final Logger log = LoggerFactory.getLogger(CcelebQueryService.class);

    private final CcelebRepository ccelebRepository;

    private final CcelebMapper ccelebMapper;

    private final CcelebSearchRepository ccelebSearchRepository;

    public CcelebQueryService(CcelebRepository ccelebRepository, CcelebMapper ccelebMapper, CcelebSearchRepository ccelebSearchRepository) {
        this.ccelebRepository = ccelebRepository;
        this.ccelebMapper = ccelebMapper;
        this.ccelebSearchRepository = ccelebSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CcelebDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CcelebDTO> findByCriteria(CcelebCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cceleb> specification = createSpecification(criteria);
        return ccelebMapper.toDto(ccelebRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CcelebDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CcelebDTO> findByCriteria(CcelebCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cceleb> specification = createSpecification(criteria);
        return ccelebRepository.findAll(specification, page).map(ccelebMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CcelebCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cceleb> specification = createSpecification(criteria);
        return ccelebRepository.count(specification);
    }

    /**
     * Function to convert {@link CcelebCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cceleb> createSpecification(CcelebCriteria criteria) {
        Specification<Cceleb> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cceleb_.id));
            }
            if (criteria.getCelebName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCelebName(), Cceleb_.celebName));
            }
            if (criteria.getCommunityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommunityId(),
                            root -> root.join(Cceleb_.communities, JoinType.LEFT).get(Community_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
