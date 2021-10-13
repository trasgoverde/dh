package com.blocknitive.com.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.blocknitive.com.domain.Blockuser;
import com.blocknitive.com.repository.BlockuserRepository;
import com.blocknitive.com.repository.search.BlockuserSearchRepository;
import com.blocknitive.com.service.BlockuserService;
import com.blocknitive.com.service.dto.BlockuserDTO;
import com.blocknitive.com.service.mapper.BlockuserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Blockuser}.
 */
@Service
@Transactional
public class BlockuserServiceImpl implements BlockuserService {

    private final Logger log = LoggerFactory.getLogger(BlockuserServiceImpl.class);

    private final BlockuserRepository blockuserRepository;

    private final BlockuserMapper blockuserMapper;

    private final BlockuserSearchRepository blockuserSearchRepository;

    public BlockuserServiceImpl(
        BlockuserRepository blockuserRepository,
        BlockuserMapper blockuserMapper,
        BlockuserSearchRepository blockuserSearchRepository
    ) {
        this.blockuserRepository = blockuserRepository;
        this.blockuserMapper = blockuserMapper;
        this.blockuserSearchRepository = blockuserSearchRepository;
    }

    @Override
    public BlockuserDTO save(BlockuserDTO blockuserDTO) {
        log.debug("Request to save Blockuser : {}", blockuserDTO);
        Blockuser blockuser = blockuserMapper.toEntity(blockuserDTO);
        blockuser = blockuserRepository.save(blockuser);
        BlockuserDTO result = blockuserMapper.toDto(blockuser);
        blockuserSearchRepository.save(blockuser);
        return result;
    }

    @Override
    public Optional<BlockuserDTO> partialUpdate(BlockuserDTO blockuserDTO) {
        log.debug("Request to partially update Blockuser : {}", blockuserDTO);

        return blockuserRepository
            .findById(blockuserDTO.getId())
            .map(existingBlockuser -> {
                blockuserMapper.partialUpdate(existingBlockuser, blockuserDTO);

                return existingBlockuser;
            })
            .map(blockuserRepository::save)
            .map(savedBlockuser -> {
                blockuserSearchRepository.save(savedBlockuser);

                return savedBlockuser;
            })
            .map(blockuserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlockuserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Blockusers");
        return blockuserRepository.findAll(pageable).map(blockuserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BlockuserDTO> findOne(Long id) {
        log.debug("Request to get Blockuser : {}", id);
        return blockuserRepository.findById(id).map(blockuserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Blockuser : {}", id);
        blockuserRepository.deleteById(id);
        blockuserSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlockuserDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Blockusers for query {}", query);
        return blockuserSearchRepository.search(query, pageable).map(blockuserMapper::toDto);
    }
}
