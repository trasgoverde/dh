package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Calbum;
import com.blocknitive.com.repository.CalbumRepository;
import com.blocknitive.com.repository.search.CalbumSearchRepository;
import com.blocknitive.com.service.CalbumService;
import com.blocknitive.com.service.dto.CalbumDTO;
import com.blocknitive.com.service.mapper.CalbumMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Calbum}.
 */
@Service
@Transactional
public class CalbumServiceImpl implements CalbumService {

    private final Logger log = LoggerFactory.getLogger(CalbumServiceImpl.class);

    private final CalbumRepository calbumRepository;

    private final CalbumMapper calbumMapper;

    private final CalbumSearchRepository calbumSearchRepository;

    public CalbumServiceImpl(CalbumRepository calbumRepository, CalbumMapper calbumMapper, CalbumSearchRepository calbumSearchRepository) {
        this.calbumRepository = calbumRepository;
        this.calbumMapper = calbumMapper;
        this.calbumSearchRepository = calbumSearchRepository;
    }

    @Override
    public CalbumDTO save(CalbumDTO calbumDTO) {
        log.debug("Request to save Calbum : {}", calbumDTO);
        Calbum calbum = calbumMapper.toEntity(calbumDTO);
        calbum = calbumRepository.save(calbum);
        CalbumDTO result = calbumMapper.toDto(calbum);
        calbumSearchRepository.save(calbum);
        return result;
    }

    @Override
    public Optional<CalbumDTO> partialUpdate(CalbumDTO calbumDTO) {
        log.debug("Request to partially update Calbum : {}", calbumDTO);

        return calbumRepository
            .findById(calbumDTO.getId())
            .map(existingCalbum -> {
                calbumMapper.partialUpdate(existingCalbum, calbumDTO);

                return existingCalbum;
            })
            .map(calbumRepository::save)
            .map(savedCalbum -> {
                calbumSearchRepository.save(savedCalbum);

                return savedCalbum;
            })
            .map(calbumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CalbumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Calbums");
        return calbumRepository.findAll(pageable).map(calbumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CalbumDTO> findOne(Long id) {
        log.debug("Request to get Calbum : {}", id);
        return calbumRepository.findById(id).map(calbumMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Calbum : {}", id);
        calbumRepository.deleteById(id);
        calbumSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CalbumDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Calbums for query {}", query);
        return calbumSearchRepository.search(query, pageable).map(calbumMapper::toDto);
    }
}
