package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.ProposalVote;
import com.blocknitive.com.repository.ProposalVoteRepository;
import com.blocknitive.com.repository.search.ProposalVoteSearchRepository;
import com.blocknitive.com.service.ProposalVoteService;
import com.blocknitive.com.service.dto.ProposalVoteDTO;
import com.blocknitive.com.service.mapper.ProposalVoteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProposalVote}.
 */
@Service
@Transactional
public class ProposalVoteServiceImpl implements ProposalVoteService {

    private final Logger log = LoggerFactory.getLogger(ProposalVoteServiceImpl.class);

    private final ProposalVoteRepository proposalVoteRepository;

    private final ProposalVoteMapper proposalVoteMapper;

    private final ProposalVoteSearchRepository proposalVoteSearchRepository;

    public ProposalVoteServiceImpl(
        ProposalVoteRepository proposalVoteRepository,
        ProposalVoteMapper proposalVoteMapper,
        ProposalVoteSearchRepository proposalVoteSearchRepository
    ) {
        this.proposalVoteRepository = proposalVoteRepository;
        this.proposalVoteMapper = proposalVoteMapper;
        this.proposalVoteSearchRepository = proposalVoteSearchRepository;
    }

    @Override
    public ProposalVoteDTO save(ProposalVoteDTO proposalVoteDTO) {
        log.debug("Request to save ProposalVote : {}", proposalVoteDTO);
        ProposalVote proposalVote = proposalVoteMapper.toEntity(proposalVoteDTO);
        proposalVote = proposalVoteRepository.save(proposalVote);
        ProposalVoteDTO result = proposalVoteMapper.toDto(proposalVote);
        proposalVoteSearchRepository.save(proposalVote);
        return result;
    }

    @Override
    public Optional<ProposalVoteDTO> partialUpdate(ProposalVoteDTO proposalVoteDTO) {
        log.debug("Request to partially update ProposalVote : {}", proposalVoteDTO);

        return proposalVoteRepository
            .findById(proposalVoteDTO.getId())
            .map(existingProposalVote -> {
                proposalVoteMapper.partialUpdate(existingProposalVote, proposalVoteDTO);

                return existingProposalVote;
            })
            .map(proposalVoteRepository::save)
            .map(savedProposalVote -> {
                proposalVoteSearchRepository.save(savedProposalVote);

                return savedProposalVote;
            })
            .map(proposalVoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProposalVoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProposalVotes");
        return proposalVoteRepository.findAll(pageable).map(proposalVoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProposalVoteDTO> findOne(Long id) {
        log.debug("Request to get ProposalVote : {}", id);
        return proposalVoteRepository.findById(id).map(proposalVoteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProposalVote : {}", id);
        proposalVoteRepository.deleteById(id);
        proposalVoteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProposalVoteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProposalVotes for query {}", query);
        return proposalVoteSearchRepository.search(query, pageable).map(proposalVoteMapper::toDto);
    }
}
