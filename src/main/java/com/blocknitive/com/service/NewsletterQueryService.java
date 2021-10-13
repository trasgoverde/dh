package com.blocknitive.com.service;

import com.blocknitive.com.domain.*; // for static metamodels
import com.blocknitive.com.domain.Newsletter;
import com.blocknitive.com.repository.NewsletterRepository;
import com.blocknitive.com.repository.search.NewsletterSearchRepository;
import com.blocknitive.com.service.criteria.NewsletterCriteria;
import com.blocknitive.com.service.dto.NewsletterDTO;
import com.blocknitive.com.service.mapper.NewsletterMapper;
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
 * Service for executing complex queries for {@link Newsletter} entities in the database.
 * The main input is a {@link NewsletterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NewsletterDTO} or a {@link Page} of {@link NewsletterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NewsletterQueryService extends QueryService<Newsletter> {

    private final Logger log = LoggerFactory.getLogger(NewsletterQueryService.class);

    private final NewsletterRepository newsletterRepository;

    private final NewsletterMapper newsletterMapper;

    private final NewsletterSearchRepository newsletterSearchRepository;

    public NewsletterQueryService(
        NewsletterRepository newsletterRepository,
        NewsletterMapper newsletterMapper,
        NewsletterSearchRepository newsletterSearchRepository
    ) {
        this.newsletterRepository = newsletterRepository;
        this.newsletterMapper = newsletterMapper;
        this.newsletterSearchRepository = newsletterSearchRepository;
    }

    /**
     * Return a {@link List} of {@link NewsletterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NewsletterDTO> findByCriteria(NewsletterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Newsletter> specification = createSpecification(criteria);
        return newsletterMapper.toDto(newsletterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NewsletterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NewsletterDTO> findByCriteria(NewsletterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Newsletter> specification = createSpecification(criteria);
        return newsletterRepository.findAll(specification, page).map(newsletterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NewsletterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Newsletter> specification = createSpecification(criteria);
        return newsletterRepository.count(specification);
    }

    /**
     * Function to convert {@link NewsletterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Newsletter> createSpecification(NewsletterCriteria criteria) {
        Specification<Newsletter> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Newsletter_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Newsletter_.creationDate));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Newsletter_.email));
            }
        }
        return specification;
    }
}
