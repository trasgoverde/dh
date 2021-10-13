package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Vquestion;
import com.blocknitive.com.repository.VquestionRepository;
import com.blocknitive.com.repository.search.VquestionSearchRepository;
import com.blocknitive.com.service.VquestionService;
import com.blocknitive.com.service.dto.VquestionDTO;
import com.blocknitive.com.service.mapper.VquestionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vquestion}.
 */
@Service
@Transactional
public class VquestionServiceImpl implements VquestionService {

    private final Logger log = LoggerFactory.getLogger(VquestionServiceImpl.class);

    private final VquestionRepository vquestionRepository;

    private final VquestionMapper vquestionMapper;

    private final VquestionSearchRepository vquestionSearchRepository;

    public VquestionServiceImpl(
        VquestionRepository vquestionRepository,
        VquestionMapper vquestionMapper,
        VquestionSearchRepository vquestionSearchRepository
    ) {
        this.vquestionRepository = vquestionRepository;
        this.vquestionMapper = vquestionMapper;
        this.vquestionSearchRepository = vquestionSearchRepository;
    }

    @Override
    public VquestionDTO save(VquestionDTO vquestionDTO) {
        log.debug("Request to save Vquestion : {}", vquestionDTO);
        Vquestion vquestion = vquestionMapper.toEntity(vquestionDTO);
        vquestion = vquestionRepository.save(vquestion);
        VquestionDTO result = vquestionMapper.toDto(vquestion);
        vquestionSearchRepository.save(vquestion);
        return result;
    }

    @Override
    public Optional<VquestionDTO> partialUpdate(VquestionDTO vquestionDTO) {
        log.debug("Request to partially update Vquestion : {}", vquestionDTO);

        return vquestionRepository
            .findById(vquestionDTO.getId())
            .map(existingVquestion -> {
                vquestionMapper.partialUpdate(existingVquestion, vquestionDTO);

                return existingVquestion;
            })
            .map(vquestionRepository::save)
            .map(savedVquestion -> {
                vquestionSearchRepository.save(savedVquestion);

                return savedVquestion;
            })
            .map(vquestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VquestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vquestions");
        return vquestionRepository.findAll(pageable).map(vquestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VquestionDTO> findOne(Long id) {
        log.debug("Request to get Vquestion : {}", id);
        return vquestionRepository.findById(id).map(vquestionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vquestion : {}", id);
        vquestionRepository.deleteById(id);
        vquestionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VquestionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Vquestions for query {}", query);
        return vquestionSearchRepository.search(query, pageable).map(vquestionMapper::toDto);
    }
}
