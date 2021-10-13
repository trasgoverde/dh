package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Proposal;
import com.blocknitive.com.repository.ProposalRepository;
import com.blocknitive.com.repository.search.ProposalSearchRepository;
import com.blocknitive.com.service.ProposalService;
import com.blocknitive.com.service.dto.ProposalDTO;
import com.blocknitive.com.service.mapper.ProposalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Proposal}.
 */
@Service
@Transactional
public class ProposalServiceImpl implements ProposalService {

    private final Logger log = LoggerFactory.getLogger(ProposalServiceImpl.class);

    private final ProposalRepository proposalRepository;

    private final ProposalMapper proposalMapper;

    private final ProposalSearchRepository proposalSearchRepository;

    public ProposalServiceImpl(
        ProposalRepository proposalRepository,
        ProposalMapper proposalMapper,
        ProposalSearchRepository proposalSearchRepository
    ) {
        this.proposalRepository = proposalRepository;
        this.proposalMapper = proposalMapper;
        this.proposalSearchRepository = proposalSearchRepository;
    }

    @Override
    public ProposalDTO save(ProposalDTO proposalDTO) {
        log.debug("Request to save Proposal : {}", proposalDTO);
        Proposal proposal = proposalMapper.toEntity(proposalDTO);
        proposal = proposalRepository.save(proposal);
        ProposalDTO result = proposalMapper.toDto(proposal);
        proposalSearchRepository.save(proposal);
        return result;
    }

    @Override
    public Optional<ProposalDTO> partialUpdate(ProposalDTO proposalDTO) {
        log.debug("Request to partially update Proposal : {}", proposalDTO);

        return proposalRepository
            .findById(proposalDTO.getId())
            .map(existingProposal -> {
                proposalMapper.partialUpdate(existingProposal, proposalDTO);

                return existingProposal;
            })
            .map(proposalRepository::save)
            .map(savedProposal -> {
                proposalSearchRepository.save(savedProposal);

                return savedProposal;
            })
            .map(proposalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProposalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Proposals");
        return proposalRepository.findAll(pageable).map(proposalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProposalDTO> findOne(Long id) {
        log.debug("Request to get Proposal : {}", id);
        return proposalRepository.findById(id).map(proposalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Proposal : {}", id);
        proposalRepository.deleteById(id);
        proposalSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProposalDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Proposals for query {}", query);
        return proposalSearchRepository.search(query, pageable).map(proposalMapper::toDto);
    }
}
