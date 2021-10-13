package com.blocknitive.com.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.repository.ProposalVoteRepository;
import com.blocknitive.com.service.ProposalVoteQueryService;
import com.blocknitive.com.service.ProposalVoteService;
import com.blocknitive.com.service.criteria.ProposalVoteCriteria;
import com.blocknitive.com.service.dto.ProposalVoteDTO;
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
 * REST controller for managing {@link com.blocknitive.com.domain.ProposalVote}.
 */
@RestController
@RequestMapping("/api")
public class ProposalVoteResource {

    private final Logger log = LoggerFactory.getLogger(ProposalVoteResource.class);

    private static final String ENTITY_NAME = "proposalVote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProposalVoteService proposalVoteService;

    private final ProposalVoteRepository proposalVoteRepository;

    private final ProposalVoteQueryService proposalVoteQueryService;

    public ProposalVoteResource(
        ProposalVoteService proposalVoteService,
        ProposalVoteRepository proposalVoteRepository,
        ProposalVoteQueryService proposalVoteQueryService
    ) {
        this.proposalVoteService = proposalVoteService;
        this.proposalVoteRepository = proposalVoteRepository;
        this.proposalVoteQueryService = proposalVoteQueryService;
    }

    /**
     * {@code POST  /proposal-votes} : Create a new proposalVote.
     *
     * @param proposalVoteDTO the proposalVoteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proposalVoteDTO, or with status {@code 400 (Bad Request)} if the proposalVote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proposal-votes")
    public ResponseEntity<ProposalVoteDTO> createProposalVote(@Valid @RequestBody ProposalVoteDTO proposalVoteDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProposalVote : {}", proposalVoteDTO);
        if (proposalVoteDTO.getId() != null) {
            throw new BadRequestAlertException("A new proposalVote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProposalVoteDTO result = proposalVoteService.save(proposalVoteDTO);
        return ResponseEntity
            .created(new URI("/api/proposal-votes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proposal-votes/:id} : Updates an existing proposalVote.
     *
     * @param id the id of the proposalVoteDTO to save.
     * @param proposalVoteDTO the proposalVoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposalVoteDTO,
     * or with status {@code 400 (Bad Request)} if the proposalVoteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proposalVoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proposal-votes/{id}")
    public ResponseEntity<ProposalVoteDTO> updateProposalVote(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProposalVoteDTO proposalVoteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProposalVote : {}, {}", id, proposalVoteDTO);
        if (proposalVoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proposalVoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proposalVoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProposalVoteDTO result = proposalVoteService.save(proposalVoteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proposalVoteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /proposal-votes/:id} : Partial updates given fields of an existing proposalVote, field will ignore if it is null
     *
     * @param id the id of the proposalVoteDTO to save.
     * @param proposalVoteDTO the proposalVoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposalVoteDTO,
     * or with status {@code 400 (Bad Request)} if the proposalVoteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the proposalVoteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the proposalVoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/proposal-votes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProposalVoteDTO> partialUpdateProposalVote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProposalVoteDTO proposalVoteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProposalVote partially : {}, {}", id, proposalVoteDTO);
        if (proposalVoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proposalVoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proposalVoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProposalVoteDTO> result = proposalVoteService.partialUpdate(proposalVoteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proposalVoteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /proposal-votes} : get all the proposalVotes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proposalVotes in body.
     */
    @GetMapping("/proposal-votes")
    public ResponseEntity<List<ProposalVoteDTO>> getAllProposalVotes(ProposalVoteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProposalVotes by criteria: {}", criteria);
        Page<ProposalVoteDTO> page = proposalVoteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /proposal-votes/count} : count all the proposalVotes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/proposal-votes/count")
    public ResponseEntity<Long> countProposalVotes(ProposalVoteCriteria criteria) {
        log.debug("REST request to count ProposalVotes by criteria: {}", criteria);
        return ResponseEntity.ok().body(proposalVoteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /proposal-votes/:id} : get the "id" proposalVote.
     *
     * @param id the id of the proposalVoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proposalVoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proposal-votes/{id}")
    public ResponseEntity<ProposalVoteDTO> getProposalVote(@PathVariable Long id) {
        log.debug("REST request to get ProposalVote : {}", id);
        Optional<ProposalVoteDTO> proposalVoteDTO = proposalVoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(proposalVoteDTO);
    }

    /**
     * {@code DELETE  /proposal-votes/:id} : delete the "id" proposalVote.
     *
     * @param id the id of the proposalVoteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proposal-votes/{id}")
    public ResponseEntity<Void> deleteProposalVote(@PathVariable Long id) {
        log.debug("REST request to delete ProposalVote : {}", id);
        proposalVoteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/proposal-votes?query=:query} : search for the proposalVote corresponding
     * to the query.
     *
     * @param query the query of the proposalVote search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/proposal-votes")
    public ResponseEntity<List<ProposalVoteDTO>> searchProposalVotes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProposalVotes for query {}", query);
        Page<ProposalVoteDTO> page = proposalVoteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
