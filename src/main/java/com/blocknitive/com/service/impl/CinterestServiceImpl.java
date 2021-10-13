package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Cinterest;
import com.blocknitive.com.repository.CinterestRepository;
import com.blocknitive.com.repository.search.CinterestSearchRepository;
import com.blocknitive.com.service.CinterestService;
import com.blocknitive.com.service.dto.CinterestDTO;
import com.blocknitive.com.service.mapper.CinterestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cinterest}.
 */
@Service
@Transactional
public class CinterestServiceImpl implements CinterestService {

    private final Logger log = LoggerFactory.getLogger(CinterestServiceImpl.class);

    private final CinterestRepository cinterestRepository;

    private final CinterestMapper cinterestMapper;

    private final CinterestSearchRepository cinterestSearchRepository;

    public CinterestServiceImpl(
        CinterestRepository cinterestRepository,
        CinterestMapper cinterestMapper,
        CinterestSearchRepository cinterestSearchRepository
    ) {
        this.cinterestRepository = cinterestRepository;
        this.cinterestMapper = cinterestMapper;
        this.cinterestSearchRepository = cinterestSearchRepository;
    }

    @Override
    public CinterestDTO save(CinterestDTO cinterestDTO) {
        log.debug("Request to save Cinterest : {}", cinterestDTO);
        Cinterest cinterest = cinterestMapper.toEntity(cinterestDTO);
        cinterest = cinterestRepository.save(cinterest);
        CinterestDTO result = cinterestMapper.toDto(cinterest);
        cinterestSearchRepository.save(cinterest);
        return result;
    }

    @Override
    public Optional<CinterestDTO> partialUpdate(CinterestDTO cinterestDTO) {
        log.debug("Request to partially update Cinterest : {}", cinterestDTO);

        return cinterestRepository
            .findById(cinterestDTO.getId())
            .map(existingCinterest -> {
                cinterestMapper.partialUpdate(existingCinterest, cinterestDTO);

                return existingCinterest;
            })
            .map(cinterestRepository::save)
            .map(savedCinterest -> {
                cinterestSearchRepository.save(savedCinterest);

                return savedCinterest;
            })
            .map(cinterestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CinterestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cinterests");
        return cinterestRepository.findAll(pageable).map(cinterestMapper::toDto);
    }

    public Page<CinterestDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cinterestRepository.findAllWithEagerRelationships(pageable).map(cinterestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CinterestDTO> findOne(Long id) {
        log.debug("Request to get Cinterest : {}", id);
        return cinterestRepository.findOneWithEagerRelationships(id).map(cinterestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cinterest : {}", id);
        cinterestRepository.deleteById(id);
        cinterestSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CinterestDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cinterests for query {}", query);
        return cinterestSearchRepository.search(query, pageable).map(cinterestMapper::toDto);
    }
}
