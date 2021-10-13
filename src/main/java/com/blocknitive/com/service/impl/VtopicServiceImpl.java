package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Vtopic;
import com.blocknitive.com.repository.VtopicRepository;
import com.blocknitive.com.repository.search.VtopicSearchRepository;
import com.blocknitive.com.service.VtopicService;
import com.blocknitive.com.service.dto.VtopicDTO;
import com.blocknitive.com.service.mapper.VtopicMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vtopic}.
 */
@Service
@Transactional
public class VtopicServiceImpl implements VtopicService {

    private final Logger log = LoggerFactory.getLogger(VtopicServiceImpl.class);

    private final VtopicRepository vtopicRepository;

    private final VtopicMapper vtopicMapper;

    private final VtopicSearchRepository vtopicSearchRepository;

    public VtopicServiceImpl(VtopicRepository vtopicRepository, VtopicMapper vtopicMapper, VtopicSearchRepository vtopicSearchRepository) {
        this.vtopicRepository = vtopicRepository;
        this.vtopicMapper = vtopicMapper;
        this.vtopicSearchRepository = vtopicSearchRepository;
    }

    @Override
    public VtopicDTO save(VtopicDTO vtopicDTO) {
        log.debug("Request to save Vtopic : {}", vtopicDTO);
        Vtopic vtopic = vtopicMapper.toEntity(vtopicDTO);
        vtopic = vtopicRepository.save(vtopic);
        VtopicDTO result = vtopicMapper.toDto(vtopic);
        vtopicSearchRepository.save(vtopic);
        return result;
    }

    @Override
    public Optional<VtopicDTO> partialUpdate(VtopicDTO vtopicDTO) {
        log.debug("Request to partially update Vtopic : {}", vtopicDTO);

        return vtopicRepository
            .findById(vtopicDTO.getId())
            .map(existingVtopic -> {
                vtopicMapper.partialUpdate(existingVtopic, vtopicDTO);

                return existingVtopic;
            })
            .map(vtopicRepository::save)
            .map(savedVtopic -> {
                vtopicSearchRepository.save(savedVtopic);

                return savedVtopic;
            })
            .map(vtopicMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VtopicDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vtopics");
        return vtopicRepository.findAll(pageable).map(vtopicMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VtopicDTO> findOne(Long id) {
        log.debug("Request to get Vtopic : {}", id);
        return vtopicRepository.findById(id).map(vtopicMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vtopic : {}", id);
        vtopicRepository.deleteById(id);
        vtopicSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VtopicDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Vtopics for query {}", query);
        return vtopicSearchRepository.search(query, pageable).map(vtopicMapper::toDto);
    }
}
