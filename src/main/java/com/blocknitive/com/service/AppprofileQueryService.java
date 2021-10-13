package com.blocknitive.com.service;

import com.blocknitive.com.domain.*; // for static metamodels
import com.blocknitive.com.domain.Appprofile;
import com.blocknitive.com.repository.AppprofileRepository;
import com.blocknitive.com.repository.search.AppprofileSearchRepository;
import com.blocknitive.com.service.criteria.AppprofileCriteria;
import com.blocknitive.com.service.dto.AppprofileDTO;
import com.blocknitive.com.service.mapper.AppprofileMapper;
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
 * Service for executing complex queries for {@link Appprofile} entities in the database.
 * The main input is a {@link AppprofileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppprofileDTO} or a {@link Page} of {@link AppprofileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppprofileQueryService extends QueryService<Appprofile> {

    private final Logger log = LoggerFactory.getLogger(AppprofileQueryService.class);

    private final AppprofileRepository appprofileRepository;

    private final AppprofileMapper appprofileMapper;

    private final AppprofileSearchRepository appprofileSearchRepository;

    public AppprofileQueryService(
        AppprofileRepository appprofileRepository,
        AppprofileMapper appprofileMapper,
        AppprofileSearchRepository appprofileSearchRepository
    ) {
        this.appprofileRepository = appprofileRepository;
        this.appprofileMapper = appprofileMapper;
        this.appprofileSearchRepository = appprofileSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AppprofileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppprofileDTO> findByCriteria(AppprofileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Appprofile> specification = createSpecification(criteria);
        return appprofileMapper.toDto(appprofileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AppprofileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppprofileDTO> findByCriteria(AppprofileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Appprofile> specification = createSpecification(criteria);
        return appprofileRepository.findAll(specification, page).map(appprofileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppprofileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Appprofile> specification = createSpecification(criteria);
        return appprofileRepository.count(specification);
    }

    /**
     * Function to convert {@link AppprofileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Appprofile> createSpecification(AppprofileCriteria criteria) {
        Specification<Appprofile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Appprofile_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Appprofile_.creationDate));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Appprofile_.gender));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Appprofile_.phone));
            }
            if (criteria.getBio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBio(), Appprofile_.bio));
            }
            if (criteria.getFacebook() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFacebook(), Appprofile_.facebook));
            }
            if (criteria.getTwitter() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTwitter(), Appprofile_.twitter));
            }
            if (criteria.getLinkedin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinkedin(), Appprofile_.linkedin));
            }
            if (criteria.getInstagram() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInstagram(), Appprofile_.instagram));
            }
            if (criteria.getGooglePlus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGooglePlus(), Appprofile_.googlePlus));
            }
            if (criteria.getBirthdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthdate(), Appprofile_.birthdate));
            }
            if (criteria.getCivilStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getCivilStatus(), Appprofile_.civilStatus));
            }
            if (criteria.getLookingFor() != null) {
                specification = specification.and(buildSpecification(criteria.getLookingFor(), Appprofile_.lookingFor));
            }
            if (criteria.getPurpose() != null) {
                specification = specification.and(buildSpecification(criteria.getPurpose(), Appprofile_.purpose));
            }
            if (criteria.getPhysical() != null) {
                specification = specification.and(buildSpecification(criteria.getPhysical(), Appprofile_.physical));
            }
            if (criteria.getReligion() != null) {
                specification = specification.and(buildSpecification(criteria.getReligion(), Appprofile_.religion));
            }
            if (criteria.getEthnicGroup() != null) {
                specification = specification.and(buildSpecification(criteria.getEthnicGroup(), Appprofile_.ethnicGroup));
            }
            if (criteria.getStudies() != null) {
                specification = specification.and(buildSpecification(criteria.getStudies(), Appprofile_.studies));
            }
            if (criteria.getSibblings() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSibblings(), Appprofile_.sibblings));
            }
            if (criteria.getEyes() != null) {
                specification = specification.and(buildSpecification(criteria.getEyes(), Appprofile_.eyes));
            }
            if (criteria.getSmoker() != null) {
                specification = specification.and(buildSpecification(criteria.getSmoker(), Appprofile_.smoker));
            }
            if (criteria.getChildren() != null) {
                specification = specification.and(buildSpecification(criteria.getChildren(), Appprofile_.children));
            }
            if (criteria.getFutureChildren() != null) {
                specification = specification.and(buildSpecification(criteria.getFutureChildren(), Appprofile_.futureChildren));
            }
            if (criteria.getPet() != null) {
                specification = specification.and(buildSpecification(criteria.getPet(), Appprofile_.pet));
            }
            if (criteria.getAppuserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppuserId(), root -> root.join(Appprofile_.appuser, JoinType.LEFT).get(Appuser_.id))
                    );
            }
        }
        return specification;
    }
}
