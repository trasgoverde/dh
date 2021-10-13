package com.blocknitive.com.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.repository.VquestionRepository;
import com.blocknitive.com.service.VquestionQueryService;
import com.blocknitive.com.service.VquestionService;
import com.blocknitive.com.service.criteria.VquestionCriteria;
import com.blocknitive.com.service.dto.VquestionDTO;
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
 * REST controller for managing {@link com.blocknitive.com.domain.Vquestion}.
 */
@RestController
@RequestMapping("/api")
public class VquestionResource {

    private final Logger log = LoggerFactory.getLogger(VquestionResource.class);

    private static final String ENTITY_NAME = "vquestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VquestionService vquestionService;

    private final VquestionRepository vquestionRepository;

    private final VquestionQueryService vquestionQueryService;

    public VquestionResource(
        VquestionService vquestionService,
        VquestionRepository vquestionRepository,
        VquestionQueryService vquestionQueryService
    ) {
        this.vquestionService = vquestionService;
        this.vquestionRepository = vquestionRepository;
        this.vquestionQueryService = vquestionQueryService;
    }

    /**
     * {@code POST  /vquestions} : Create a new vquestion.
     *
     * @param vquestionDTO the vquestionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vquestionDTO, or with status {@code 400 (Bad Request)} if the vquestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vquestions")
    public ResponseEntity<VquestionDTO> createVquestion(@Valid @RequestBody VquestionDTO vquestionDTO) throws URISyntaxException {
        log.debug("REST request to save Vquestion : {}", vquestionDTO);
        if (vquestionDTO.getId() != null) {
            throw new BadRequestAlertException("A new vquestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VquestionDTO result = vquestionService.save(vquestionDTO);
        return ResponseEntity
            .created(new URI("/api/vquestions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vquestions/:id} : Updates an existing vquestion.
     *
     * @param id the id of the vquestionDTO to save.
     * @param vquestionDTO the vquestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vquestionDTO,
     * or with status {@code 400 (Bad Request)} if the vquestionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vquestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vquestions/{id}")
    public ResponseEntity<VquestionDTO> updateVquestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VquestionDTO vquestionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vquestion : {}, {}", id, vquestionDTO);
        if (vquestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vquestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vquestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VquestionDTO result = vquestionService.save(vquestionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vquestionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vquestions/:id} : Partial updates given fields of an existing vquestion, field will ignore if it is null
     *
     * @param id the id of the vquestionDTO to save.
     * @param vquestionDTO the vquestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vquestionDTO,
     * or with status {@code 400 (Bad Request)} if the vquestionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vquestionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vquestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vquestions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VquestionDTO> partialUpdateVquestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VquestionDTO vquestionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vquestion partially : {}, {}", id, vquestionDTO);
        if (vquestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vquestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vquestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VquestionDTO> result = vquestionService.partialUpdate(vquestionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vquestionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vquestions} : get all the vquestions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vquestions in body.
     */
    @GetMapping("/vquestions")
    public ResponseEntity<List<VquestionDTO>> getAllVquestions(VquestionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Vquestions by criteria: {}", criteria);
        Page<VquestionDTO> page = vquestionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vquestions/count} : count all the vquestions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vquestions/count")
    public ResponseEntity<Long> countVquestions(VquestionCriteria criteria) {
        log.debug("REST request to count Vquestions by criteria: {}", criteria);
        return ResponseEntity.ok().body(vquestionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vquestions/:id} : get the "id" vquestion.
     *
     * @param id the id of the vquestionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vquestionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vquestions/{id}")
    public ResponseEntity<VquestionDTO> getVquestion(@PathVariable Long id) {
        log.debug("REST request to get Vquestion : {}", id);
        Optional<VquestionDTO> vquestionDTO = vquestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vquestionDTO);
    }

    /**
     * {@code DELETE  /vquestions/:id} : delete the "id" vquestion.
     *
     * @param id the id of the vquestionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vquestions/{id}")
    public ResponseEntity<Void> deleteVquestion(@PathVariable Long id) {
        log.debug("REST request to delete Vquestion : {}", id);
        vquestionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/vquestions?query=:query} : search for the vquestion corresponding
     * to the query.
     *
     * @param query the query of the vquestion search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/vquestions")
    public ResponseEntity<List<VquestionDTO>> searchVquestions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Vquestions for query {}", query);
        Page<VquestionDTO> page = vquestionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
