package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Photo;
import com.blocknitive.com.repository.PhotoRepository;
import com.blocknitive.com.repository.search.PhotoSearchRepository;
import com.blocknitive.com.service.PhotoService;
import com.blocknitive.com.service.dto.PhotoDTO;
import com.blocknitive.com.service.mapper.PhotoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Photo}.
 */
@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private final PhotoRepository photoRepository;

    private final PhotoMapper photoMapper;

    private final PhotoSearchRepository photoSearchRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository, PhotoMapper photoMapper, PhotoSearchRepository photoSearchRepository) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
        this.photoSearchRepository = photoSearchRepository;
    }

    @Override
    public PhotoDTO save(PhotoDTO photoDTO) {
        log.debug("Request to save Photo : {}", photoDTO);
        Photo photo = photoMapper.toEntity(photoDTO);
        photo = photoRepository.save(photo);
        PhotoDTO result = photoMapper.toDto(photo);
        photoSearchRepository.save(photo);
        return result;
    }

    @Override
    public Optional<PhotoDTO> partialUpdate(PhotoDTO photoDTO) {
        log.debug("Request to partially update Photo : {}", photoDTO);

        return photoRepository
            .findById(photoDTO.getId())
            .map(existingPhoto -> {
                photoMapper.partialUpdate(existingPhoto, photoDTO);

                return existingPhoto;
            })
            .map(photoRepository::save)
            .map(savedPhoto -> {
                photoSearchRepository.save(savedPhoto);

                return savedPhoto;
            })
            .map(photoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PhotoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Photos");
        return photoRepository.findAll(pageable).map(photoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PhotoDTO> findOne(Long id) {
        log.debug("Request to get Photo : {}", id);
        return photoRepository.findById(id).map(photoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Photo : {}", id);
        photoRepository.deleteById(id);
        photoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PhotoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Photos for query {}", query);
        return photoSearchRepository.search(query, pageable).map(photoMapper::toDto);
    }
}
