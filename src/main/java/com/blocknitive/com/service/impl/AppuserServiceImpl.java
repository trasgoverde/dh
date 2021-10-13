package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Appuser;
import com.blocknitive.com.repository.AppuserRepository;
import com.blocknitive.com.repository.search.AppuserSearchRepository;
import com.blocknitive.com.service.AppuserService;
import com.blocknitive.com.service.dto.AppuserDTO;
import com.blocknitive.com.service.mapper.AppuserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Appuser}.
 */
@Service
@Transactional
public class AppuserServiceImpl implements AppuserService {

    private final Logger log = LoggerFactory.getLogger(AppuserServiceImpl.class);

    private final AppuserRepository appuserRepository;

    private final AppuserMapper appuserMapper;

    private final AppuserSearchRepository appuserSearchRepository;

    public AppuserServiceImpl(
        AppuserRepository appuserRepository,
        AppuserMapper appuserMapper,
        AppuserSearchRepository appuserSearchRepository
    ) {
        this.appuserRepository = appuserRepository;
        this.appuserMapper = appuserMapper;
        this.appuserSearchRepository = appuserSearchRepository;
    }

    @Override
    public AppuserDTO save(AppuserDTO appuserDTO) {
        log.debug("Request to save Appuser : {}", appuserDTO);
        Appuser appuser = appuserMapper.toEntity(appuserDTO);
        appuser = appuserRepository.save(appuser);
        AppuserDTO result = appuserMapper.toDto(appuser);
        appuserSearchRepository.save(appuser);
        return result;
    }

    @Override
    public Optional<AppuserDTO> partialUpdate(AppuserDTO appuserDTO) {
        log.debug("Request to partially update Appuser : {}", appuserDTO);

        return appuserRepository
            .findById(appuserDTO.getId())
            .map(existingAppuser -> {
                appuserMapper.partialUpdate(existingAppuser, appuserDTO);

                return existingAppuser;
            })
            .map(appuserRepository::save)
            .map(savedAppuser -> {
                appuserSearchRepository.save(savedAppuser);

                return savedAppuser;
            })
            .map(appuserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppuserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Appusers");
        return appuserRepository.findAll(pageable).map(appuserMapper::toDto);
    }

    /**
     *  Get all the appusers where Appprofile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppuserDTO> findAllWhereAppprofileIsNull() {
        log.debug("Request to get all appusers where Appprofile is null");
        return StreamSupport
            .stream(appuserRepository.findAll().spliterator(), false)
            .filter(appuser -> appuser.getAppprofile() == null)
            .map(appuserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the appusers where Appphoto is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppuserDTO> findAllWhereAppphotoIsNull() {
        log.debug("Request to get all appusers where Appphoto is null");
        return StreamSupport
            .stream(appuserRepository.findAll().spliterator(), false)
            .filter(appuser -> appuser.getAppphoto() == null)
            .map(appuserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppuserDTO> findOne(Long id) {
        log.debug("Request to get Appuser : {}", id);
        return appuserRepository.findById(id).map(appuserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Appuser : {}", id);
        appuserRepository.deleteById(id);
        appuserSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppuserDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Appusers for query {}", query);
        return appuserSearchRepository.search(query, pageable).map(appuserMapper::toDto);
    }
}
