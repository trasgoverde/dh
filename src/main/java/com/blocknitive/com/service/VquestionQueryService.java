package com.blocknitive.com.service;

import com.blocknitive.com.domain.*; // for static metamodels
import com.blocknitive.com.domain.Vquestion;
import com.blocknitive.com.repository.VquestionRepository;
import com.blocknitive.com.repository.search.VquestionSearchRepository;
import com.blocknitive.com.service.criteria.VquestionCriteria;
import com.blocknitive.com.service.dto.VquestionDTO;
import com.blocknitive.com.service.mapper.VquestionMapper;
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
 * Service for executing complex queries for {@link Vquestion} entities in the database.
 * The main input is a {@link VquestionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VquestionDTO} or a {@link Page} of {@link VquestionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VquestionQueryService extends QueryService<Vquestion> {

    private final Logger log = LoggerFactory.getLogger(VquestionQueryService.class);

    private final VquestionRepository vquestionRepository;

    private final VquestionMapper vquestionMapper;

    private final VquestionSearchRepository vquestionSearchRepository;

    public VquestionQueryService(
        VquestionRepository vquestionRepository,
        VquestionMapper vquestionMapper,
        VquestionSearchRepository vquestionSearchRepository
    ) {
        this.vquestionRepository = vquestionRepository;
        this.vquestionMapper = vquestionMapper;
        this.vquestionSearchRepository = vquestionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link VquestionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VquestionDTO> findByCriteria(VquestionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vquestion> specification = createSpecification(criteria);
        return vquestionMapper.toDto(vquestionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VquestionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VquestionDTO> findByCriteria(VquestionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vquestion> specification = createSpecification(criteria);
        return vquestionRepository.findAll(specification, page).map(vquestionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VquestionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vquestion> specification = createSpecification(criteria);
        return vquestionRepository.count(specification);
    }

    /**
     * Function to convert {@link VquestionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vquestion> createSpecification(VquestionCriteria criteria) {
        Specification<Vquestion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vquestion_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Vquestion_.creationDate));
            }
            if (criteria.getVquestion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVquestion(), Vquestion_.vquestion));
            }
            if (criteria.getVquestionDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getVquestionDescription(), Vquestion_.vquestionDescription));
            }
            if (criteria.getVanswerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVanswerId(), root -> root.join(Vquestion_.vanswers, JoinType.LEFT).get(Vanswer_.id))
                    );
            }
            if (criteria.getVthumbId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVthumbId(), root -> root.join(Vquestion_.vthumbs, JoinType.LEFT).get(Vthumb_.id))
                    );
            }
            if (criteria.getAppuserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppuserId(), root -> root.join(Vquestion_.appuser, JoinType.LEFT).get(Appuser_.id))
                    );
            }
            if (criteria.getVtopicId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVtopicId(), root -> root.join(Vquestion_.vtopic, JoinType.LEFT).get(Vtopic_.id))
                    );
            }
        }
        return specification;
    }
}
