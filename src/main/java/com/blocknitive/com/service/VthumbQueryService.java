package com.blocknitive.com.service;

import com.blocknitive.com.domain.*; // for static metamodels
import com.blocknitive.com.domain.Vthumb;
import com.blocknitive.com.repository.VthumbRepository;
import com.blocknitive.com.repository.search.VthumbSearchRepository;
import com.blocknitive.com.service.criteria.VthumbCriteria;
import com.blocknitive.com.service.dto.VthumbDTO;
import com.blocknitive.com.service.mapper.VthumbMapper;
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
 * Service for executing complex queries for {@link Vthumb} entities in the database.
 * The main input is a {@link VthumbCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VthumbDTO} or a {@link Page} of {@link VthumbDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VthumbQueryService extends QueryService<Vthumb> {

    private final Logger log = LoggerFactory.getLogger(VthumbQueryService.class);

    private final VthumbRepository vthumbRepository;

    private final VthumbMapper vthumbMapper;

    private final VthumbSearchRepository vthumbSearchRepository;

    public VthumbQueryService(VthumbRepository vthumbRepository, VthumbMapper vthumbMapper, VthumbSearchRepository vthumbSearchRepository) {
        this.vthumbRepository = vthumbRepository;
        this.vthumbMapper = vthumbMapper;
        this.vthumbSearchRepository = vthumbSearchRepository;
    }

    /**
     * Return a {@link List} of {@link VthumbDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VthumbDTO> findByCriteria(VthumbCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vthumb> specification = createSpecification(criteria);
        return vthumbMapper.toDto(vthumbRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VthumbDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VthumbDTO> findByCriteria(VthumbCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vthumb> specification = createSpecification(criteria);
        return vthumbRepository.findAll(specification, page).map(vthumbMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VthumbCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vthumb> specification = createSpecification(criteria);
        return vthumbRepository.count(specification);
    }

    /**
     * Function to convert {@link VthumbCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vthumb> createSpecification(VthumbCriteria criteria) {
        Specification<Vthumb> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vthumb_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Vthumb_.creationDate));
            }
            if (criteria.getVthumbUp() != null) {
                specification = specification.and(buildSpecification(criteria.getVthumbUp(), Vthumb_.vthumbUp));
            }
            if (criteria.getVthumbDown() != null) {
                specification = specification.and(buildSpecification(criteria.getVthumbDown(), Vthumb_.vthumbDown));
            }
            if (criteria.getAppuserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppuserId(), root -> root.join(Vthumb_.appuser, JoinType.LEFT).get(Appuser_.id))
                    );
            }
            if (criteria.getVquestionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVquestionId(),
                            root -> root.join(Vthumb_.vquestion, JoinType.LEFT).get(Vquestion_.id)
                        )
                    );
            }
            if (criteria.getVanswerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVanswerId(), root -> root.join(Vthumb_.vanswer, JoinType.LEFT).get(Vanswer_.id))
                    );
            }
        }
        return specification;
    }
}
