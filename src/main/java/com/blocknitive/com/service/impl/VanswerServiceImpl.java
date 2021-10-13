package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Vanswer;
import com.blocknitive.com.repository.VanswerRepository;
import com.blocknitive.com.repository.search.VanswerSearchRepository;
import com.blocknitive.com.service.VanswerService;
import com.blocknitive.com.service.dto.VanswerDTO;
import com.blocknitive.com.service.mapper.VanswerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vanswer}.
 */
@Service
@Transactional
public class VanswerServiceImpl implements VanswerService {

    private final Logger log = LoggerFactory.getLogger(VanswerServiceImpl.class);

    private final VanswerRepository vanswerRepository;

    private final VanswerMapper vanswerMapper;

    private final VanswerSearchRepository vanswerSearchRepository;

    public VanswerServiceImpl(
        VanswerRepository vanswerRepository,
        VanswerMapper vanswerMapper,
        VanswerSearchRepository vanswerSearchRepository
    ) {
        this.vanswerRepository = vanswerRepository;
        this.vanswerMapper = vanswerMapper;
        this.vanswerSearchRepository = vanswerSearchRepository;
    }

    @Override
    public VanswerDTO save(VanswerDTO vanswerDTO) {
        log.debug("Request to save Vanswer : {}", vanswerDTO);
        Vanswer vanswer = vanswerMapper.toEntity(vanswerDTO);
        vanswer = vanswerRepository.save(vanswer);
        VanswerDTO result = vanswerMapper.toDto(vanswer);
        vanswerSearchRepository.save(vanswer);
        return result;
    }

    @Override
    public Optional<VanswerDTO> partialUpdate(VanswerDTO vanswerDTO) {
        log.debug("Request to partially update Vanswer : {}", vanswerDTO);

        return vanswerRepository
            .findById(vanswerDTO.getId())
            .map(existingVanswer -> {
                vanswerMapper.partialUpdate(existingVanswer, vanswerDTO);

                return existingVanswer;
            })
            .map(vanswerRepository::save)
            .map(savedVanswer -> {
                vanswerSearchRepository.save(savedVanswer);

                return savedVanswer;
            })
            .map(vanswerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VanswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vanswers");
        return vanswerRepository.findAll(pageable).map(vanswerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VanswerDTO> findOne(Long id) {
        log.debug("Request to get Vanswer : {}", id);
        return vanswerRepository.findById(id).map(vanswerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vanswer : {}", id);
        vanswerRepository.deleteById(id);
        vanswerSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VanswerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Vanswers for query {}", query);
        return vanswerSearchRepository.search(query, pageable).map(vanswerMapper::toDto);
    }
}
