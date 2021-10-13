package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Celeb;
import com.blocknitive.com.repository.CelebRepository;
import com.blocknitive.com.repository.search.CelebSearchRepository;
import com.blocknitive.com.service.CelebService;
import com.blocknitive.com.service.dto.CelebDTO;
import com.blocknitive.com.service.mapper.CelebMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Celeb}.
 */
@Service
@Transactional
public class CelebServiceImpl implements CelebService {

    private final Logger log = LoggerFactory.getLogger(CelebServiceImpl.class);

    private final CelebRepository celebRepository;

    private final CelebMapper celebMapper;

    private final CelebSearchRepository celebSearchRepository;

    public CelebServiceImpl(CelebRepository celebRepository, CelebMapper celebMapper, CelebSearchRepository celebSearchRepository) {
        this.celebRepository = celebRepository;
        this.celebMapper = celebMapper;
        this.celebSearchRepository = celebSearchRepository;
    }

    @Override
    public CelebDTO save(CelebDTO celebDTO) {
        log.debug("Request to save Celeb : {}", celebDTO);
        Celeb celeb = celebMapper.toEntity(celebDTO);
        celeb = celebRepository.save(celeb);
        CelebDTO result = celebMapper.toDto(celeb);
        celebSearchRepository.save(celeb);
        return result;
    }

    @Override
    public Optional<CelebDTO> partialUpdate(CelebDTO celebDTO) {
        log.debug("Request to partially update Celeb : {}", celebDTO);

        return celebRepository
            .findById(celebDTO.getId())
            .map(existingCeleb -> {
                celebMapper.partialUpdate(existingCeleb, celebDTO);

                return existingCeleb;
            })
            .map(celebRepository::save)
            .map(savedCeleb -> {
                celebSearchRepository.save(savedCeleb);

                return savedCeleb;
            })
            .map(celebMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CelebDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Celebs");
        return celebRepository.findAll(pageable).map(celebMapper::toDto);
    }

    public Page<CelebDTO> findAllWithEagerRelationships(Pageable pageable) {
        return celebRepository.findAllWithEagerRelationships(pageable).map(celebMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CelebDTO> findOne(Long id) {
        log.debug("Request to get Celeb : {}", id);
        return celebRepository.findOneWithEagerRelationships(id).map(celebMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Celeb : {}", id);
        celebRepository.deleteById(id);
        celebSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CelebDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Celebs for query {}", query);
        return celebSearchRepository.search(query, pageable).map(celebMapper::toDto);
    }
}
