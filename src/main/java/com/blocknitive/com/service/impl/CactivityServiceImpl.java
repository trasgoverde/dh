package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Cactivity;
import com.blocknitive.com.repository.CactivityRepository;
import com.blocknitive.com.repository.search.CactivitySearchRepository;
import com.blocknitive.com.service.CactivityService;
import com.blocknitive.com.service.dto.CactivityDTO;
import com.blocknitive.com.service.mapper.CactivityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cactivity}.
 */
@Service
@Transactional
public class CactivityServiceImpl implements CactivityService {

    private final Logger log = LoggerFactory.getLogger(CactivityServiceImpl.class);

    private final CactivityRepository cactivityRepository;

    private final CactivityMapper cactivityMapper;

    private final CactivitySearchRepository cactivitySearchRepository;

    public CactivityServiceImpl(
        CactivityRepository cactivityRepository,
        CactivityMapper cactivityMapper,
        CactivitySearchRepository cactivitySearchRepository
    ) {
        this.cactivityRepository = cactivityRepository;
        this.cactivityMapper = cactivityMapper;
        this.cactivitySearchRepository = cactivitySearchRepository;
    }

    @Override
    public CactivityDTO save(CactivityDTO cactivityDTO) {
        log.debug("Request to save Cactivity : {}", cactivityDTO);
        Cactivity cactivity = cactivityMapper.toEntity(cactivityDTO);
        cactivity = cactivityRepository.save(cactivity);
        CactivityDTO result = cactivityMapper.toDto(cactivity);
        cactivitySearchRepository.save(cactivity);
        return result;
    }

    @Override
    public Optional<CactivityDTO> partialUpdate(CactivityDTO cactivityDTO) {
        log.debug("Request to partially update Cactivity : {}", cactivityDTO);

        return cactivityRepository
            .findById(cactivityDTO.getId())
            .map(existingCactivity -> {
                cactivityMapper.partialUpdate(existingCactivity, cactivityDTO);

                return existingCactivity;
            })
            .map(cactivityRepository::save)
            .map(savedCactivity -> {
                cactivitySearchRepository.save(savedCactivity);

                return savedCactivity;
            })
            .map(cactivityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CactivityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cactivities");
        return cactivityRepository.findAll(pageable).map(cactivityMapper::toDto);
    }

    public Page<CactivityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cactivityRepository.findAllWithEagerRelationships(pageable).map(cactivityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CactivityDTO> findOne(Long id) {
        log.debug("Request to get Cactivity : {}", id);
        return cactivityRepository.findOneWithEagerRelationships(id).map(cactivityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cactivity : {}", id);
        cactivityRepository.deleteById(id);
        cactivitySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CactivityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cactivities for query {}", query);
        return cactivitySearchRepository.search(query, pageable).map(cactivityMapper::toDto);
    }
}
