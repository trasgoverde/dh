package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Urllink;
import com.blocknitive.com.repository.UrllinkRepository;
import com.blocknitive.com.repository.search.UrllinkSearchRepository;
import com.blocknitive.com.service.UrllinkService;
import com.blocknitive.com.service.dto.UrllinkDTO;
import com.blocknitive.com.service.mapper.UrllinkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Urllink}.
 */
@Service
@Transactional
public class UrllinkServiceImpl implements UrllinkService {

    private final Logger log = LoggerFactory.getLogger(UrllinkServiceImpl.class);

    private final UrllinkRepository urllinkRepository;

    private final UrllinkMapper urllinkMapper;

    private final UrllinkSearchRepository urllinkSearchRepository;

    public UrllinkServiceImpl(
        UrllinkRepository urllinkRepository,
        UrllinkMapper urllinkMapper,
        UrllinkSearchRepository urllinkSearchRepository
    ) {
        this.urllinkRepository = urllinkRepository;
        this.urllinkMapper = urllinkMapper;
        this.urllinkSearchRepository = urllinkSearchRepository;
    }

    @Override
    public UrllinkDTO save(UrllinkDTO urllinkDTO) {
        log.debug("Request to save Urllink : {}", urllinkDTO);
        Urllink urllink = urllinkMapper.toEntity(urllinkDTO);
        urllink = urllinkRepository.save(urllink);
        UrllinkDTO result = urllinkMapper.toDto(urllink);
        urllinkSearchRepository.save(urllink);
        return result;
    }

    @Override
    public Optional<UrllinkDTO> partialUpdate(UrllinkDTO urllinkDTO) {
        log.debug("Request to partially update Urllink : {}", urllinkDTO);

        return urllinkRepository
            .findById(urllinkDTO.getId())
            .map(existingUrllink -> {
                urllinkMapper.partialUpdate(existingUrllink, urllinkDTO);

                return existingUrllink;
            })
            .map(urllinkRepository::save)
            .map(savedUrllink -> {
                urllinkSearchRepository.save(savedUrllink);

                return savedUrllink;
            })
            .map(urllinkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UrllinkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Urllinks");
        return urllinkRepository.findAll(pageable).map(urllinkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UrllinkDTO> findOne(Long id) {
        log.debug("Request to get Urllink : {}", id);
        return urllinkRepository.findById(id).map(urllinkMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Urllink : {}", id);
        urllinkRepository.deleteById(id);
        urllinkSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UrllinkDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Urllinks for query {}", query);
        return urllinkSearchRepository.search(query, pageable).map(urllinkMapper::toDto);
    }
}
