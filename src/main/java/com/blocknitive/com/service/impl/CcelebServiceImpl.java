package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Cceleb;
import com.blocknitive.com.repository.CcelebRepository;
import com.blocknitive.com.repository.search.CcelebSearchRepository;
import com.blocknitive.com.service.CcelebService;
import com.blocknitive.com.service.dto.CcelebDTO;
import com.blocknitive.com.service.mapper.CcelebMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cceleb}.
 */
@Service
@Transactional
public class CcelebServiceImpl implements CcelebService {

    private final Logger log = LoggerFactory.getLogger(CcelebServiceImpl.class);

    private final CcelebRepository ccelebRepository;

    private final CcelebMapper ccelebMapper;

    private final CcelebSearchRepository ccelebSearchRepository;

    public CcelebServiceImpl(CcelebRepository ccelebRepository, CcelebMapper ccelebMapper, CcelebSearchRepository ccelebSearchRepository) {
        this.ccelebRepository = ccelebRepository;
        this.ccelebMapper = ccelebMapper;
        this.ccelebSearchRepository = ccelebSearchRepository;
    }

    @Override
    public CcelebDTO save(CcelebDTO ccelebDTO) {
        log.debug("Request to save Cceleb : {}", ccelebDTO);
        Cceleb cceleb = ccelebMapper.toEntity(ccelebDTO);
        cceleb = ccelebRepository.save(cceleb);
        CcelebDTO result = ccelebMapper.toDto(cceleb);
        ccelebSearchRepository.save(cceleb);
        return result;
    }

    @Override
    public Optional<CcelebDTO> partialUpdate(CcelebDTO ccelebDTO) {
        log.debug("Request to partially update Cceleb : {}", ccelebDTO);

        return ccelebRepository
            .findById(ccelebDTO.getId())
            .map(existingCceleb -> {
                ccelebMapper.partialUpdate(existingCceleb, ccelebDTO);

                return existingCceleb;
            })
            .map(ccelebRepository::save)
            .map(savedCceleb -> {
                ccelebSearchRepository.save(savedCceleb);

                return savedCceleb;
            })
            .map(ccelebMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CcelebDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ccelebs");
        return ccelebRepository.findAll(pageable).map(ccelebMapper::toDto);
    }

    public Page<CcelebDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ccelebRepository.findAllWithEagerRelationships(pageable).map(ccelebMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CcelebDTO> findOne(Long id) {
        log.debug("Request to get Cceleb : {}", id);
        return ccelebRepository.findOneWithEagerRelationships(id).map(ccelebMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cceleb : {}", id);
        ccelebRepository.deleteById(id);
        ccelebSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CcelebDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ccelebs for query {}", query);
        return ccelebSearchRepository.search(query, pageable).map(ccelebMapper::toDto);
    }
}
