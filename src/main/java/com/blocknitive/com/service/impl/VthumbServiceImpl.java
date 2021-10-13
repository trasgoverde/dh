package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Vthumb;
import com.blocknitive.com.repository.VthumbRepository;
import com.blocknitive.com.repository.search.VthumbSearchRepository;
import com.blocknitive.com.service.VthumbService;
import com.blocknitive.com.service.dto.VthumbDTO;
import com.blocknitive.com.service.mapper.VthumbMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vthumb}.
 */
@Service
@Transactional
public class VthumbServiceImpl implements VthumbService {

    private final Logger log = LoggerFactory.getLogger(VthumbServiceImpl.class);

    private final VthumbRepository vthumbRepository;

    private final VthumbMapper vthumbMapper;

    private final VthumbSearchRepository vthumbSearchRepository;

    public VthumbServiceImpl(VthumbRepository vthumbRepository, VthumbMapper vthumbMapper, VthumbSearchRepository vthumbSearchRepository) {
        this.vthumbRepository = vthumbRepository;
        this.vthumbMapper = vthumbMapper;
        this.vthumbSearchRepository = vthumbSearchRepository;
    }

    @Override
    public VthumbDTO save(VthumbDTO vthumbDTO) {
        log.debug("Request to save Vthumb : {}", vthumbDTO);
        Vthumb vthumb = vthumbMapper.toEntity(vthumbDTO);
        vthumb = vthumbRepository.save(vthumb);
        VthumbDTO result = vthumbMapper.toDto(vthumb);
        vthumbSearchRepository.save(vthumb);
        return result;
    }

    @Override
    public Optional<VthumbDTO> partialUpdate(VthumbDTO vthumbDTO) {
        log.debug("Request to partially update Vthumb : {}", vthumbDTO);

        return vthumbRepository
            .findById(vthumbDTO.getId())
            .map(existingVthumb -> {
                vthumbMapper.partialUpdate(existingVthumb, vthumbDTO);

                return existingVthumb;
            })
            .map(vthumbRepository::save)
            .map(savedVthumb -> {
                vthumbSearchRepository.save(savedVthumb);

                return savedVthumb;
            })
            .map(vthumbMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VthumbDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vthumbs");
        return vthumbRepository.findAll(pageable).map(vthumbMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VthumbDTO> findOne(Long id) {
        log.debug("Request to get Vthumb : {}", id);
        return vthumbRepository.findById(id).map(vthumbMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vthumb : {}", id);
        vthumbRepository.deleteById(id);
        vthumbSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VthumbDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Vthumbs for query {}", query);
        return vthumbSearchRepository.search(query, pageable).map(vthumbMapper::toDto);
    }
}
