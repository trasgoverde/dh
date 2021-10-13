package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Community;
import com.blocknitive.com.repository.CommunityRepository;
import com.blocknitive.com.repository.search.CommunitySearchRepository;
import com.blocknitive.com.service.CommunityService;
import com.blocknitive.com.service.dto.CommunityDTO;
import com.blocknitive.com.service.mapper.CommunityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Community}.
 */
@Service
@Transactional
public class CommunityServiceImpl implements CommunityService {

    private final Logger log = LoggerFactory.getLogger(CommunityServiceImpl.class);

    private final CommunityRepository communityRepository;

    private final CommunityMapper communityMapper;

    private final CommunitySearchRepository communitySearchRepository;

    public CommunityServiceImpl(
        CommunityRepository communityRepository,
        CommunityMapper communityMapper,
        CommunitySearchRepository communitySearchRepository
    ) {
        this.communityRepository = communityRepository;
        this.communityMapper = communityMapper;
        this.communitySearchRepository = communitySearchRepository;
    }

    @Override
    public CommunityDTO save(CommunityDTO communityDTO) {
        log.debug("Request to save Community : {}", communityDTO);
        Community community = communityMapper.toEntity(communityDTO);
        community = communityRepository.save(community);
        CommunityDTO result = communityMapper.toDto(community);
        communitySearchRepository.save(community);
        return result;
    }

    @Override
    public Optional<CommunityDTO> partialUpdate(CommunityDTO communityDTO) {
        log.debug("Request to partially update Community : {}", communityDTO);

        return communityRepository
            .findById(communityDTO.getId())
            .map(existingCommunity -> {
                communityMapper.partialUpdate(existingCommunity, communityDTO);

                return existingCommunity;
            })
            .map(communityRepository::save)
            .map(savedCommunity -> {
                communitySearchRepository.save(savedCommunity);

                return savedCommunity;
            })
            .map(communityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommunityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Communities");
        return communityRepository.findAll(pageable).map(communityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommunityDTO> findOne(Long id) {
        log.debug("Request to get Community : {}", id);
        return communityRepository.findById(id).map(communityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Community : {}", id);
        communityRepository.deleteById(id);
        communitySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommunityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Communities for query {}", query);
        return communitySearchRepository.search(query, pageable).map(communityMapper::toDto);
    }
}
