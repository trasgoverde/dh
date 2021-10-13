package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Follow;
import com.blocknitive.com.repository.FollowRepository;
import com.blocknitive.com.repository.search.FollowSearchRepository;
import com.blocknitive.com.service.FollowService;
import com.blocknitive.com.service.dto.FollowDTO;
import com.blocknitive.com.service.mapper.FollowMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Follow}.
 */
@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    private final Logger log = LoggerFactory.getLogger(FollowServiceImpl.class);

    private final FollowRepository followRepository;

    private final FollowMapper followMapper;

    private final FollowSearchRepository followSearchRepository;

    public FollowServiceImpl(FollowRepository followRepository, FollowMapper followMapper, FollowSearchRepository followSearchRepository) {
        this.followRepository = followRepository;
        this.followMapper = followMapper;
        this.followSearchRepository = followSearchRepository;
    }

    @Override
    public FollowDTO save(FollowDTO followDTO) {
        log.debug("Request to save Follow : {}", followDTO);
        Follow follow = followMapper.toEntity(followDTO);
        follow = followRepository.save(follow);
        FollowDTO result = followMapper.toDto(follow);
        followSearchRepository.save(follow);
        return result;
    }

    @Override
    public Optional<FollowDTO> partialUpdate(FollowDTO followDTO) {
        log.debug("Request to partially update Follow : {}", followDTO);

        return followRepository
            .findById(followDTO.getId())
            .map(existingFollow -> {
                followMapper.partialUpdate(existingFollow, followDTO);

                return existingFollow;
            })
            .map(followRepository::save)
            .map(savedFollow -> {
                followSearchRepository.save(savedFollow);

                return savedFollow;
            })
            .map(followMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Follows");
        return followRepository.findAll(pageable).map(followMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FollowDTO> findOne(Long id) {
        log.debug("Request to get Follow : {}", id);
        return followRepository.findById(id).map(followMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Follow : {}", id);
        followRepository.deleteById(id);
        followSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Follows for query {}", query);
        return followSearchRepository.search(query, pageable).map(followMapper::toDto);
    }
}
