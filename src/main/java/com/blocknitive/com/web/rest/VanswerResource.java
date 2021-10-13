package com.blocknitive.com.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.repository.VanswerRepository;
import com.blocknitive.com.service.VanswerQueryService;
import com.blocknitive.com.service.VanswerService;
import com.blocknitive.com.service.criteria.VanswerCriteria;
import com.blocknitive.com.service.dto.VanswerDTO;
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
 * REST controller for managing {@link com.blocknitive.com.domain.Vanswer}.
 */
@RestController
@RequestMapping("/api")
public class VanswerResource {

    private final Logger log = LoggerFactory.getLogger(VanswerResource.class);

    private static final String ENTITY_NAME = "vanswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VanswerService vanswerService;

    private final VanswerRepository vanswerRepository;

    private final VanswerQueryService vanswerQueryService;

    public VanswerResource(VanswerService vanswerService, VanswerRepository vanswerRepository, VanswerQueryService vanswerQueryService) {
        this.vanswerService = vanswerService;
        this.vanswerRepository = vanswerRepository;
        this.vanswerQueryService = vanswerQueryService;
    }

    /**
     * {@code POST  /vanswers} : Create a new vanswer.
     *
     * @param vanswerDTO the vanswerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vanswerDTO, or with status {@code 400 (Bad Request)} if the vanswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vanswers")
    public ResponseEntity<VanswerDTO> createVanswer(@Valid @RequestBody VanswerDTO vanswerDTO) throws URISyntaxException {
        log.debug("REST request to save Vanswer : {}", vanswerDTO);
        if (vanswerDTO.getId() != null) {
            throw new BadRequestAlertException("A new vanswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VanswerDTO result = vanswerService.save(vanswerDTO);
        return ResponseEntity
            .created(new URI("/api/vanswers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vanswers/:id} : Updates an existing vanswer.
     *
     * @param id the id of the vanswerDTO to save.
     * @param vanswerDTO the vanswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vanswerDTO,
     * or with status {@code 400 (Bad Request)} if the vanswerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vanswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vanswers/{id}")
    public ResponseEntity<VanswerDTO> updateVanswer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VanswerDTO vanswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vanswer : {}, {}", id, vanswerDTO);
        if (vanswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vanswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vanswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VanswerDTO result = vanswerService.save(vanswerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vanswerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vanswers/:id} : Partial updates given fields of an existing vanswer, field will ignore if it is null
     *
     * @param id the id of the vanswerDTO to save.
     * @param vanswerDTO the vanswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vanswerDTO,
     * or with status {@code 400 (Bad Request)} if the vanswerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vanswerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vanswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vanswers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VanswerDTO> partialUpdateVanswer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VanswerDTO vanswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vanswer partially : {}, {}", id, vanswerDTO);
        if (vanswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vanswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vanswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VanswerDTO> result = vanswerService.partialUpdate(vanswerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vanswerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vanswers} : get all the vanswers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vanswers in body.
     */
    @GetMapping("/vanswers")
    public ResponseEntity<List<VanswerDTO>> getAllVanswers(VanswerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Vanswers by criteria: {}", criteria);
        Page<VanswerDTO> page = vanswerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vanswers/count} : count all the vanswers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vanswers/count")
    public ResponseEntity<Long> countVanswers(VanswerCriteria criteria) {
        log.debug("REST request to count Vanswers by criteria: {}", criteria);
        return ResponseEntity.ok().body(vanswerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vanswers/:id} : get the "id" vanswer.
     *
     * @param id the id of the vanswerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vanswerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vanswers/{id}")
    public ResponseEntity<VanswerDTO> getVanswer(@PathVariable Long id) {
        log.debug("REST request to get Vanswer : {}", id);
        Optional<VanswerDTO> vanswerDTO = vanswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vanswerDTO);
    }

    /**
     * {@code DELETE  /vanswers/:id} : delete the "id" vanswer.
     *
     * @param id the id of the vanswerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vanswers/{id}")
    public ResponseEntity<Void> deleteVanswer(@PathVariable Long id) {
        log.debug("REST request to delete Vanswer : {}", id);
        vanswerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/vanswers?query=:query} : search for the vanswer corresponding
     * to the query.
     *
     * @param query the query of the vanswer search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/vanswers")
    public ResponseEntity<List<VanswerDTO>> searchVanswers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Vanswers for query {}", query);
        Page<VanswerDTO> page = vanswerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
