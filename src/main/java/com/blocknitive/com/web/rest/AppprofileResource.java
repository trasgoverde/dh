package com.blocknitive.com.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.repository.AppprofileRepository;
import com.blocknitive.com.service.AppprofileQueryService;
import com.blocknitive.com.service.AppprofileService;
import com.blocknitive.com.service.criteria.AppprofileCriteria;
import com.blocknitive.com.service.dto.AppprofileDTO;
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
 * REST controller for managing {@link com.blocknitive.com.domain.Appprofile}.
 */
@RestController
@RequestMapping("/api")
public class AppprofileResource {

    private final Logger log = LoggerFactory.getLogger(AppprofileResource.class);

    private static final String ENTITY_NAME = "appprofile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppprofileService appprofileService;

    private final AppprofileRepository appprofileRepository;

    private final AppprofileQueryService appprofileQueryService;

    public AppprofileResource(
        AppprofileService appprofileService,
        AppprofileRepository appprofileRepository,
        AppprofileQueryService appprofileQueryService
    ) {
        this.appprofileService = appprofileService;
        this.appprofileRepository = appprofileRepository;
        this.appprofileQueryService = appprofileQueryService;
    }

    /**
     * {@code POST  /appprofiles} : Create a new appprofile.
     *
     * @param appprofileDTO the appprofileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appprofileDTO, or with status {@code 400 (Bad Request)} if the appprofile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appprofiles")
    public ResponseEntity<AppprofileDTO> createAppprofile(@Valid @RequestBody AppprofileDTO appprofileDTO) throws URISyntaxException {
        log.debug("REST request to save Appprofile : {}", appprofileDTO);
        if (appprofileDTO.getId() != null) {
            throw new BadRequestAlertException("A new appprofile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppprofileDTO result = appprofileService.save(appprofileDTO);
        return ResponseEntity
            .created(new URI("/api/appprofiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appprofiles/:id} : Updates an existing appprofile.
     *
     * @param id the id of the appprofileDTO to save.
     * @param appprofileDTO the appprofileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appprofileDTO,
     * or with status {@code 400 (Bad Request)} if the appprofileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appprofileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appprofiles/{id}")
    public ResponseEntity<AppprofileDTO> updateAppprofile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppprofileDTO appprofileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Appprofile : {}, {}", id, appprofileDTO);
        if (appprofileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appprofileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appprofileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppprofileDTO result = appprofileService.save(appprofileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appprofileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /appprofiles/:id} : Partial updates given fields of an existing appprofile, field will ignore if it is null
     *
     * @param id the id of the appprofileDTO to save.
     * @param appprofileDTO the appprofileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appprofileDTO,
     * or with status {@code 400 (Bad Request)} if the appprofileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appprofileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appprofileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appprofiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppprofileDTO> partialUpdateAppprofile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppprofileDTO appprofileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Appprofile partially : {}, {}", id, appprofileDTO);
        if (appprofileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appprofileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appprofileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppprofileDTO> result = appprofileService.partialUpdate(appprofileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appprofileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /appprofiles} : get all the appprofiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appprofiles in body.
     */
    @GetMapping("/appprofiles")
    public ResponseEntity<List<AppprofileDTO>> getAllAppprofiles(AppprofileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Appprofiles by criteria: {}", criteria);
        Page<AppprofileDTO> page = appprofileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appprofiles/count} : count all the appprofiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/appprofiles/count")
    public ResponseEntity<Long> countAppprofiles(AppprofileCriteria criteria) {
        log.debug("REST request to count Appprofiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(appprofileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /appprofiles/:id} : get the "id" appprofile.
     *
     * @param id the id of the appprofileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appprofileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appprofiles/{id}")
    public ResponseEntity<AppprofileDTO> getAppprofile(@PathVariable Long id) {
        log.debug("REST request to get Appprofile : {}", id);
        Optional<AppprofileDTO> appprofileDTO = appprofileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appprofileDTO);
    }

    /**
     * {@code DELETE  /appprofiles/:id} : delete the "id" appprofile.
     *
     * @param id the id of the appprofileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appprofiles/{id}")
    public ResponseEntity<Void> deleteAppprofile(@PathVariable Long id) {
        log.debug("REST request to delete Appprofile : {}", id);
        appprofileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/appprofiles?query=:query} : search for the appprofile corresponding
     * to the query.
     *
     * @param query the query of the appprofile search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/appprofiles")
    public ResponseEntity<List<AppprofileDTO>> searchAppprofiles(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Appprofiles for query {}", query);
        Page<AppprofileDTO> page = appprofileService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
