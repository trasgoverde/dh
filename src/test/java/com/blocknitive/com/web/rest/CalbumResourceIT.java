package com.blocknitive.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blocknitive.com.IntegrationTest;
import com.blocknitive.com.domain.Calbum;
import com.blocknitive.com.domain.Community;
import com.blocknitive.com.domain.Photo;
import com.blocknitive.com.repository.CalbumRepository;
import com.blocknitive.com.repository.search.CalbumSearchRepository;
import com.blocknitive.com.service.criteria.CalbumCriteria;
import com.blocknitive.com.service.dto.CalbumDTO;
import com.blocknitive.com.service.mapper.CalbumMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CalbumResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CalbumResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/calbums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/calbums";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CalbumRepository calbumRepository;

    @Autowired
    private CalbumMapper calbumMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.CalbumSearchRepositoryMockConfiguration
     */
    @Autowired
    private CalbumSearchRepository mockCalbumSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalbumMockMvc;

    private Calbum calbum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calbum createEntity(EntityManager em) {
        Calbum calbum = new Calbum().creationDate(DEFAULT_CREATION_DATE).title(DEFAULT_TITLE);
        // Add required entity
        Community community;
        if (TestUtil.findAll(em, Community.class).isEmpty()) {
            community = CommunityResourceIT.createEntity(em);
            em.persist(community);
            em.flush();
        } else {
            community = TestUtil.findAll(em, Community.class).get(0);
        }
        calbum.setCommunity(community);
        return calbum;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calbum createUpdatedEntity(EntityManager em) {
        Calbum calbum = new Calbum().creationDate(UPDATED_CREATION_DATE).title(UPDATED_TITLE);
        // Add required entity
        Community community;
        if (TestUtil.findAll(em, Community.class).isEmpty()) {
            community = CommunityResourceIT.createUpdatedEntity(em);
            em.persist(community);
            em.flush();
        } else {
            community = TestUtil.findAll(em, Community.class).get(0);
        }
        calbum.setCommunity(community);
        return calbum;
    }

    @BeforeEach
    public void initTest() {
        calbum = createEntity(em);
    }

    @Test
    @Transactional
    void createCalbum() throws Exception {
        int databaseSizeBeforeCreate = calbumRepository.findAll().size();
        // Create the Calbum
        CalbumDTO calbumDTO = calbumMapper.toDto(calbum);
        restCalbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calbumDTO)))
            .andExpect(status().isCreated());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeCreate + 1);
        Calbum testCalbum = calbumList.get(calbumList.size() - 1);
        assertThat(testCalbum.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCalbum.getTitle()).isEqualTo(DEFAULT_TITLE);

        // Validate the Calbum in Elasticsearch
        verify(mockCalbumSearchRepository, times(1)).save(testCalbum);
    }

    @Test
    @Transactional
    void createCalbumWithExistingId() throws Exception {
        // Create the Calbum with an existing ID
        calbum.setId(1L);
        CalbumDTO calbumDTO = calbumMapper.toDto(calbum);

        int databaseSizeBeforeCreate = calbumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calbumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeCreate);

        // Validate the Calbum in Elasticsearch
        verify(mockCalbumSearchRepository, times(0)).save(calbum);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = calbumRepository.findAll().size();
        // set the field null
        calbum.setCreationDate(null);

        // Create the Calbum, which fails.
        CalbumDTO calbumDTO = calbumMapper.toDto(calbum);

        restCalbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calbumDTO)))
            .andExpect(status().isBadRequest());

        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = calbumRepository.findAll().size();
        // set the field null
        calbum.setTitle(null);

        // Create the Calbum, which fails.
        CalbumDTO calbumDTO = calbumMapper.toDto(calbum);

        restCalbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calbumDTO)))
            .andExpect(status().isBadRequest());

        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCalbums() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList
        restCalbumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calbum.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getCalbum() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get the calbum
        restCalbumMockMvc
            .perform(get(ENTITY_API_URL_ID, calbum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calbum.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getCalbumsByIdFiltering() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        Long id = calbum.getId();

        defaultCalbumShouldBeFound("id.equals=" + id);
        defaultCalbumShouldNotBeFound("id.notEquals=" + id);

        defaultCalbumShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCalbumShouldNotBeFound("id.greaterThan=" + id);

        defaultCalbumShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCalbumShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCalbumsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList where creationDate equals to DEFAULT_CREATION_DATE
        defaultCalbumShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the calbumList where creationDate equals to UPDATED_CREATION_DATE
        defaultCalbumShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllCalbumsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultCalbumShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the calbumList where creationDate not equals to UPDATED_CREATION_DATE
        defaultCalbumShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllCalbumsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultCalbumShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the calbumList where creationDate equals to UPDATED_CREATION_DATE
        defaultCalbumShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllCalbumsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList where creationDate is not null
        defaultCalbumShouldBeFound("creationDate.specified=true");

        // Get all the calbumList where creationDate is null
        defaultCalbumShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCalbumsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList where title equals to DEFAULT_TITLE
        defaultCalbumShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the calbumList where title equals to UPDATED_TITLE
        defaultCalbumShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCalbumsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList where title not equals to DEFAULT_TITLE
        defaultCalbumShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the calbumList where title not equals to UPDATED_TITLE
        defaultCalbumShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCalbumsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCalbumShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the calbumList where title equals to UPDATED_TITLE
        defaultCalbumShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCalbumsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList where title is not null
        defaultCalbumShouldBeFound("title.specified=true");

        // Get all the calbumList where title is null
        defaultCalbumShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllCalbumsByTitleContainsSomething() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList where title contains DEFAULT_TITLE
        defaultCalbumShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the calbumList where title contains UPDATED_TITLE
        defaultCalbumShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCalbumsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        // Get all the calbumList where title does not contain DEFAULT_TITLE
        defaultCalbumShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the calbumList where title does not contain UPDATED_TITLE
        defaultCalbumShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCalbumsByPhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);
        Photo photo;
        if (TestUtil.findAll(em, Photo.class).isEmpty()) {
            photo = PhotoResourceIT.createEntity(em);
            em.persist(photo);
            em.flush();
        } else {
            photo = TestUtil.findAll(em, Photo.class).get(0);
        }
        em.persist(photo);
        em.flush();
        calbum.addPhoto(photo);
        calbumRepository.saveAndFlush(calbum);
        Long photoId = photo.getId();

        // Get all the calbumList where photo equals to photoId
        defaultCalbumShouldBeFound("photoId.equals=" + photoId);

        // Get all the calbumList where photo equals to (photoId + 1)
        defaultCalbumShouldNotBeFound("photoId.equals=" + (photoId + 1));
    }

    @Test
    @Transactional
    void getAllCalbumsByCommunityIsEqualToSomething() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);
        Community community;
        if (TestUtil.findAll(em, Community.class).isEmpty()) {
            community = CommunityResourceIT.createEntity(em);
            em.persist(community);
            em.flush();
        } else {
            community = TestUtil.findAll(em, Community.class).get(0);
        }
        em.persist(community);
        em.flush();
        calbum.setCommunity(community);
        calbumRepository.saveAndFlush(calbum);
        Long communityId = community.getId();

        // Get all the calbumList where community equals to communityId
        defaultCalbumShouldBeFound("communityId.equals=" + communityId);

        // Get all the calbumList where community equals to (communityId + 1)
        defaultCalbumShouldNotBeFound("communityId.equals=" + (communityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCalbumShouldBeFound(String filter) throws Exception {
        restCalbumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calbum.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));

        // Check, that the count call also returns 1
        restCalbumMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCalbumShouldNotBeFound(String filter) throws Exception {
        restCalbumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCalbumMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCalbum() throws Exception {
        // Get the calbum
        restCalbumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCalbum() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        int databaseSizeBeforeUpdate = calbumRepository.findAll().size();

        // Update the calbum
        Calbum updatedCalbum = calbumRepository.findById(calbum.getId()).get();
        // Disconnect from session so that the updates on updatedCalbum are not directly saved in db
        em.detach(updatedCalbum);
        updatedCalbum.creationDate(UPDATED_CREATION_DATE).title(UPDATED_TITLE);
        CalbumDTO calbumDTO = calbumMapper.toDto(updatedCalbum);

        restCalbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calbumDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calbumDTO))
            )
            .andExpect(status().isOk());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeUpdate);
        Calbum testCalbum = calbumList.get(calbumList.size() - 1);
        assertThat(testCalbum.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCalbum.getTitle()).isEqualTo(UPDATED_TITLE);

        // Validate the Calbum in Elasticsearch
        verify(mockCalbumSearchRepository).save(testCalbum);
    }

    @Test
    @Transactional
    void putNonExistingCalbum() throws Exception {
        int databaseSizeBeforeUpdate = calbumRepository.findAll().size();
        calbum.setId(count.incrementAndGet());

        // Create the Calbum
        CalbumDTO calbumDTO = calbumMapper.toDto(calbum);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calbumDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calbumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Calbum in Elasticsearch
        verify(mockCalbumSearchRepository, times(0)).save(calbum);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalbum() throws Exception {
        int databaseSizeBeforeUpdate = calbumRepository.findAll().size();
        calbum.setId(count.incrementAndGet());

        // Create the Calbum
        CalbumDTO calbumDTO = calbumMapper.toDto(calbum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calbumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Calbum in Elasticsearch
        verify(mockCalbumSearchRepository, times(0)).save(calbum);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalbum() throws Exception {
        int databaseSizeBeforeUpdate = calbumRepository.findAll().size();
        calbum.setId(count.incrementAndGet());

        // Create the Calbum
        CalbumDTO calbumDTO = calbumMapper.toDto(calbum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalbumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calbumDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Calbum in Elasticsearch
        verify(mockCalbumSearchRepository, times(0)).save(calbum);
    }

    @Test
    @Transactional
    void partialUpdateCalbumWithPatch() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        int databaseSizeBeforeUpdate = calbumRepository.findAll().size();

        // Update the calbum using partial update
        Calbum partialUpdatedCalbum = new Calbum();
        partialUpdatedCalbum.setId(calbum.getId());

        restCalbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalbum))
            )
            .andExpect(status().isOk());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeUpdate);
        Calbum testCalbum = calbumList.get(calbumList.size() - 1);
        assertThat(testCalbum.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCalbum.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateCalbumWithPatch() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        int databaseSizeBeforeUpdate = calbumRepository.findAll().size();

        // Update the calbum using partial update
        Calbum partialUpdatedCalbum = new Calbum();
        partialUpdatedCalbum.setId(calbum.getId());

        partialUpdatedCalbum.creationDate(UPDATED_CREATION_DATE).title(UPDATED_TITLE);

        restCalbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalbum))
            )
            .andExpect(status().isOk());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeUpdate);
        Calbum testCalbum = calbumList.get(calbumList.size() - 1);
        assertThat(testCalbum.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCalbum.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingCalbum() throws Exception {
        int databaseSizeBeforeUpdate = calbumRepository.findAll().size();
        calbum.setId(count.incrementAndGet());

        // Create the Calbum
        CalbumDTO calbumDTO = calbumMapper.toDto(calbum);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calbumDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calbumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Calbum in Elasticsearch
        verify(mockCalbumSearchRepository, times(0)).save(calbum);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalbum() throws Exception {
        int databaseSizeBeforeUpdate = calbumRepository.findAll().size();
        calbum.setId(count.incrementAndGet());

        // Create the Calbum
        CalbumDTO calbumDTO = calbumMapper.toDto(calbum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calbumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Calbum in Elasticsearch
        verify(mockCalbumSearchRepository, times(0)).save(calbum);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalbum() throws Exception {
        int databaseSizeBeforeUpdate = calbumRepository.findAll().size();
        calbum.setId(count.incrementAndGet());

        // Create the Calbum
        CalbumDTO calbumDTO = calbumMapper.toDto(calbum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalbumMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(calbumDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calbum in the database
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Calbum in Elasticsearch
        verify(mockCalbumSearchRepository, times(0)).save(calbum);
    }

    @Test
    @Transactional
    void deleteCalbum() throws Exception {
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);

        int databaseSizeBeforeDelete = calbumRepository.findAll().size();

        // Delete the calbum
        restCalbumMockMvc
            .perform(delete(ENTITY_API_URL_ID, calbum.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Calbum> calbumList = calbumRepository.findAll();
        assertThat(calbumList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Calbum in Elasticsearch
        verify(mockCalbumSearchRepository, times(1)).deleteById(calbum.getId());
    }

    @Test
    @Transactional
    void searchCalbum() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        calbumRepository.saveAndFlush(calbum);
        when(mockCalbumSearchRepository.search("id:" + calbum.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(calbum), PageRequest.of(0, 1), 1));

        // Search the calbum
        restCalbumMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + calbum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calbum.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }
}
