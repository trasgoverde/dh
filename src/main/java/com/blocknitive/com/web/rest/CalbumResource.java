package com.blocknitive.com.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.repository.CalbumRepository;
import com.blocknitive.com.service.CalbumQueryService;
import com.blocknitive.com.service.CalbumService;
import com.blocknitive.com.service.criteria.CalbumCriteria;
import com.blocknitive.com.service.dto.CalbumDTO;
import com.blocknitive.com.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.blocknitive.com.domain.Calbum}.
 */
@RestController
@RequestMapping("/api")
public class CalbumResource {

    private final Logger log = LoggerFactory.getLogger(CalbumResource.class);

    private static final String ENTITY_NAME = "calbum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalbumService calbumService;

    private final CalbumRepository calbumRepository;

    private final CalbumQueryService calbumQueryService;

    public CalbumResource(CalbumService calbumService, CalbumRepository calbumRepository, CalbumQueryService calbumQueryService) {
        this.calbumService = calbumService;
        this.calbumRepository = calbumRepository;
        this.calbumQueryService = calbumQueryService;
    }

    /**
     * {@code POST  /calbums} : Create a new calbum.
     *
     * @param calbumDTO the calbumDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calbumDTO, or with status {@code 400 (Bad Request)} if the calbum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calbums")
    public ResponseEntity<CalbumDTO> createCalbum(@Valid @RequestBody CalbumDTO calbumDTO) throws URISyntaxException {
        log.debug("REST request to save Calbum : {}", calbumDTO);
        if (calbumDTO.getId() != null) {
            throw new BadRequestAlertException("A new calbum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CalbumDTO result = calbumService.save(calbumDTO);
        return ResponseEntity
            .created(new URI("/api/calbums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calbums/:id} : Updates an existing calbum.
     *
     * @param id the id of the calbumDTO to save.
     * @param calbumDTO the calbumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calbumDTO,
     * or with status {@code 400 (Bad Request)} if the calbumDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calbumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calbums/{id}")
    public ResponseEntity<CalbumDTO> updateCalbum(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CalbumDTO calbumDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Calbum : {}, {}", id, calbumDTO);
        if (calbumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calbumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calbumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CalbumDTO result = calbumService.save(calbumDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calbumDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /calbums/:id} : Partial updates given fields of an existing calbum, field will ignore if it is null
     *
     * @param id the id of the calbumDTO to save.
     * @param calbumDTO the calbumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calbumDTO,
     * or with status {@code 400 (Bad Request)} if the calbumDTO is not valid,
     * or with status {@code 404 (Not Found)} if the calbumDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the calbumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/calbums/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CalbumDTO> partialUpdateCalbum(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CalbumDTO calbumDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Calbum partially : {}, {}", id, calbumDTO);
        if (calbumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calbumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calbumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CalbumDTO> result = calbumService.partialUpdate(calbumDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calbumDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /calbums} : get all the calbums.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calbums in body.
     */
    @GetMapping("/calbums")
    public ResponseEntity<List<CalbumDTO>> getAllCalbums(CalbumCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Calbums by criteria: {}", criteria);
        Page<CalbumDTO> page = calbumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /calbums/count} : count all the calbums.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/calbums/count")
    public ResponseEntity<Long> countCalbums(CalbumCriteria criteria) {
        log.debug("REST request to count Calbums by criteria: {}", criteria);
        return ResponseEntity.ok().body(calbumQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /calbums/:id} : get the "id" calbum.
     *
     * @param id the id of the calbumDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calbumDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calbums/{id}")
    public ResponseEntity<CalbumDTO> getCalbum(@PathVariable Long id) {
        log.debug("REST request to get Calbum : {}", id);
        Optional<CalbumDTO> calbumDTO = calbumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(calbumDTO);
    }

    /**
     * {@code DELETE  /calbums/:id} : delete the "id" calbum.
     *
     * @param id the id of the calbumDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calbums/{id}")
    public ResponseEntity<Void> deleteCalbum(@PathVariable Long id) {
        log.debug("REST request to delete Calbum : {}", id);
        calbumService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/calbums?query=:query} : search for the calbum corresponding
     * to the query.
     *
     * @param query the query of the calbum search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/calbums")
    public ResponseEntity<List<CalbumDTO>> searchCalbums(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Calbums for query {}", query);
        Page<CalbumDTO> page = calbumService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
