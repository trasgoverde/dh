package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Feedback;
import com.blocknitive.com.repository.FeedbackRepository;
import com.blocknitive.com.repository.search.FeedbackSearchRepository;
import com.blocknitive.com.service.FeedbackService;
import com.blocknitive.com.service.dto.FeedbackDTO;
import com.blocknitive.com.service.mapper.FeedbackMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Feedback}.
 */
@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    private final FeedbackSearchRepository feedbackSearchRepository;

    public FeedbackServiceImpl(
        FeedbackRepository feedbackRepository,
        FeedbackMapper feedbackMapper,
        FeedbackSearchRepository feedbackSearchRepository
    ) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.feedbackSearchRepository = feedbackSearchRepository;
    }

    @Override
    public FeedbackDTO save(FeedbackDTO feedbackDTO) {
        log.debug("Request to save Feedback : {}", feedbackDTO);
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);
        feedback = feedbackRepository.save(feedback);
        FeedbackDTO result = feedbackMapper.toDto(feedback);
        feedbackSearchRepository.save(feedback);
        return result;
    }

    @Override
    public Optional<FeedbackDTO> partialUpdate(FeedbackDTO feedbackDTO) {
        log.debug("Request to partially update Feedback : {}", feedbackDTO);

        return feedbackRepository
            .findById(feedbackDTO.getId())
            .map(existingFeedback -> {
                feedbackMapper.partialUpdate(existingFeedback, feedbackDTO);

                return existingFeedback;
            })
            .map(feedbackRepository::save)
            .map(savedFeedback -> {
                feedbackSearchRepository.save(savedFeedback);

                return savedFeedback;
            })
            .map(feedbackMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Feedbacks");
        return feedbackRepository.findAll(pageable).map(feedbackMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeedbackDTO> findOne(Long id) {
        log.debug("Request to get Feedback : {}", id);
        return feedbackRepository.findById(id).map(feedbackMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Feedback : {}", id);
        feedbackRepository.deleteById(id);
        feedbackSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Feedbacks for query {}", query);
        return feedbackSearchRepository.search(query, pageable).map(feedbackMapper::toDto);
    }
}
