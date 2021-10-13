package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Topic;
import com.blocknitive.com.repository.TopicRepository;
import com.blocknitive.com.repository.search.TopicSearchRepository;
import com.blocknitive.com.service.TopicService;
import com.blocknitive.com.service.dto.TopicDTO;
import com.blocknitive.com.service.mapper.TopicMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Topic}.
 */
@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    private final Logger log = LoggerFactory.getLogger(TopicServiceImpl.class);

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    private final TopicSearchRepository topicSearchRepository;

    public TopicServiceImpl(TopicRepository topicRepository, TopicMapper topicMapper, TopicSearchRepository topicSearchRepository) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
        this.topicSearchRepository = topicSearchRepository;
    }

    @Override
    public TopicDTO save(TopicDTO topicDTO) {
        log.debug("Request to save Topic : {}", topicDTO);
        Topic topic = topicMapper.toEntity(topicDTO);
        topic = topicRepository.save(topic);
        TopicDTO result = topicMapper.toDto(topic);
        topicSearchRepository.save(topic);
        return result;
    }

    @Override
    public Optional<TopicDTO> partialUpdate(TopicDTO topicDTO) {
        log.debug("Request to partially update Topic : {}", topicDTO);

        return topicRepository
            .findById(topicDTO.getId())
            .map(existingTopic -> {
                topicMapper.partialUpdate(existingTopic, topicDTO);

                return existingTopic;
            })
            .map(topicRepository::save)
            .map(savedTopic -> {
                topicSearchRepository.save(savedTopic);

                return savedTopic;
            })
            .map(topicMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopicDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Topics");
        return topicRepository.findAll(pageable).map(topicMapper::toDto);
    }

    public Page<TopicDTO> findAllWithEagerRelationships(Pageable pageable) {
        return topicRepository.findAllWithEagerRelationships(pageable).map(topicMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TopicDTO> findOne(Long id) {
        log.debug("Request to get Topic : {}", id);
        return topicRepository.findOneWithEagerRelationships(id).map(topicMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Topic : {}", id);
        topicRepository.deleteById(id);
        topicSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopicDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Topics for query {}", query);
        return topicSearchRepository.search(query, pageable).map(topicMapper::toDto);
    }
}
