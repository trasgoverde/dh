package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.ConfigVariables;
import com.blocknitive.com.repository.ConfigVariablesRepository;
import com.blocknitive.com.repository.search.ConfigVariablesSearchRepository;
import com.blocknitive.com.service.ConfigVariablesService;
import com.blocknitive.com.service.dto.ConfigVariablesDTO;
import com.blocknitive.com.service.mapper.ConfigVariablesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ConfigVariables}.
 */
@Service
@Transactional
public class ConfigVariablesServiceImpl implements ConfigVariablesService {

    private final Logger log = LoggerFactory.getLogger(ConfigVariablesServiceImpl.class);

    private final ConfigVariablesRepository configVariablesRepository;

    private final ConfigVariablesMapper configVariablesMapper;

    private final ConfigVariablesSearchRepository configVariablesSearchRepository;

    public ConfigVariablesServiceImpl(
        ConfigVariablesRepository configVariablesRepository,
        ConfigVariablesMapper configVariablesMapper,
        ConfigVariablesSearchRepository configVariablesSearchRepository
    ) {
        this.configVariablesRepository = configVariablesRepository;
        this.configVariablesMapper = configVariablesMapper;
        this.configVariablesSearchRepository = configVariablesSearchRepository;
    }

    @Override
    public ConfigVariablesDTO save(ConfigVariablesDTO configVariablesDTO) {
        log.debug("Request to save ConfigVariables : {}", configVariablesDTO);
        ConfigVariables configVariables = configVariablesMapper.toEntity(configVariablesDTO);
        configVariables = configVariablesRepository.save(configVariables);
        ConfigVariablesDTO result = configVariablesMapper.toDto(configVariables);
        configVariablesSearchRepository.save(configVariables);
        return result;
    }

    @Override
    public Optional<ConfigVariablesDTO> partialUpdate(ConfigVariablesDTO configVariablesDTO) {
        log.debug("Request to partially update ConfigVariables : {}", configVariablesDTO);

        return configVariablesRepository
            .findById(configVariablesDTO.getId())
            .map(existingConfigVariables -> {
                configVariablesMapper.partialUpdate(existingConfigVariables, configVariablesDTO);

                return existingConfigVariables;
            })
            .map(configVariablesRepository::save)
            .map(savedConfigVariables -> {
                configVariablesSearchRepository.save(savedConfigVariables);

                return savedConfigVariables;
            })
            .map(configVariablesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConfigVariablesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigVariables");
        return configVariablesRepository.findAll(pageable).map(configVariablesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigVariablesDTO> findOne(Long id) {
        log.debug("Request to get ConfigVariables : {}", id);
        return configVariablesRepository.findById(id).map(configVariablesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigVariables : {}", id);
        configVariablesRepository.deleteById(id);
        configVariablesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConfigVariablesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ConfigVariables for query {}", query);
        return configVariablesSearchRepository.search(query, pageable).map(configVariablesMapper::toDto);
    }
}
