package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Appprofile;
import com.blocknitive.com.repository.AppprofileRepository;
import com.blocknitive.com.repository.search.AppprofileSearchRepository;
import com.blocknitive.com.service.AppprofileService;
import com.blocknitive.com.service.dto.AppprofileDTO;
import com.blocknitive.com.service.mapper.AppprofileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Appprofile}.
 */
@Service
@Transactional
public class AppprofileServiceImpl implements AppprofileService {

    private final Logger log = LoggerFactory.getLogger(AppprofileServiceImpl.class);

    private final AppprofileRepository appprofileRepository;

    private final AppprofileMapper appprofileMapper;

    private final AppprofileSearchRepository appprofileSearchRepository;

    public AppprofileServiceImpl(
        AppprofileRepository appprofileRepository,
        AppprofileMapper appprofileMapper,
        AppprofileSearchRepository appprofileSearchRepository
    ) {
        this.appprofileRepository = appprofileRepository;
        this.appprofileMapper = appprofileMapper;
        this.appprofileSearchRepository = appprofileSearchRepository;
    }

    @Override
    public AppprofileDTO save(AppprofileDTO appprofileDTO) {
        log.debug("Request to save Appprofile : {}", appprofileDTO);
        Appprofile appprofile = appprofileMapper.toEntity(appprofileDTO);
        appprofile = appprofileRepository.save(appprofile);
        AppprofileDTO result = appprofileMapper.toDto(appprofile);
        appprofileSearchRepository.save(appprofile);
        return result;
    }

    @Override
    public Optional<AppprofileDTO> partialUpdate(AppprofileDTO appprofileDTO) {
        log.debug("Request to partially update Appprofile : {}", appprofileDTO);

        return appprofileRepository
            .findById(appprofileDTO.getId())
            .map(existingAppprofile -> {
                appprofileMapper.partialUpdate(existingAppprofile, appprofileDTO);

                return existingAppprofile;
            })
            .map(appprofileRepository::save)
            .map(savedAppprofile -> {
                appprofileSearchRepository.save(savedAppprofile);

                return savedAppprofile;
            })
            .map(appprofileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppprofileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Appprofiles");
        return appprofileRepository.findAll(pageable).map(appprofileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppprofileDTO> findOne(Long id) {
        log.debug("Request to get Appprofile : {}", id);
        return appprofileRepository.findById(id).map(appprofileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Appprofile : {}", id);
        appprofileRepository.deleteById(id);
        appprofileSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppprofileDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Appprofiles for query {}", query);
        return appprofileSearchRepository.search(query, pageable).map(appprofileMapper::toDto);
    }
}
