package com.blocknitive.com.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.repository.CmessageRepository;
import com.blocknitive.com.service.CmessageQueryService;
import com.blocknitive.com.service.CmessageService;
import com.blocknitive.com.service.criteria.CmessageCriteria;
import com.blocknitive.com.service.dto.CmessageDTO;
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
 * REST controller for managing {@link com.blocknitive.com.domain.Cmessage}.
 */
@RestController
@RequestMapping("/api")
public class CmessageResource {

    private final Logger log = LoggerFactory.getLogger(CmessageResource.class);

    private static final String ENTITY_NAME = "cmessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CmessageService cmessageService;

    private final CmessageRepository cmessageRepository;

    private final CmessageQueryService cmessageQueryService;

    public CmessageResource(
        CmessageService cmessageService,
        CmessageRepository cmessageRepository,
        CmessageQueryService cmessageQueryService
    ) {
        this.cmessageService = cmessageService;
        this.cmessageRepository = cmessageRepository;
        this.cmessageQueryService = cmessageQueryService;
    }

    /**
     * {@code POST  /cmessages} : Create a new cmessage.
     *
     * @param cmessageDTO the cmessageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cmessageDTO, or with status {@code 400 (Bad Request)} if the cmessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cmessages")
    public ResponseEntity<CmessageDTO> createCmessage(@Valid @RequestBody CmessageDTO cmessageDTO) throws URISyntaxException {
        log.debug("REST request to save Cmessage : {}", cmessageDTO);
        if (cmessageDTO.getId() != null) {
            throw new BadRequestAlertException("A new cmessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CmessageDTO result = cmessageService.save(cmessageDTO);
        return ResponseEntity
            .created(new URI("/api/cmessages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cmessages/:id} : Updates an existing cmessage.
     *
     * @param id the id of the cmessageDTO to save.
     * @param cmessageDTO the cmessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cmessageDTO,
     * or with status {@code 400 (Bad Request)} if the cmessageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cmessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cmessages/{id}")
    public ResponseEntity<CmessageDTO> updateCmessage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CmessageDTO cmessageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cmessage : {}, {}", id, cmessageDTO);
        if (cmessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cmessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cmessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CmessageDTO result = cmessageService.save(cmessageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmessageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cmessages/:id} : Partial updates given fields of an existing cmessage, field will ignore if it is null
     *
     * @param id the id of the cmessageDTO to save.
     * @param cmessageDTO the cmessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cmessageDTO,
     * or with status {@code 400 (Bad Request)} if the cmessageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cmessageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cmessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cmessages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CmessageDTO> partialUpdateCmessage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CmessageDTO cmessageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cmessage partially : {}, {}", id, cmessageDTO);
        if (cmessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cmessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cmessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CmessageDTO> result = cmessageService.partialUpdate(cmessageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmessageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cmessages} : get all the cmessages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cmessages in body.
     */
    @GetMapping("/cmessages")
    public ResponseEntity<List<CmessageDTO>> getAllCmessages(CmessageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cmessages by criteria: {}", criteria);
        Page<CmessageDTO> page = cmessageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cmessages/count} : count all the cmessages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cmessages/count")
    public ResponseEntity<Long> countCmessages(CmessageCriteria criteria) {
        log.debug("REST request to count Cmessages by criteria: {}", criteria);
        return ResponseEntity.ok().body(cmessageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cmessages/:id} : get the "id" cmessage.
     *
     * @param id the id of the cmessageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cmessageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cmessages/{id}")
    public ResponseEntity<CmessageDTO> getCmessage(@PathVariable Long id) {
        log.debug("REST request to get Cmessage : {}", id);
        Optional<CmessageDTO> cmessageDTO = cmessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cmessageDTO);
    }

    /**
     * {@code DELETE  /cmessages/:id} : delete the "id" cmessage.
     *
     * @param id the id of the cmessageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cmessages/{id}")
    public ResponseEntity<Void> deleteCmessage(@PathVariable Long id) {
        log.debug("REST request to delete Cmessage : {}", id);
        cmessageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/cmessages?query=:query} : search for the cmessage corresponding
     * to the query.
     *
     * @param query the query of the cmessage search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/cmessages")
    public ResponseEntity<List<CmessageDTO>> searchCmessages(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Cmessages for query {}", query);
        Page<CmessageDTO> page = cmessageService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
