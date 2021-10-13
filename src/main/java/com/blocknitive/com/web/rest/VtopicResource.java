package com.blocknitive.com.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.repository.VtopicRepository;
import com.blocknitive.com.service.VtopicQueryService;
import com.blocknitive.com.service.VtopicService;
import com.blocknitive.com.service.criteria.VtopicCriteria;
import com.blocknitive.com.service.dto.VtopicDTO;
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
 * REST controller for managing {@link com.blocknitive.com.domain.Vtopic}.
 */
@RestController
@RequestMapping("/api")
public class VtopicResource {

    private final Logger log = LoggerFactory.getLogger(VtopicResource.class);

    private static final String ENTITY_NAME = "vtopic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VtopicService vtopicService;

    private final VtopicRepository vtopicRepository;

    private final VtopicQueryService vtopicQueryService;

    public VtopicResource(VtopicService vtopicService, VtopicRepository vtopicRepository, VtopicQueryService vtopicQueryService) {
        this.vtopicService = vtopicService;
        this.vtopicRepository = vtopicRepository;
        this.vtopicQueryService = vtopicQueryService;
    }

    /**
     * {@code POST  /vtopics} : Create a new vtopic.
     *
     * @param vtopicDTO the vtopicDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vtopicDTO, or with status {@code 400 (Bad Request)} if the vtopic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vtopics")
    public ResponseEntity<VtopicDTO> createVtopic(@Valid @RequestBody VtopicDTO vtopicDTO) throws URISyntaxException {
        log.debug("REST request to save Vtopic : {}", vtopicDTO);
        if (vtopicDTO.getId() != null) {
            throw new BadRequestAlertException("A new vtopic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VtopicDTO result = vtopicService.save(vtopicDTO);
        return ResponseEntity
            .created(new URI("/api/vtopics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vtopics/:id} : Updates an existing vtopic.
     *
     * @param id the id of the vtopicDTO to save.
     * @param vtopicDTO the vtopicDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vtopicDTO,
     * or with status {@code 400 (Bad Request)} if the vtopicDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vtopicDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vtopics/{id}")
    public ResponseEntity<VtopicDTO> updateVtopic(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VtopicDTO vtopicDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vtopic : {}, {}", id, vtopicDTO);
        if (vtopicDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vtopicDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vtopicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VtopicDTO result = vtopicService.save(vtopicDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vtopicDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vtopics/:id} : Partial updates given fields of an existing vtopic, field will ignore if it is null
     *
     * @param id the id of the vtopicDTO to save.
     * @param vtopicDTO the vtopicDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vtopicDTO,
     * or with status {@code 400 (Bad Request)} if the vtopicDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vtopicDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vtopicDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vtopics/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VtopicDTO> partialUpdateVtopic(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VtopicDTO vtopicDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vtopic partially : {}, {}", id, vtopicDTO);
        if (vtopicDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vtopicDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vtopicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VtopicDTO> result = vtopicService.partialUpdate(vtopicDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vtopicDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vtopics} : get all the vtopics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vtopics in body.
     */
    @GetMapping("/vtopics")
    public ResponseEntity<List<VtopicDTO>> getAllVtopics(VtopicCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Vtopics by criteria: {}", criteria);
        Page<VtopicDTO> page = vtopicQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vtopics/count} : count all the vtopics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vtopics/count")
    public ResponseEntity<Long> countVtopics(VtopicCriteria criteria) {
        log.debug("REST request to count Vtopics by criteria: {}", criteria);
        return ResponseEntity.ok().body(vtopicQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vtopics/:id} : get the "id" vtopic.
     *
     * @param id the id of the vtopicDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vtopicDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vtopics/{id}")
    public ResponseEntity<VtopicDTO> getVtopic(@PathVariable Long id) {
        log.debug("REST request to get Vtopic : {}", id);
        Optional<VtopicDTO> vtopicDTO = vtopicService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vtopicDTO);
    }

    /**
     * {@code DELETE  /vtopics/:id} : delete the "id" vtopic.
     *
     * @param id the id of the vtopicDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vtopics/{id}")
    public ResponseEntity<Void> deleteVtopic(@PathVariable Long id) {
        log.debug("REST request to delete Vtopic : {}", id);
        vtopicService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/vtopics?query=:query} : search for the vtopic corresponding
     * to the query.
     *
     * @param query the query of the vtopic search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/vtopics")
    public ResponseEntity<List<VtopicDTO>> searchVtopics(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Vtopics for query {}", query);
        Page<VtopicDTO> page = vtopicService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
