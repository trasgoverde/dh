package com.blocknitive.com.service;

import com.blocknitive.com.service.dto.ProposalVoteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blocknitive.com.domain.ProposalVote}.
 */
public interface ProposalVoteService {
    /**
     * Save a proposalVote.
     *
     * @param proposalVoteDTO the entity to save.
     * @return the persisted entity.
     */
    ProposalVoteDTO save(ProposalVoteDTO proposalVoteDTO);

    /**
     * Partially updates a proposalVote.
     *
     * @param proposalVoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProposalVoteDTO> partialUpdate(ProposalVoteDTO proposalVoteDTO);

    /**
     * Get all the proposalVotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProposalVoteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" proposalVote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProposalVoteDTO> findOne(Long id);

    /**
     * Delete the "id" proposalVote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the proposalVote corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProposalVoteDTO> search(String query, Pageable pageable);
}
