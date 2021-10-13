package com.blocknitive.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blocknitive.com.IntegrationTest;
import com.blocknitive.com.domain.Appphoto;
import com.blocknitive.com.domain.Appuser;
import com.blocknitive.com.repository.AppphotoRepository;
import com.blocknitive.com.repository.search.AppphotoSearchRepository;
import com.blocknitive.com.service.criteria.AppphotoCriteria;
import com.blocknitive.com.service.dto.AppphotoDTO;
import com.blocknitive.com.service.mapper.AppphotoMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AppphotoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AppphotoResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/appphotos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/appphotos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppphotoRepository appphotoRepository;

    @Autowired
    private AppphotoMapper appphotoMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.AppphotoSearchRepositoryMockConfiguration
     */
    @Autowired
    private AppphotoSearchRepository mockAppphotoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppphotoMockMvc;

    private Appphoto appphoto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appphoto createEntity(EntityManager em) {
        Appphoto appphoto = new Appphoto()
            .creationDate(DEFAULT_CREATION_DATE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        appphoto.setAppuser(appuser);
        return appphoto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appphoto createUpdatedEntity(EntityManager em) {
        Appphoto appphoto = new Appphoto()
            .creationDate(UPDATED_CREATION_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        appphoto.setAppuser(appuser);
        return appphoto;
    }

    @BeforeEach
    public void initTest() {
        appphoto = createEntity(em);
    }

    @Test
    @Transactional
    void createAppphoto() throws Exception {
        int databaseSizeBeforeCreate = appphotoRepository.findAll().size();
        // Create the Appphoto
        AppphotoDTO appphotoDTO = appphotoMapper.toDto(appphoto);
        restAppphotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appphotoDTO)))
            .andExpect(status().isCreated());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeCreate + 1);
        Appphoto testAppphoto = appphotoList.get(appphotoList.size() - 1);
        assertThat(testAppphoto.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAppphoto.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testAppphoto.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the Appphoto in Elasticsearch
        verify(mockAppphotoSearchRepository, times(1)).save(testAppphoto);
    }

    @Test
    @Transactional
    void createAppphotoWithExistingId() throws Exception {
        // Create the Appphoto with an existing ID
        appphoto.setId(1L);
        AppphotoDTO appphotoDTO = appphotoMapper.toDto(appphoto);

        int databaseSizeBeforeCreate = appphotoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppphotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appphotoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Appphoto in Elasticsearch
        verify(mockAppphotoSearchRepository, times(0)).save(appphoto);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = appphotoRepository.findAll().size();
        // set the field null
        appphoto.setCreationDate(null);

        // Create the Appphoto, which fails.
        AppphotoDTO appphotoDTO = appphotoMapper.toDto(appphoto);

        restAppphotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appphotoDTO)))
            .andExpect(status().isBadRequest());

        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppphotos() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        // Get all the appphotoList
        restAppphotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appphoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getAppphoto() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        // Get the appphoto
        restAppphotoMockMvc
            .perform(get(ENTITY_API_URL_ID, appphoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appphoto.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getAppphotosByIdFiltering() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        Long id = appphoto.getId();

        defaultAppphotoShouldBeFound("id.equals=" + id);
        defaultAppphotoShouldNotBeFound("id.notEquals=" + id);

        defaultAppphotoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppphotoShouldNotBeFound("id.greaterThan=" + id);

        defaultAppphotoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppphotoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppphotosByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        // Get all the appphotoList where creationDate equals to DEFAULT_CREATION_DATE
        defaultAppphotoShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the appphotoList where creationDate equals to UPDATED_CREATION_DATE
        defaultAppphotoShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllAppphotosByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        // Get all the appphotoList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultAppphotoShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the appphotoList where creationDate not equals to UPDATED_CREATION_DATE
        defaultAppphotoShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllAppphotosByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        // Get all the appphotoList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultAppphotoShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the appphotoList where creationDate equals to UPDATED_CREATION_DATE
        defaultAppphotoShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllAppphotosByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        // Get all the appphotoList where creationDate is not null
        defaultAppphotoShouldBeFound("creationDate.specified=true");

        // Get all the appphotoList where creationDate is null
        defaultAppphotoShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAppphotosByAppuserIsEqualToSomething() throws Exception {
        // Get already existing entity
        Appuser appuser = appphoto.getAppuser();
        appphotoRepository.saveAndFlush(appphoto);
        Long appuserId = appuser.getId();

        // Get all the appphotoList where appuser equals to appuserId
        defaultAppphotoShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the appphotoList where appuser equals to (appuserId + 1)
        defaultAppphotoShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppphotoShouldBeFound(String filter) throws Exception {
        restAppphotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appphoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restAppphotoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppphotoShouldNotBeFound(String filter) throws Exception {
        restAppphotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppphotoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppphoto() throws Exception {
        // Get the appphoto
        restAppphotoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppphoto() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        int databaseSizeBeforeUpdate = appphotoRepository.findAll().size();

        // Update the appphoto
        Appphoto updatedAppphoto = appphotoRepository.findById(appphoto.getId()).get();
        // Disconnect from session so that the updates on updatedAppphoto are not directly saved in db
        em.detach(updatedAppphoto);
        updatedAppphoto.creationDate(UPDATED_CREATION_DATE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        AppphotoDTO appphotoDTO = appphotoMapper.toDto(updatedAppphoto);

        restAppphotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appphotoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appphotoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeUpdate);
        Appphoto testAppphoto = appphotoList.get(appphotoList.size() - 1);
        assertThat(testAppphoto.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAppphoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAppphoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the Appphoto in Elasticsearch
        verify(mockAppphotoSearchRepository).save(testAppphoto);
    }

    @Test
    @Transactional
    void putNonExistingAppphoto() throws Exception {
        int databaseSizeBeforeUpdate = appphotoRepository.findAll().size();
        appphoto.setId(count.incrementAndGet());

        // Create the Appphoto
        AppphotoDTO appphotoDTO = appphotoMapper.toDto(appphoto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppphotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appphotoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appphotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appphoto in Elasticsearch
        verify(mockAppphotoSearchRepository, times(0)).save(appphoto);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppphoto() throws Exception {
        int databaseSizeBeforeUpdate = appphotoRepository.findAll().size();
        appphoto.setId(count.incrementAndGet());

        // Create the Appphoto
        AppphotoDTO appphotoDTO = appphotoMapper.toDto(appphoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppphotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appphotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appphoto in Elasticsearch
        verify(mockAppphotoSearchRepository, times(0)).save(appphoto);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppphoto() throws Exception {
        int databaseSizeBeforeUpdate = appphotoRepository.findAll().size();
        appphoto.setId(count.incrementAndGet());

        // Create the Appphoto
        AppphotoDTO appphotoDTO = appphotoMapper.toDto(appphoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppphotoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appphotoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appphoto in Elasticsearch
        verify(mockAppphotoSearchRepository, times(0)).save(appphoto);
    }

    @Test
    @Transactional
    void partialUpdateAppphotoWithPatch() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        int databaseSizeBeforeUpdate = appphotoRepository.findAll().size();

        // Update the appphoto using partial update
        Appphoto partialUpdatedAppphoto = new Appphoto();
        partialUpdatedAppphoto.setId(appphoto.getId());

        partialUpdatedAppphoto.image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restAppphotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppphoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppphoto))
            )
            .andExpect(status().isOk());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeUpdate);
        Appphoto testAppphoto = appphotoList.get(appphotoList.size() - 1);
        assertThat(testAppphoto.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAppphoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAppphoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAppphotoWithPatch() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        int databaseSizeBeforeUpdate = appphotoRepository.findAll().size();

        // Update the appphoto using partial update
        Appphoto partialUpdatedAppphoto = new Appphoto();
        partialUpdatedAppphoto.setId(appphoto.getId());

        partialUpdatedAppphoto.creationDate(UPDATED_CREATION_DATE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restAppphotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppphoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppphoto))
            )
            .andExpect(status().isOk());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeUpdate);
        Appphoto testAppphoto = appphotoList.get(appphotoList.size() - 1);
        assertThat(testAppphoto.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAppphoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAppphoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAppphoto() throws Exception {
        int databaseSizeBeforeUpdate = appphotoRepository.findAll().size();
        appphoto.setId(count.incrementAndGet());

        // Create the Appphoto
        AppphotoDTO appphotoDTO = appphotoMapper.toDto(appphoto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppphotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appphotoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appphotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appphoto in Elasticsearch
        verify(mockAppphotoSearchRepository, times(0)).save(appphoto);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppphoto() throws Exception {
        int databaseSizeBeforeUpdate = appphotoRepository.findAll().size();
        appphoto.setId(count.incrementAndGet());

        // Create the Appphoto
        AppphotoDTO appphotoDTO = appphotoMapper.toDto(appphoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppphotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appphotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appphoto in Elasticsearch
        verify(mockAppphotoSearchRepository, times(0)).save(appphoto);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppphoto() throws Exception {
        int databaseSizeBeforeUpdate = appphotoRepository.findAll().size();
        appphoto.setId(count.incrementAndGet());

        // Create the Appphoto
        AppphotoDTO appphotoDTO = appphotoMapper.toDto(appphoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppphotoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appphotoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appphoto in the database
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appphoto in Elasticsearch
        verify(mockAppphotoSearchRepository, times(0)).save(appphoto);
    }

    @Test
    @Transactional
    void deleteAppphoto() throws Exception {
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);

        int databaseSizeBeforeDelete = appphotoRepository.findAll().size();

        // Delete the appphoto
        restAppphotoMockMvc
            .perform(delete(ENTITY_API_URL_ID, appphoto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appphoto> appphotoList = appphotoRepository.findAll();
        assertThat(appphotoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Appphoto in Elasticsearch
        verify(mockAppphotoSearchRepository, times(1)).deleteById(appphoto.getId());
    }

    @Test
    @Transactional
    void searchAppphoto() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        appphotoRepository.saveAndFlush(appphoto);
        when(mockAppphotoSearchRepository.search("id:" + appphoto.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(appphoto), PageRequest.of(0, 1), 1));

        // Search the appphoto
        restAppphotoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + appphoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appphoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
}
