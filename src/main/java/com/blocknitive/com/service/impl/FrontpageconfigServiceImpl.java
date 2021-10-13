package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Frontpageconfig;
import com.blocknitive.com.repository.FrontpageconfigRepository;
import com.blocknitive.com.repository.search.FrontpageconfigSearchRepository;
import com.blocknitive.com.service.FrontpageconfigService;
import com.blocknitive.com.service.dto.FrontpageconfigDTO;
import com.blocknitive.com.service.mapper.FrontpageconfigMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Frontpageconfig}.
 */
@Service
@Transactional
public class FrontpageconfigServiceImpl implements FrontpageconfigService {

    private final Logger log = LoggerFactory.getLogger(FrontpageconfigServiceImpl.class);

    private final FrontpageconfigRepository frontpageconfigRepository;

    private final FrontpageconfigMapper frontpageconfigMapper;

    private final FrontpageconfigSearchRepository frontpageconfigSearchRepository;

    public FrontpageconfigServiceImpl(
        FrontpageconfigRepository frontpageconfigRepository,
        FrontpageconfigMapper frontpageconfigMapper,
        FrontpageconfigSearchRepository frontpageconfigSearchRepository
    ) {
        this.frontpageconfigRepository = frontpageconfigRepository;
        this.frontpageconfigMapper = frontpageconfigMapper;
        this.frontpageconfigSearchRepository = frontpageconfigSearchRepository;
    }

    @Override
    public FrontpageconfigDTO save(FrontpageconfigDTO frontpageconfigDTO) {
        log.debug("Request to save Frontpageconfig : {}", frontpageconfigDTO);
        Frontpageconfig frontpageconfig = frontpageconfigMapper.toEntity(frontpageconfigDTO);
        frontpageconfig = frontpageconfigRepository.save(frontpageconfig);
        FrontpageconfigDTO result = frontpageconfigMapper.toDto(frontpageconfig);
        frontpageconfigSearchRepository.save(frontpageconfig);
        return result;
    }

    @Override
    public Optional<FrontpageconfigDTO> partialUpdate(FrontpageconfigDTO frontpageconfigDTO) {
        log.debug("Request to partially update Frontpageconfig : {}", frontpageconfigDTO);

        return frontpageconfigRepository
            .findById(frontpageconfigDTO.getId())
            .map(existingFrontpageconfig -> {
                frontpageconfigMapper.partialUpdate(existingFrontpageconfig, frontpageconfigDTO);

                return existingFrontpageconfig;
            })
            .map(frontpageconfigRepository::save)
            .map(savedFrontpageconfig -> {
                frontpageconfigSearchRepository.save(savedFrontpageconfig);

                return savedFrontpageconfig;
            })
            .map(frontpageconfigMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FrontpageconfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Frontpageconfigs");
        return frontpageconfigRepository.findAll(pageable).map(frontpageconfigMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FrontpageconfigDTO> findOne(Long id) {
        log.debug("Request to get Frontpageconfig : {}", id);
        return frontpageconfigRepository.findById(id).map(frontpageconfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Frontpageconfig : {}", id);
        frontpageconfigRepository.deleteById(id);
        frontpageconfigSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FrontpageconfigDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Frontpageconfigs for query {}", query);
        return frontpageconfigSearchRepository.search(query, pageable).map(frontpageconfigMapper::toDto);
    }
}
