package com.blocknitive.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blocknitive.com.IntegrationTest;
import com.blocknitive.com.domain.Appuser;
import com.blocknitive.com.domain.Vquestion;
import com.blocknitive.com.domain.Vtopic;
import com.blocknitive.com.repository.VtopicRepository;
import com.blocknitive.com.repository.search.VtopicSearchRepository;
import com.blocknitive.com.service.criteria.VtopicCriteria;
import com.blocknitive.com.service.dto.VtopicDTO;
import com.blocknitive.com.service.mapper.VtopicMapper;
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
 * Integration tests for the {@link VtopicResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VtopicResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VTOPIC_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_VTOPIC_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_VTOPIC_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_VTOPIC_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vtopics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/vtopics";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VtopicRepository vtopicRepository;

    @Autowired
    private VtopicMapper vtopicMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.VtopicSearchRepositoryMockConfiguration
     */
    @Autowired
    private VtopicSearchRepository mockVtopicSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVtopicMockMvc;

    private Vtopic vtopic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vtopic createEntity(EntityManager em) {
        Vtopic vtopic = new Vtopic()
            .creationDate(DEFAULT_CREATION_DATE)
            .vtopicTitle(DEFAULT_VTOPIC_TITLE)
            .vtopicDescription(DEFAULT_VTOPIC_DESCRIPTION);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vtopic.setAppuser(appuser);
        return vtopic;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vtopic createUpdatedEntity(EntityManager em) {
        Vtopic vtopic = new Vtopic()
            .creationDate(UPDATED_CREATION_DATE)
            .vtopicTitle(UPDATED_VTOPIC_TITLE)
            .vtopicDescription(UPDATED_VTOPIC_DESCRIPTION);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vtopic.setAppuser(appuser);
        return vtopic;
    }

    @BeforeEach
    public void initTest() {
        vtopic = createEntity(em);
    }

    @Test
    @Transactional
    void createVtopic() throws Exception {
        int databaseSizeBeforeCreate = vtopicRepository.findAll().size();
        // Create the Vtopic
        VtopicDTO vtopicDTO = vtopicMapper.toDto(vtopic);
        restVtopicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vtopicDTO)))
            .andExpect(status().isCreated());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeCreate + 1);
        Vtopic testVtopic = vtopicList.get(vtopicList.size() - 1);
        assertThat(testVtopic.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testVtopic.getVtopicTitle()).isEqualTo(DEFAULT_VTOPIC_TITLE);
        assertThat(testVtopic.getVtopicDescription()).isEqualTo(DEFAULT_VTOPIC_DESCRIPTION);

        // Validate the Vtopic in Elasticsearch
        verify(mockVtopicSearchRepository, times(1)).save(testVtopic);
    }

    @Test
    @Transactional
    void createVtopicWithExistingId() throws Exception {
        // Create the Vtopic with an existing ID
        vtopic.setId(1L);
        VtopicDTO vtopicDTO = vtopicMapper.toDto(vtopic);

        int databaseSizeBeforeCreate = vtopicRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVtopicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vtopicDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeCreate);

        // Validate the Vtopic in Elasticsearch
        verify(mockVtopicSearchRepository, times(0)).save(vtopic);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vtopicRepository.findAll().size();
        // set the field null
        vtopic.setCreationDate(null);

        // Create the Vtopic, which fails.
        VtopicDTO vtopicDTO = vtopicMapper.toDto(vtopic);

        restVtopicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vtopicDTO)))
            .andExpect(status().isBadRequest());

        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVtopicTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = vtopicRepository.findAll().size();
        // set the field null
        vtopic.setVtopicTitle(null);

        // Create the Vtopic, which fails.
        VtopicDTO vtopicDTO = vtopicMapper.toDto(vtopic);

        restVtopicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vtopicDTO)))
            .andExpect(status().isBadRequest());

        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVtopics() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList
        restVtopicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vtopic.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vtopicTitle").value(hasItem(DEFAULT_VTOPIC_TITLE)))
            .andExpect(jsonPath("$.[*].vtopicDescription").value(hasItem(DEFAULT_VTOPIC_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getVtopic() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get the vtopic
        restVtopicMockMvc
            .perform(get(ENTITY_API_URL_ID, vtopic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vtopic.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.vtopicTitle").value(DEFAULT_VTOPIC_TITLE))
            .andExpect(jsonPath("$.vtopicDescription").value(DEFAULT_VTOPIC_DESCRIPTION));
    }

    @Test
    @Transactional
    void getVtopicsByIdFiltering() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        Long id = vtopic.getId();

        defaultVtopicShouldBeFound("id.equals=" + id);
        defaultVtopicShouldNotBeFound("id.notEquals=" + id);

        defaultVtopicShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVtopicShouldNotBeFound("id.greaterThan=" + id);

        defaultVtopicShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVtopicShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVtopicsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where creationDate equals to DEFAULT_CREATION_DATE
        defaultVtopicShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the vtopicList where creationDate equals to UPDATED_CREATION_DATE
        defaultVtopicShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVtopicsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultVtopicShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the vtopicList where creationDate not equals to UPDATED_CREATION_DATE
        defaultVtopicShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVtopicsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultVtopicShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the vtopicList where creationDate equals to UPDATED_CREATION_DATE
        defaultVtopicShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVtopicsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where creationDate is not null
        defaultVtopicShouldBeFound("creationDate.specified=true");

        // Get all the vtopicList where creationDate is null
        defaultVtopicShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicTitle equals to DEFAULT_VTOPIC_TITLE
        defaultVtopicShouldBeFound("vtopicTitle.equals=" + DEFAULT_VTOPIC_TITLE);

        // Get all the vtopicList where vtopicTitle equals to UPDATED_VTOPIC_TITLE
        defaultVtopicShouldNotBeFound("vtopicTitle.equals=" + UPDATED_VTOPIC_TITLE);
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicTitle not equals to DEFAULT_VTOPIC_TITLE
        defaultVtopicShouldNotBeFound("vtopicTitle.notEquals=" + DEFAULT_VTOPIC_TITLE);

        // Get all the vtopicList where vtopicTitle not equals to UPDATED_VTOPIC_TITLE
        defaultVtopicShouldBeFound("vtopicTitle.notEquals=" + UPDATED_VTOPIC_TITLE);
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicTitleIsInShouldWork() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicTitle in DEFAULT_VTOPIC_TITLE or UPDATED_VTOPIC_TITLE
        defaultVtopicShouldBeFound("vtopicTitle.in=" + DEFAULT_VTOPIC_TITLE + "," + UPDATED_VTOPIC_TITLE);

        // Get all the vtopicList where vtopicTitle equals to UPDATED_VTOPIC_TITLE
        defaultVtopicShouldNotBeFound("vtopicTitle.in=" + UPDATED_VTOPIC_TITLE);
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicTitle is not null
        defaultVtopicShouldBeFound("vtopicTitle.specified=true");

        // Get all the vtopicList where vtopicTitle is null
        defaultVtopicShouldNotBeFound("vtopicTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicTitleContainsSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicTitle contains DEFAULT_VTOPIC_TITLE
        defaultVtopicShouldBeFound("vtopicTitle.contains=" + DEFAULT_VTOPIC_TITLE);

        // Get all the vtopicList where vtopicTitle contains UPDATED_VTOPIC_TITLE
        defaultVtopicShouldNotBeFound("vtopicTitle.contains=" + UPDATED_VTOPIC_TITLE);
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicTitleNotContainsSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicTitle does not contain DEFAULT_VTOPIC_TITLE
        defaultVtopicShouldNotBeFound("vtopicTitle.doesNotContain=" + DEFAULT_VTOPIC_TITLE);

        // Get all the vtopicList where vtopicTitle does not contain UPDATED_VTOPIC_TITLE
        defaultVtopicShouldBeFound("vtopicTitle.doesNotContain=" + UPDATED_VTOPIC_TITLE);
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicDescription equals to DEFAULT_VTOPIC_DESCRIPTION
        defaultVtopicShouldBeFound("vtopicDescription.equals=" + DEFAULT_VTOPIC_DESCRIPTION);

        // Get all the vtopicList where vtopicDescription equals to UPDATED_VTOPIC_DESCRIPTION
        defaultVtopicShouldNotBeFound("vtopicDescription.equals=" + UPDATED_VTOPIC_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicDescription not equals to DEFAULT_VTOPIC_DESCRIPTION
        defaultVtopicShouldNotBeFound("vtopicDescription.notEquals=" + DEFAULT_VTOPIC_DESCRIPTION);

        // Get all the vtopicList where vtopicDescription not equals to UPDATED_VTOPIC_DESCRIPTION
        defaultVtopicShouldBeFound("vtopicDescription.notEquals=" + UPDATED_VTOPIC_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicDescription in DEFAULT_VTOPIC_DESCRIPTION or UPDATED_VTOPIC_DESCRIPTION
        defaultVtopicShouldBeFound("vtopicDescription.in=" + DEFAULT_VTOPIC_DESCRIPTION + "," + UPDATED_VTOPIC_DESCRIPTION);

        // Get all the vtopicList where vtopicDescription equals to UPDATED_VTOPIC_DESCRIPTION
        defaultVtopicShouldNotBeFound("vtopicDescription.in=" + UPDATED_VTOPIC_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicDescription is not null
        defaultVtopicShouldBeFound("vtopicDescription.specified=true");

        // Get all the vtopicList where vtopicDescription is null
        defaultVtopicShouldNotBeFound("vtopicDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicDescriptionContainsSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicDescription contains DEFAULT_VTOPIC_DESCRIPTION
        defaultVtopicShouldBeFound("vtopicDescription.contains=" + DEFAULT_VTOPIC_DESCRIPTION);

        // Get all the vtopicList where vtopicDescription contains UPDATED_VTOPIC_DESCRIPTION
        defaultVtopicShouldNotBeFound("vtopicDescription.contains=" + UPDATED_VTOPIC_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVtopicsByVtopicDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        // Get all the vtopicList where vtopicDescription does not contain DEFAULT_VTOPIC_DESCRIPTION
        defaultVtopicShouldNotBeFound("vtopicDescription.doesNotContain=" + DEFAULT_VTOPIC_DESCRIPTION);

        // Get all the vtopicList where vtopicDescription does not contain UPDATED_VTOPIC_DESCRIPTION
        defaultVtopicShouldBeFound("vtopicDescription.doesNotContain=" + UPDATED_VTOPIC_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVtopicsByVquestionIsEqualToSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);
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
        vtopic.addVquestion(vquestion);
        vtopicRepository.saveAndFlush(vtopic);
        Long vquestionId = vquestion.getId();

        // Get all the vtopicList where vquestion equals to vquestionId
        defaultVtopicShouldBeFound("vquestionId.equals=" + vquestionId);

        // Get all the vtopicList where vquestion equals to (vquestionId + 1)
        defaultVtopicShouldNotBeFound("vquestionId.equals=" + (vquestionId + 1));
    }

    @Test
    @Transactional
    void getAllVtopicsByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);
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
        vtopic.setAppuser(appuser);
        vtopicRepository.saveAndFlush(vtopic);
        Long appuserId = appuser.getId();

        // Get all the vtopicList where appuser equals to appuserId
        defaultVtopicShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the vtopicList where appuser equals to (appuserId + 1)
        defaultVtopicShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVtopicShouldBeFound(String filter) throws Exception {
        restVtopicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vtopic.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vtopicTitle").value(hasItem(DEFAULT_VTOPIC_TITLE)))
            .andExpect(jsonPath("$.[*].vtopicDescription").value(hasItem(DEFAULT_VTOPIC_DESCRIPTION)));

        // Check, that the count call also returns 1
        restVtopicMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVtopicShouldNotBeFound(String filter) throws Exception {
        restVtopicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVtopicMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVtopic() throws Exception {
        // Get the vtopic
        restVtopicMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVtopic() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        int databaseSizeBeforeUpdate = vtopicRepository.findAll().size();

        // Update the vtopic
        Vtopic updatedVtopic = vtopicRepository.findById(vtopic.getId()).get();
        // Disconnect from session so that the updates on updatedVtopic are not directly saved in db
        em.detach(updatedVtopic);
        updatedVtopic.creationDate(UPDATED_CREATION_DATE).vtopicTitle(UPDATED_VTOPIC_TITLE).vtopicDescription(UPDATED_VTOPIC_DESCRIPTION);
        VtopicDTO vtopicDTO = vtopicMapper.toDto(updatedVtopic);

        restVtopicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vtopicDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vtopicDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeUpdate);
        Vtopic testVtopic = vtopicList.get(vtopicList.size() - 1);
        assertThat(testVtopic.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVtopic.getVtopicTitle()).isEqualTo(UPDATED_VTOPIC_TITLE);
        assertThat(testVtopic.getVtopicDescription()).isEqualTo(UPDATED_VTOPIC_DESCRIPTION);

        // Validate the Vtopic in Elasticsearch
        verify(mockVtopicSearchRepository).save(testVtopic);
    }

    @Test
    @Transactional
    void putNonExistingVtopic() throws Exception {
        int databaseSizeBeforeUpdate = vtopicRepository.findAll().size();
        vtopic.setId(count.incrementAndGet());

        // Create the Vtopic
        VtopicDTO vtopicDTO = vtopicMapper.toDto(vtopic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVtopicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vtopicDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vtopicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vtopic in Elasticsearch
        verify(mockVtopicSearchRepository, times(0)).save(vtopic);
    }

    @Test
    @Transactional
    void putWithIdMismatchVtopic() throws Exception {
        int databaseSizeBeforeUpdate = vtopicRepository.findAll().size();
        vtopic.setId(count.incrementAndGet());

        // Create the Vtopic
        VtopicDTO vtopicDTO = vtopicMapper.toDto(vtopic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVtopicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vtopicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vtopic in Elasticsearch
        verify(mockVtopicSearchRepository, times(0)).save(vtopic);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVtopic() throws Exception {
        int databaseSizeBeforeUpdate = vtopicRepository.findAll().size();
        vtopic.setId(count.incrementAndGet());

        // Create the Vtopic
        VtopicDTO vtopicDTO = vtopicMapper.toDto(vtopic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVtopicMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vtopicDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vtopic in Elasticsearch
        verify(mockVtopicSearchRepository, times(0)).save(vtopic);
    }

    @Test
    @Transactional
    void partialUpdateVtopicWithPatch() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        int databaseSizeBeforeUpdate = vtopicRepository.findAll().size();

        // Update the vtopic using partial update
        Vtopic partialUpdatedVtopic = new Vtopic();
        partialUpdatedVtopic.setId(vtopic.getId());

        partialUpdatedVtopic.creationDate(UPDATED_CREATION_DATE);

        restVtopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVtopic.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVtopic))
            )
            .andExpect(status().isOk());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeUpdate);
        Vtopic testVtopic = vtopicList.get(vtopicList.size() - 1);
        assertThat(testVtopic.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVtopic.getVtopicTitle()).isEqualTo(DEFAULT_VTOPIC_TITLE);
        assertThat(testVtopic.getVtopicDescription()).isEqualTo(DEFAULT_VTOPIC_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateVtopicWithPatch() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        int databaseSizeBeforeUpdate = vtopicRepository.findAll().size();

        // Update the vtopic using partial update
        Vtopic partialUpdatedVtopic = new Vtopic();
        partialUpdatedVtopic.setId(vtopic.getId());

        partialUpdatedVtopic
            .creationDate(UPDATED_CREATION_DATE)
            .vtopicTitle(UPDATED_VTOPIC_TITLE)
            .vtopicDescription(UPDATED_VTOPIC_DESCRIPTION);

        restVtopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVtopic.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVtopic))
            )
            .andExpect(status().isOk());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeUpdate);
        Vtopic testVtopic = vtopicList.get(vtopicList.size() - 1);
        assertThat(testVtopic.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVtopic.getVtopicTitle()).isEqualTo(UPDATED_VTOPIC_TITLE);
        assertThat(testVtopic.getVtopicDescription()).isEqualTo(UPDATED_VTOPIC_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingVtopic() throws Exception {
        int databaseSizeBeforeUpdate = vtopicRepository.findAll().size();
        vtopic.setId(count.incrementAndGet());

        // Create the Vtopic
        VtopicDTO vtopicDTO = vtopicMapper.toDto(vtopic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVtopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vtopicDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vtopicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vtopic in Elasticsearch
        verify(mockVtopicSearchRepository, times(0)).save(vtopic);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVtopic() throws Exception {
        int databaseSizeBeforeUpdate = vtopicRepository.findAll().size();
        vtopic.setId(count.incrementAndGet());

        // Create the Vtopic
        VtopicDTO vtopicDTO = vtopicMapper.toDto(vtopic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVtopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vtopicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vtopic in Elasticsearch
        verify(mockVtopicSearchRepository, times(0)).save(vtopic);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVtopic() throws Exception {
        int databaseSizeBeforeUpdate = vtopicRepository.findAll().size();
        vtopic.setId(count.incrementAndGet());

        // Create the Vtopic
        VtopicDTO vtopicDTO = vtopicMapper.toDto(vtopic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVtopicMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vtopicDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vtopic in the database
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vtopic in Elasticsearch
        verify(mockVtopicSearchRepository, times(0)).save(vtopic);
    }

    @Test
    @Transactional
    void deleteVtopic() throws Exception {
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);

        int databaseSizeBeforeDelete = vtopicRepository.findAll().size();

        // Delete the vtopic
        restVtopicMockMvc
            .perform(delete(ENTITY_API_URL_ID, vtopic.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vtopic> vtopicList = vtopicRepository.findAll();
        assertThat(vtopicList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Vtopic in Elasticsearch
        verify(mockVtopicSearchRepository, times(1)).deleteById(vtopic.getId());
    }

    @Test
    @Transactional
    void searchVtopic() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        vtopicRepository.saveAndFlush(vtopic);
        when(mockVtopicSearchRepository.search("id:" + vtopic.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(vtopic), PageRequest.of(0, 1), 1));

        // Search the vtopic
        restVtopicMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + vtopic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vtopic.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vtopicTitle").value(hasItem(DEFAULT_VTOPIC_TITLE)))
            .andExpect(jsonPath("$.[*].vtopicDescription").value(hasItem(DEFAULT_VTOPIC_DESCRIPTION)));
    }
}
