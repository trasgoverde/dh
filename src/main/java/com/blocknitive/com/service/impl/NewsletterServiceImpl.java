package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Newsletter;
import com.blocknitive.com.repository.NewsletterRepository;
import com.blocknitive.com.repository.search.NewsletterSearchRepository;
import com.blocknitive.com.service.NewsletterService;
import com.blocknitive.com.service.dto.NewsletterDTO;
import com.blocknitive.com.service.mapper.NewsletterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Newsletter}.
 */
@Service
@Transactional
public class NewsletterServiceImpl implements NewsletterService {

    private final Logger log = LoggerFactory.getLogger(NewsletterServiceImpl.class);

    private final NewsletterRepository newsletterRepository;

    private final NewsletterMapper newsletterMapper;

    private final NewsletterSearchRepository newsletterSearchRepository;

    public NewsletterServiceImpl(
        NewsletterRepository newsletterRepository,
        NewsletterMapper newsletterMapper,
        NewsletterSearchRepository newsletterSearchRepository
    ) {
        this.newsletterRepository = newsletterRepository;
        this.newsletterMapper = newsletterMapper;
        this.newsletterSearchRepository = newsletterSearchRepository;
    }

    @Override
    public NewsletterDTO save(NewsletterDTO newsletterDTO) {
        log.debug("Request to save Newsletter : {}", newsletterDTO);
        Newsletter newsletter = newsletterMapper.toEntity(newsletterDTO);
        newsletter = newsletterRepository.save(newsletter);
        NewsletterDTO result = newsletterMapper.toDto(newsletter);
        newsletterSearchRepository.save(newsletter);
        return result;
    }

    @Override
    public Optional<NewsletterDTO> partialUpdate(NewsletterDTO newsletterDTO) {
        log.debug("Request to partially update Newsletter : {}", newsletterDTO);

        return newsletterRepository
            .findById(newsletterDTO.getId())
            .map(existingNewsletter -> {
                newsletterMapper.partialUpdate(existingNewsletter, newsletterDTO);

                return existingNewsletter;
            })
            .map(newsletterRepository::save)
            .map(savedNewsletter -> {
                newsletterSearchRepository.save(savedNewsletter);

                return savedNewsletter;
            })
            .map(newsletterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NewsletterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Newsletters");
        return newsletterRepository.findAll(pageable).map(newsletterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NewsletterDTO> findOne(Long id) {
        log.debug("Request to get Newsletter : {}", id);
        return newsletterRepository.findById(id).map(newsletterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Newsletter : {}", id);
        newsletterRepository.deleteById(id);
        newsletterSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NewsletterDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Newsletters for query {}", query);
        return newsletterSearchRepository.search(query, pageable).map(newsletterMapper::toDto);
    }
}
