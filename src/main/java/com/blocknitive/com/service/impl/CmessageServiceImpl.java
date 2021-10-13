package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Cmessage;
import com.blocknitive.com.repository.CmessageRepository;
import com.blocknitive.com.repository.search.CmessageSearchRepository;
import com.blocknitive.com.service.CmessageService;
import com.blocknitive.com.service.dto.CmessageDTO;
import com.blocknitive.com.service.mapper.CmessageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cmessage}.
 */
@Service
@Transactional
public class CmessageServiceImpl implements CmessageService {

    private final Logger log = LoggerFactory.getLogger(CmessageServiceImpl.class);

    private final CmessageRepository cmessageRepository;

    private final CmessageMapper cmessageMapper;

    private final CmessageSearchRepository cmessageSearchRepository;

    public CmessageServiceImpl(
        CmessageRepository cmessageRepository,
        CmessageMapper cmessageMapper,
        CmessageSearchRepository cmessageSearchRepository
    ) {
        this.cmessageRepository = cmessageRepository;
        this.cmessageMapper = cmessageMapper;
        this.cmessageSearchRepository = cmessageSearchRepository;
    }

    @Override
    public CmessageDTO save(CmessageDTO cmessageDTO) {
        log.debug("Request to save Cmessage : {}", cmessageDTO);
        Cmessage cmessage = cmessageMapper.toEntity(cmessageDTO);
        cmessage = cmessageRepository.save(cmessage);
        CmessageDTO result = cmessageMapper.toDto(cmessage);
        cmessageSearchRepository.save(cmessage);
        return result;
    }

    @Override
    public Optional<CmessageDTO> partialUpdate(CmessageDTO cmessageDTO) {
        log.debug("Request to partially update Cmessage : {}", cmessageDTO);

        return cmessageRepository
            .findById(cmessageDTO.getId())
            .map(existingCmessage -> {
                cmessageMapper.partialUpdate(existingCmessage, cmessageDTO);

                return existingCmessage;
            })
            .map(cmessageRepository::save)
            .map(savedCmessage -> {
                cmessageSearchRepository.save(savedCmessage);

                return savedCmessage;
            })
            .map(cmessageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CmessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cmessages");
        return cmessageRepository.findAll(pageable).map(cmessageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CmessageDTO> findOne(Long id) {
        log.debug("Request to get Cmessage : {}", id);
        return cmessageRepository.findById(id).map(cmessageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cmessage : {}", id);
        cmessageRepository.deleteById(id);
        cmessageSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CmessageDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cmessages for query {}", query);
        return cmessageSearchRepository.search(query, pageable).map(cmessageMapper::toDto);
    }
}
