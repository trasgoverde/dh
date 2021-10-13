package com.blocknitive.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blocknitive.com.IntegrationTest;
import com.blocknitive.com.domain.Appuser;
import com.blocknitive.com.domain.Vanswer;
import com.blocknitive.com.domain.Vquestion;
import com.blocknitive.com.domain.Vthumb;
import com.blocknitive.com.repository.VthumbRepository;
import com.blocknitive.com.repository.search.VthumbSearchRepository;
import com.blocknitive.com.service.criteria.VthumbCriteria;
import com.blocknitive.com.service.dto.VthumbDTO;
import com.blocknitive.com.service.mapper.VthumbMapper;
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
 * Integration tests for the {@link VthumbResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VthumbResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_VTHUMB_UP = false;
    private static final Boolean UPDATED_VTHUMB_UP = true;

    private static final Boolean DEFAULT_VTHUMB_DOWN = false;
    private static final Boolean UPDATED_VTHUMB_DOWN = true;

    private static final String ENTITY_API_URL = "/api/vthumbs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/vthumbs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VthumbRepository vthumbRepository;

    @Autowired
    private VthumbMapper vthumbMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.VthumbSearchRepositoryMockConfiguration
     */
    @Autowired
    private VthumbSearchRepository mockVthumbSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVthumbMockMvc;

    private Vthumb vthumb;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vthumb createEntity(EntityManager em) {
        Vthumb vthumb = new Vthumb().creationDate(DEFAULT_CREATION_DATE).vthumbUp(DEFAULT_VTHUMB_UP).vthumbDown(DEFAULT_VTHUMB_DOWN);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vthumb.setAppuser(appuser);
        return vthumb;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vthumb createUpdatedEntity(EntityManager em) {
        Vthumb vthumb = new Vthumb().creationDate(UPDATED_CREATION_DATE).vthumbUp(UPDATED_VTHUMB_UP).vthumbDown(UPDATED_VTHUMB_DOWN);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vthumb.setAppuser(appuser);
        return vthumb;
    }

    @BeforeEach
    public void initTest() {
        vthumb = createEntity(em);
    }

    @Test
    @Transactional
    void createVthumb() throws Exception {
        int databaseSizeBeforeCreate = vthumbRepository.findAll().size();
        // Create the Vthumb
        VthumbDTO vthumbDTO = vthumbMapper.toDto(vthumb);
        restVthumbMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vthumbDTO)))
            .andExpect(status().isCreated());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeCreate + 1);
        Vthumb testVthumb = vthumbList.get(vthumbList.size() - 1);
        assertThat(testVthumb.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testVthumb.getVthumbUp()).isEqualTo(DEFAULT_VTHUMB_UP);
        assertThat(testVthumb.getVthumbDown()).isEqualTo(DEFAULT_VTHUMB_DOWN);

        // Validate the Vthumb in Elasticsearch
        verify(mockVthumbSearchRepository, times(1)).save(testVthumb);
    }

    @Test
    @Transactional
    void createVthumbWithExistingId() throws Exception {
        // Create the Vthumb with an existing ID
        vthumb.setId(1L);
        VthumbDTO vthumbDTO = vthumbMapper.toDto(vthumb);

        int databaseSizeBeforeCreate = vthumbRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVthumbMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vthumbDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeCreate);

        // Validate the Vthumb in Elasticsearch
        verify(mockVthumbSearchRepository, times(0)).save(vthumb);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vthumbRepository.findAll().size();
        // set the field null
        vthumb.setCreationDate(null);

        // Create the Vthumb, which fails.
        VthumbDTO vthumbDTO = vthumbMapper.toDto(vthumb);

        restVthumbMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vthumbDTO)))
            .andExpect(status().isBadRequest());

        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVthumbs() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList
        restVthumbMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vthumb.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vthumbUp").value(hasItem(DEFAULT_VTHUMB_UP.booleanValue())))
            .andExpect(jsonPath("$.[*].vthumbDown").value(hasItem(DEFAULT_VTHUMB_DOWN.booleanValue())));
    }

    @Test
    @Transactional
    void getVthumb() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get the vthumb
        restVthumbMockMvc
            .perform(get(ENTITY_API_URL_ID, vthumb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vthumb.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.vthumbUp").value(DEFAULT_VTHUMB_UP.booleanValue()))
            .andExpect(jsonPath("$.vthumbDown").value(DEFAULT_VTHUMB_DOWN.booleanValue()));
    }

    @Test
    @Transactional
    void getVthumbsByIdFiltering() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        Long id = vthumb.getId();

        defaultVthumbShouldBeFound("id.equals=" + id);
        defaultVthumbShouldNotBeFound("id.notEquals=" + id);

        defaultVthumbShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVthumbShouldNotBeFound("id.greaterThan=" + id);

        defaultVthumbShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVthumbShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVthumbsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where creationDate equals to DEFAULT_CREATION_DATE
        defaultVthumbShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the vthumbList where creationDate equals to UPDATED_CREATION_DATE
        defaultVthumbShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVthumbsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultVthumbShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the vthumbList where creationDate not equals to UPDATED_CREATION_DATE
        defaultVthumbShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVthumbsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultVthumbShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the vthumbList where creationDate equals to UPDATED_CREATION_DATE
        defaultVthumbShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVthumbsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where creationDate is not null
        defaultVthumbShouldBeFound("creationDate.specified=true");

        // Get all the vthumbList where creationDate is null
        defaultVthumbShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVthumbsByVthumbUpIsEqualToSomething() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where vthumbUp equals to DEFAULT_VTHUMB_UP
        defaultVthumbShouldBeFound("vthumbUp.equals=" + DEFAULT_VTHUMB_UP);

        // Get all the vthumbList where vthumbUp equals to UPDATED_VTHUMB_UP
        defaultVthumbShouldNotBeFound("vthumbUp.equals=" + UPDATED_VTHUMB_UP);
    }

    @Test
    @Transactional
    void getAllVthumbsByVthumbUpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where vthumbUp not equals to DEFAULT_VTHUMB_UP
        defaultVthumbShouldNotBeFound("vthumbUp.notEquals=" + DEFAULT_VTHUMB_UP);

        // Get all the vthumbList where vthumbUp not equals to UPDATED_VTHUMB_UP
        defaultVthumbShouldBeFound("vthumbUp.notEquals=" + UPDATED_VTHUMB_UP);
    }

    @Test
    @Transactional
    void getAllVthumbsByVthumbUpIsInShouldWork() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where vthumbUp in DEFAULT_VTHUMB_UP or UPDATED_VTHUMB_UP
        defaultVthumbShouldBeFound("vthumbUp.in=" + DEFAULT_VTHUMB_UP + "," + UPDATED_VTHUMB_UP);

        // Get all the vthumbList where vthumbUp equals to UPDATED_VTHUMB_UP
        defaultVthumbShouldNotBeFound("vthumbUp.in=" + UPDATED_VTHUMB_UP);
    }

    @Test
    @Transactional
    void getAllVthumbsByVthumbUpIsNullOrNotNull() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where vthumbUp is not null
        defaultVthumbShouldBeFound("vthumbUp.specified=true");

        // Get all the vthumbList where vthumbUp is null
        defaultVthumbShouldNotBeFound("vthumbUp.specified=false");
    }

    @Test
    @Transactional
    void getAllVthumbsByVthumbDownIsEqualToSomething() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where vthumbDown equals to DEFAULT_VTHUMB_DOWN
        defaultVthumbShouldBeFound("vthumbDown.equals=" + DEFAULT_VTHUMB_DOWN);

        // Get all the vthumbList where vthumbDown equals to UPDATED_VTHUMB_DOWN
        defaultVthumbShouldNotBeFound("vthumbDown.equals=" + UPDATED_VTHUMB_DOWN);
    }

    @Test
    @Transactional
    void getAllVthumbsByVthumbDownIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where vthumbDown not equals to DEFAULT_VTHUMB_DOWN
        defaultVthumbShouldNotBeFound("vthumbDown.notEquals=" + DEFAULT_VTHUMB_DOWN);

        // Get all the vthumbList where vthumbDown not equals to UPDATED_VTHUMB_DOWN
        defaultVthumbShouldBeFound("vthumbDown.notEquals=" + UPDATED_VTHUMB_DOWN);
    }

    @Test
    @Transactional
    void getAllVthumbsByVthumbDownIsInShouldWork() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where vthumbDown in DEFAULT_VTHUMB_DOWN or UPDATED_VTHUMB_DOWN
        defaultVthumbShouldBeFound("vthumbDown.in=" + DEFAULT_VTHUMB_DOWN + "," + UPDATED_VTHUMB_DOWN);

        // Get all the vthumbList where vthumbDown equals to UPDATED_VTHUMB_DOWN
        defaultVthumbShouldNotBeFound("vthumbDown.in=" + UPDATED_VTHUMB_DOWN);
    }

    @Test
    @Transactional
    void getAllVthumbsByVthumbDownIsNullOrNotNull() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        // Get all the vthumbList where vthumbDown is not null
        defaultVthumbShouldBeFound("vthumbDown.specified=true");

        // Get all the vthumbList where vthumbDown is null
        defaultVthumbShouldNotBeFound("vthumbDown.specified=false");
    }

    @Test
    @Transactional
    void getAllVthumbsByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        em.persist(appuser);
        em.flush();
        vthumb.setAppuser(appuser);
        vthumbRepository.saveAndFlush(vthumb);
        Long appuserId = appuser.getId();

        // Get all the vthumbList where appuser equals to appuserId
        defaultVthumbShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the vthumbList where appuser equals to (appuserId + 1)
        defaultVthumbShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    @Test
    @Transactional
    void getAllVthumbsByVquestionIsEqualToSomething() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);
        Vquestion vquestion;
        if (TestUtil.findAll(em, Vquestion.class).isEmpty()) {
            vquestion = VquestionResourceIT.createEntity(em);
            em.persist(vquestion);
            em.flush();
        } else {
            vquestion = TestUtil.findAll(em, Vquestion.class).get(0);
        }
        em.persist(vquestion);
        em.flush();
        vthumb.setVquestion(vquestion);
        vthumbRepository.saveAndFlush(vthumb);
        Long vquestionId = vquestion.getId();

        // Get all the vthumbList where vquestion equals to vquestionId
        defaultVthumbShouldBeFound("vquestionId.equals=" + vquestionId);

        // Get all the vthumbList where vquestion equals to (vquestionId + 1)
        defaultVthumbShouldNotBeFound("vquestionId.equals=" + (vquestionId + 1));
    }

    @Test
    @Transactional
    void getAllVthumbsByVanswerIsEqualToSomething() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);
        Vanswer vanswer;
        if (TestUtil.findAll(em, Vanswer.class).isEmpty()) {
            vanswer = VanswerResourceIT.createEntity(em);
            em.persist(vanswer);
            em.flush();
        } else {
            vanswer = TestUtil.findAll(em, Vanswer.class).get(0);
        }
        em.persist(vanswer);
        em.flush();
        vthumb.setVanswer(vanswer);
        vthumbRepository.saveAndFlush(vthumb);
        Long vanswerId = vanswer.getId();

        // Get all the vthumbList where vanswer equals to vanswerId
        defaultVthumbShouldBeFound("vanswerId.equals=" + vanswerId);

        // Get all the vthumbList where vanswer equals to (vanswerId + 1)
        defaultVthumbShouldNotBeFound("vanswerId.equals=" + (vanswerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVthumbShouldBeFound(String filter) throws Exception {
        restVthumbMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vthumb.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vthumbUp").value(hasItem(DEFAULT_VTHUMB_UP.booleanValue())))
            .andExpect(jsonPath("$.[*].vthumbDown").value(hasItem(DEFAULT_VTHUMB_DOWN.booleanValue())));

        // Check, that the count call also returns 1
        restVthumbMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVthumbShouldNotBeFound(String filter) throws Exception {
        restVthumbMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVthumbMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVthumb() throws Exception {
        // Get the vthumb
        restVthumbMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVthumb() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        int databaseSizeBeforeUpdate = vthumbRepository.findAll().size();

        // Update the vthumb
        Vthumb updatedVthumb = vthumbRepository.findById(vthumb.getId()).get();
        // Disconnect from session so that the updates on updatedVthumb are not directly saved in db
        em.detach(updatedVthumb);
        updatedVthumb.creationDate(UPDATED_CREATION_DATE).vthumbUp(UPDATED_VTHUMB_UP).vthumbDown(UPDATED_VTHUMB_DOWN);
        VthumbDTO vthumbDTO = vthumbMapper.toDto(updatedVthumb);

        restVthumbMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vthumbDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vthumbDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeUpdate);
        Vthumb testVthumb = vthumbList.get(vthumbList.size() - 1);
        assertThat(testVthumb.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVthumb.getVthumbUp()).isEqualTo(UPDATED_VTHUMB_UP);
        assertThat(testVthumb.getVthumbDown()).isEqualTo(UPDATED_VTHUMB_DOWN);

        // Validate the Vthumb in Elasticsearch
        verify(mockVthumbSearchRepository).save(testVthumb);
    }

    @Test
    @Transactional
    void putNonExistingVthumb() throws Exception {
        int databaseSizeBeforeUpdate = vthumbRepository.findAll().size();
        vthumb.setId(count.incrementAndGet());

        // Create the Vthumb
        VthumbDTO vthumbDTO = vthumbMapper.toDto(vthumb);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVthumbMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vthumbDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vthumbDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vthumb in Elasticsearch
        verify(mockVthumbSearchRepository, times(0)).save(vthumb);
    }

    @Test
    @Transactional
    void putWithIdMismatchVthumb() throws Exception {
        int databaseSizeBeforeUpdate = vthumbRepository.findAll().size();
        vthumb.setId(count.incrementAndGet());

        // Create the Vthumb
        VthumbDTO vthumbDTO = vthumbMapper.toDto(vthumb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVthumbMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vthumbDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vthumb in Elasticsearch
        verify(mockVthumbSearchRepository, times(0)).save(vthumb);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVthumb() throws Exception {
        int databaseSizeBeforeUpdate = vthumbRepository.findAll().size();
        vthumb.setId(count.incrementAndGet());

        // Create the Vthumb
        VthumbDTO vthumbDTO = vthumbMapper.toDto(vthumb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVthumbMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vthumbDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vthumb in Elasticsearch
        verify(mockVthumbSearchRepository, times(0)).save(vthumb);
    }

    @Test
    @Transactional
    void partialUpdateVthumbWithPatch() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        int databaseSizeBeforeUpdate = vthumbRepository.findAll().size();

        // Update the vthumb using partial update
        Vthumb partialUpdatedVthumb = new Vthumb();
        partialUpdatedVthumb.setId(vthumb.getId());

        partialUpdatedVthumb.creationDate(UPDATED_CREATION_DATE).vthumbUp(UPDATED_VTHUMB_UP);

        restVthumbMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVthumb.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVthumb))
            )
            .andExpect(status().isOk());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeUpdate);
        Vthumb testVthumb = vthumbList.get(vthumbList.size() - 1);
        assertThat(testVthumb.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVthumb.getVthumbUp()).isEqualTo(UPDATED_VTHUMB_UP);
        assertThat(testVthumb.getVthumbDown()).isEqualTo(DEFAULT_VTHUMB_DOWN);
    }

    @Test
    @Transactional
    void fullUpdateVthumbWithPatch() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        int databaseSizeBeforeUpdate = vthumbRepository.findAll().size();

        // Update the vthumb using partial update
        Vthumb partialUpdatedVthumb = new Vthumb();
        partialUpdatedVthumb.setId(vthumb.getId());

        partialUpdatedVthumb.creationDate(UPDATED_CREATION_DATE).vthumbUp(UPDATED_VTHUMB_UP).vthumbDown(UPDATED_VTHUMB_DOWN);

        restVthumbMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVthumb.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVthumb))
            )
            .andExpect(status().isOk());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeUpdate);
        Vthumb testVthumb = vthumbList.get(vthumbList.size() - 1);
        assertThat(testVthumb.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVthumb.getVthumbUp()).isEqualTo(UPDATED_VTHUMB_UP);
        assertThat(testVthumb.getVthumbDown()).isEqualTo(UPDATED_VTHUMB_DOWN);
    }

    @Test
    @Transactional
    void patchNonExistingVthumb() throws Exception {
        int databaseSizeBeforeUpdate = vthumbRepository.findAll().size();
        vthumb.setId(count.incrementAndGet());

        // Create the Vthumb
        VthumbDTO vthumbDTO = vthumbMapper.toDto(vthumb);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVthumbMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vthumbDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vthumbDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vthumb in Elasticsearch
        verify(mockVthumbSearchRepository, times(0)).save(vthumb);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVthumb() throws Exception {
        int databaseSizeBeforeUpdate = vthumbRepository.findAll().size();
        vthumb.setId(count.incrementAndGet());

        // Create the Vthumb
        VthumbDTO vthumbDTO = vthumbMapper.toDto(vthumb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVthumbMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vthumbDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vthumb in Elasticsearch
        verify(mockVthumbSearchRepository, times(0)).save(vthumb);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVthumb() throws Exception {
        int databaseSizeBeforeUpdate = vthumbRepository.findAll().size();
        vthumb.setId(count.incrementAndGet());

        // Create the Vthumb
        VthumbDTO vthumbDTO = vthumbMapper.toDto(vthumb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVthumbMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vthumbDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vthumb in the database
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vthumb in Elasticsearch
        verify(mockVthumbSearchRepository, times(0)).save(vthumb);
    }

    @Test
    @Transactional
    void deleteVthumb() throws Exception {
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);

        int databaseSizeBeforeDelete = vthumbRepository.findAll().size();

        // Delete the vthumb
        restVthumbMockMvc
            .perform(delete(ENTITY_API_URL_ID, vthumb.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vthumb> vthumbList = vthumbRepository.findAll();
        assertThat(vthumbList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Vthumb in Elasticsearch
        verify(mockVthumbSearchRepository, times(1)).deleteById(vthumb.getId());
    }

    @Test
    @Transactional
    void searchVthumb() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        vthumbRepository.saveAndFlush(vthumb);
        when(mockVthumbSearchRepository.search("id:" + vthumb.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(vthumb), PageRequest.of(0, 1), 1));

        // Search the vthumb
        restVthumbMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + vthumb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vthumb.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vthumbUp").value(hasItem(DEFAULT_VTHUMB_UP.booleanValue())))
            .andExpect(jsonPath("$.[*].vthumbDown").value(hasItem(DEFAULT_VTHUMB_DOWN.booleanValue())));
    }
}
