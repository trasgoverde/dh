package com.blocknitive.com.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.repository.VthumbRepository;
import com.blocknitive.com.service.VthumbQueryService;
import com.blocknitive.com.service.VthumbService;
import com.blocknitive.com.service.criteria.VthumbCriteria;
import com.blocknitive.com.service.dto.VthumbDTO;
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
 * REST controller for managing {@link com.blocknitive.com.domain.Vthumb}.
 */
@RestController
@RequestMapping("/api")
public class VthumbResource {

    private final Logger log = LoggerFactory.getLogger(VthumbResource.class);

    private static final String ENTITY_NAME = "vthumb";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VthumbService vthumbService;

    private final VthumbRepository vthumbRepository;

    private final VthumbQueryService vthumbQueryService;

    public VthumbResource(VthumbService vthumbService, VthumbRepository vthumbRepository, VthumbQueryService vthumbQueryService) {
        this.vthumbService = vthumbService;
        this.vthumbRepository = vthumbRepository;
        this.vthumbQueryService = vthumbQueryService;
    }

    /**
     * {@code POST  /vthumbs} : Create a new vthumb.
     *
     * @param vthumbDTO the vthumbDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vthumbDTO, or with status {@code 400 (Bad Request)} if the vthumb has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vthumbs")
    public ResponseEntity<VthumbDTO> createVthumb(@Valid @RequestBody VthumbDTO vthumbDTO) throws URISyntaxException {
        log.debug("REST request to save Vthumb : {}", vthumbDTO);
        if (vthumbDTO.getId() != null) {
            throw new BadRequestAlertException("A new vthumb cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VthumbDTO result = vthumbService.save(vthumbDTO);
        return ResponseEntity
            .created(new URI("/api/vthumbs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vthumbs/:id} : Updates an existing vthumb.
     *
     * @param id the id of the vthumbDTO to save.
     * @param vthumbDTO the vthumbDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vthumbDTO,
     * or with status {@code 400 (Bad Request)} if the vthumbDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vthumbDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vthumbs/{id}")
    public ResponseEntity<VthumbDTO> updateVthumb(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VthumbDTO vthumbDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vthumb : {}, {}", id, vthumbDTO);
        if (vthumbDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vthumbDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vthumbRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VthumbDTO result = vthumbService.save(vthumbDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vthumbDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vthumbs/:id} : Partial updates given fields of an existing vthumb, field will ignore if it is null
     *
     * @param id the id of the vthumbDTO to save.
     * @param vthumbDTO the vthumbDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vthumbDTO,
     * or with status {@code 400 (Bad Request)} if the vthumbDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vthumbDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vthumbDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vthumbs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VthumbDTO> partialUpdateVthumb(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VthumbDTO vthumbDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vthumb partially : {}, {}", id, vthumbDTO);
        if (vthumbDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vthumbDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vthumbRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VthumbDTO> result = vthumbService.partialUpdate(vthumbDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vthumbDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vthumbs} : get all the vthumbs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vthumbs in body.
     */
    @GetMapping("/vthumbs")
    public ResponseEntity<List<VthumbDTO>> getAllVthumbs(VthumbCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Vthumbs by criteria: {}", criteria);
        Page<VthumbDTO> page = vthumbQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vthumbs/count} : count all the vthumbs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vthumbs/count")
    public ResponseEntity<Long> countVthumbs(VthumbCriteria criteria) {
        log.debug("REST request to count Vthumbs by criteria: {}", criteria);
        return ResponseEntity.ok().body(vthumbQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vthumbs/:id} : get the "id" vthumb.
     *
     * @param id the id of the vthumbDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vthumbDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vthumbs/{id}")
    public ResponseEntity<VthumbDTO> getVthumb(@PathVariable Long id) {
        log.debug("REST request to get Vthumb : {}", id);
        Optional<VthumbDTO> vthumbDTO = vthumbService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vthumbDTO);
    }

    /**
     * {@code DELETE  /vthumbs/:id} : delete the "id" vthumb.
     *
     * @param id the id of the vthumbDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vthumbs/{id}")
    public ResponseEntity<Void> deleteVthumb(@PathVariable Long id) {
        log.debug("REST request to delete Vthumb : {}", id);
        vthumbService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/vthumbs?query=:query} : search for the vthumb corresponding
     * to the query.
     *
     * @param query the query of the vthumb search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/vthumbs")
    public ResponseEntity<List<VthumbDTO>> searchVthumbs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Vthumbs for query {}", query);
        Page<VthumbDTO> page = vthumbService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
