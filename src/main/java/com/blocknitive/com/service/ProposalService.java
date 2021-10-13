package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.ProposalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.Proposal}.
 */
public interface ProposalService {
    /**
     * Save a proposal.
     *
     * @param proposalDTO the entity to save.
     * @return the persisted entity.
     */
    ProposalDTO save(ProposalDTO proposalDTO);

    /**
     * Partially updates a proposal.
     *
     * @param proposalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProposalDTO> partialUpdate(ProposalDTO proposalDTO);

    /**
     * Get all the proposals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProposalDTO> findAll(Pageable pageable);

    /**
     * Get the "id" proposal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProposalDTO> findOne(Long id);

    /**
     * Delete the "id" proposal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the proposal corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProposalDTO> search(String query, Pageable pageable);
}
