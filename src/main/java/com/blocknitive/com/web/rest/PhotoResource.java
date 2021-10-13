package com.blocknitive.com.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.repository.PhotoRepository;
import com.blocknitive.com.service.PhotoQueryService;
import com.blocknitive.com.service.PhotoService;
import com.blocknitive.com.service.criteria.PhotoCriteria;
import com.blocknitive.com.service.dto.PhotoDTO;
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
 * REST controller for managing {@link com.blocknitive.com.domain.Photo}.
 */
@RestController
@RequestMapping("/api")
public class PhotoResource {

    private final Logger log = LoggerFactory.getLogger(PhotoResource.class);

    private static final String ENTITY_NAME = "photo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhotoService photoService;

    private final PhotoRepository photoRepository;

    private final PhotoQueryService photoQueryService;

    public PhotoResource(PhotoService photoService, PhotoRepository photoRepository, PhotoQueryService photoQueryService) {
        this.photoService = photoService;
        this.photoRepository = photoRepository;
        this.photoQueryService = photoQueryService;
    }

    /**
     * {@code POST  /photos} : Create a new photo.
     *
     * @param photoDTO the photoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new photoDTO, or with status {@code 400 (Bad Request)} if the photo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/photos")
    public ResponseEntity<PhotoDTO> createPhoto(@Valid @RequestBody PhotoDTO photoDTO) throws URISyntaxException {
        log.debug("REST request to save Photo : {}", photoDTO);
        if (photoDTO.getId() != null) {
            throw new BadRequestAlertException("A new photo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhotoDTO result = photoService.save(photoDTO);
        return ResponseEntity
            .created(new URI("/api/photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /photos/:id} : Updates an existing photo.
     *
     * @param id the id of the photoDTO to save.
     * @param photoDTO the photoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated photoDTO,
     * or with status {@code 400 (Bad Request)} if the photoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the photoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/photos/{id}")
    public ResponseEntity<PhotoDTO> updatePhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PhotoDTO photoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Photo : {}, {}", id, photoDTO);
        if (photoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, photoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!photoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PhotoDTO result = photoService.save(photoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, photoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /photos/:id} : Partial updates given fields of an existing photo, field will ignore if it is null
     *
     * @param id the id of the photoDTO to save.
     * @param photoDTO the photoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated photoDTO,
     * or with status {@code 400 (Bad Request)} if the photoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the photoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the photoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/photos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhotoDTO> partialUpdatePhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PhotoDTO photoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Photo partially : {}, {}", id, photoDTO);
        if (photoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, photoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!photoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhotoDTO> result = photoService.partialUpdate(photoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, photoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /photos} : get all the photos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of photos in body.
     */
    @GetMapping("/photos")
    public ResponseEntity<List<PhotoDTO>> getAllPhotos(PhotoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Photos by criteria: {}", criteria);
        Page<PhotoDTO> page = photoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /photos/count} : count all the photos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/photos/count")
    public ResponseEntity<Long> countPhotos(PhotoCriteria criteria) {
        log.debug("REST request to count Photos by criteria: {}", criteria);
        return ResponseEntity.ok().body(photoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /photos/:id} : get the "id" photo.
     *
     * @param id the id of the photoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the photoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/photos/{id}")
    public ResponseEntity<PhotoDTO> getPhoto(@PathVariable Long id) {
        log.debug("REST request to get Photo : {}", id);
        Optional<PhotoDTO> photoDTO = photoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(photoDTO);
    }

    /**
     * {@code DELETE  /photos/:id} : delete the "id" photo.
     *
     * @param id the id of the photoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/photos/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        log.debug("REST request to delete Photo : {}", id);
        photoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/photos?query=:query} : search for the photo corresponding
     * to the query.
     *
     * @param query the query of the photo search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/photos")
    public ResponseEntity<List<PhotoDTO>> searchPhotos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Photos for query {}", query);
        Page<PhotoDTO> page = photoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
