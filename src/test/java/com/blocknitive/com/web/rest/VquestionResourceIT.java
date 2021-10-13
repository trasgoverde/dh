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
import com.blocknitive.com.domain.Vtopic;
import com.blocknitive.com.repository.VquestionRepository;
import com.blocknitive.com.repository.search.VquestionSearchRepository;
import com.blocknitive.com.service.criteria.VquestionCriteria;
import com.blocknitive.com.service.dto.VquestionDTO;
import com.blocknitive.com.service.mapper.VquestionMapper;
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
 * Integration tests for the {@link VquestionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VquestionResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VQUESTION = "AAAAAAAAAA";
    private static final String UPDATED_VQUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_VQUESTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_VQUESTION_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vquestions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/vquestions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VquestionRepository vquestionRepository;

    @Autowired
    private VquestionMapper vquestionMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.VquestionSearchRepositoryMockConfiguration
     */
    @Autowired
    private VquestionSearchRepository mockVquestionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVquestionMockMvc;

    private Vquestion vquestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vquestion createEntity(EntityManager em) {
        Vquestion vquestion = new Vquestion()
            .creationDate(DEFAULT_CREATION_DATE)
            .vquestion(DEFAULT_VQUESTION)
            .vquestionDescription(DEFAULT_VQUESTION_DESCRIPTION);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vquestion.setAppuser(appuser);
        // Add required entity
        Vtopic vtopic;
        if (TestUtil.findAll(em, Vtopic.class).isEmpty()) {
            vtopic = VtopicResourceIT.createEntity(em);
            em.persist(vtopic);
            em.flush();
        } else {
            vtopic = TestUtil.findAll(em, Vtopic.class).get(0);
        }
        vquestion.setVtopic(vtopic);
        return vquestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vquestion createUpdatedEntity(EntityManager em) {
        Vquestion vquestion = new Vquestion()
            .creationDate(UPDATED_CREATION_DATE)
            .vquestion(UPDATED_VQUESTION)
            .vquestionDescription(UPDATED_VQUESTION_DESCRIPTION);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vquestion.setAppuser(appuser);
        // Add required entity
        Vtopic vtopic;
        if (TestUtil.findAll(em, Vtopic.class).isEmpty()) {
            vtopic = VtopicResourceIT.createUpdatedEntity(em);
            em.persist(vtopic);
            em.flush();
        } else {
            vtopic = TestUtil.findAll(em, Vtopic.class).get(0);
        }
        vquestion.setVtopic(vtopic);
        return vquestion;
    }

    @BeforeEach
    public void initTest() {
        vquestion = createEntity(em);
    }

    @Test
    @Transactional
    void createVquestion() throws Exception {
        int databaseSizeBeforeCreate = vquestionRepository.findAll().size();
        // Create the Vquestion
        VquestionDTO vquestionDTO = vquestionMapper.toDto(vquestion);
        restVquestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vquestionDTO)))
            .andExpect(status().isCreated());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeCreate + 1);
        Vquestion testVquestion = vquestionList.get(vquestionList.size() - 1);
        assertThat(testVquestion.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testVquestion.getVquestion()).isEqualTo(DEFAULT_VQUESTION);
        assertThat(testVquestion.getVquestionDescription()).isEqualTo(DEFAULT_VQUESTION_DESCRIPTION);

        // Validate the Vquestion in Elasticsearch
        verify(mockVquestionSearchRepository, times(1)).save(testVquestion);
    }

    @Test
    @Transactional
    void createVquestionWithExistingId() throws Exception {
        // Create the Vquestion with an existing ID
        vquestion.setId(1L);
        VquestionDTO vquestionDTO = vquestionMapper.toDto(vquestion);

        int databaseSizeBeforeCreate = vquestionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVquestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vquestionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Vquestion in Elasticsearch
        verify(mockVquestionSearchRepository, times(0)).save(vquestion);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vquestionRepository.findAll().size();
        // set the field null
        vquestion.setCreationDate(null);

        // Create the Vquestion, which fails.
        VquestionDTO vquestionDTO = vquestionMapper.toDto(vquestion);

        restVquestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vquestionDTO)))
            .andExpect(status().isBadRequest());

        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVquestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = vquestionRepository.findAll().size();
        // set the field null
        vquestion.setVquestion(null);

        // Create the Vquestion, which fails.
        VquestionDTO vquestionDTO = vquestionMapper.toDto(vquestion);

        restVquestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vquestionDTO)))
            .andExpect(status().isBadRequest());

        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVquestions() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList
        restVquestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vquestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vquestion").value(hasItem(DEFAULT_VQUESTION)))
            .andExpect(jsonPath("$.[*].vquestionDescription").value(hasItem(DEFAULT_VQUESTION_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getVquestion() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get the vquestion
        restVquestionMockMvc
            .perform(get(ENTITY_API_URL_ID, vquestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vquestion.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.vquestion").value(DEFAULT_VQUESTION))
            .andExpect(jsonPath("$.vquestionDescription").value(DEFAULT_VQUESTION_DESCRIPTION));
    }

    @Test
    @Transactional
    void getVquestionsByIdFiltering() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        Long id = vquestion.getId();

        defaultVquestionShouldBeFound("id.equals=" + id);
        defaultVquestionShouldNotBeFound("id.notEquals=" + id);

        defaultVquestionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVquestionShouldNotBeFound("id.greaterThan=" + id);

        defaultVquestionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVquestionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVquestionsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where creationDate equals to DEFAULT_CREATION_DATE
        defaultVquestionShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the vquestionList where creationDate equals to UPDATED_CREATION_DATE
        defaultVquestionShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVquestionsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultVquestionShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the vquestionList where creationDate not equals to UPDATED_CREATION_DATE
        defaultVquestionShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVquestionsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultVquestionShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the vquestionList where creationDate equals to UPDATED_CREATION_DATE
        defaultVquestionShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVquestionsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where creationDate is not null
        defaultVquestionShouldBeFound("creationDate.specified=true");

        // Get all the vquestionList where creationDate is null
        defaultVquestionShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionIsEqualToSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestion equals to DEFAULT_VQUESTION
        defaultVquestionShouldBeFound("vquestion.equals=" + DEFAULT_VQUESTION);

        // Get all the vquestionList where vquestion equals to UPDATED_VQUESTION
        defaultVquestionShouldNotBeFound("vquestion.equals=" + UPDATED_VQUESTION);
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestion not equals to DEFAULT_VQUESTION
        defaultVquestionShouldNotBeFound("vquestion.notEquals=" + DEFAULT_VQUESTION);

        // Get all the vquestionList where vquestion not equals to UPDATED_VQUESTION
        defaultVquestionShouldBeFound("vquestion.notEquals=" + UPDATED_VQUESTION);
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionIsInShouldWork() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestion in DEFAULT_VQUESTION or UPDATED_VQUESTION
        defaultVquestionShouldBeFound("vquestion.in=" + DEFAULT_VQUESTION + "," + UPDATED_VQUESTION);

        // Get all the vquestionList where vquestion equals to UPDATED_VQUESTION
        defaultVquestionShouldNotBeFound("vquestion.in=" + UPDATED_VQUESTION);
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionIsNullOrNotNull() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestion is not null
        defaultVquestionShouldBeFound("vquestion.specified=true");

        // Get all the vquestionList where vquestion is null
        defaultVquestionShouldNotBeFound("vquestion.specified=false");
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionContainsSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestion contains DEFAULT_VQUESTION
        defaultVquestionShouldBeFound("vquestion.contains=" + DEFAULT_VQUESTION);

        // Get all the vquestionList where vquestion contains UPDATED_VQUESTION
        defaultVquestionShouldNotBeFound("vquestion.contains=" + UPDATED_VQUESTION);
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionNotContainsSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestion does not contain DEFAULT_VQUESTION
        defaultVquestionShouldNotBeFound("vquestion.doesNotContain=" + DEFAULT_VQUESTION);

        // Get all the vquestionList where vquestion does not contain UPDATED_VQUESTION
        defaultVquestionShouldBeFound("vquestion.doesNotContain=" + UPDATED_VQUESTION);
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestionDescription equals to DEFAULT_VQUESTION_DESCRIPTION
        defaultVquestionShouldBeFound("vquestionDescription.equals=" + DEFAULT_VQUESTION_DESCRIPTION);

        // Get all the vquestionList where vquestionDescription equals to UPDATED_VQUESTION_DESCRIPTION
        defaultVquestionShouldNotBeFound("vquestionDescription.equals=" + UPDATED_VQUESTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestionDescription not equals to DEFAULT_VQUESTION_DESCRIPTION
        defaultVquestionShouldNotBeFound("vquestionDescription.notEquals=" + DEFAULT_VQUESTION_DESCRIPTION);

        // Get all the vquestionList where vquestionDescription not equals to UPDATED_VQUESTION_DESCRIPTION
        defaultVquestionShouldBeFound("vquestionDescription.notEquals=" + UPDATED_VQUESTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestionDescription in DEFAULT_VQUESTION_DESCRIPTION or UPDATED_VQUESTION_DESCRIPTION
        defaultVquestionShouldBeFound("vquestionDescription.in=" + DEFAULT_VQUESTION_DESCRIPTION + "," + UPDATED_VQUESTION_DESCRIPTION);

        // Get all the vquestionList where vquestionDescription equals to UPDATED_VQUESTION_DESCRIPTION
        defaultVquestionShouldNotBeFound("vquestionDescription.in=" + UPDATED_VQUESTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestionDescription is not null
        defaultVquestionShouldBeFound("vquestionDescription.specified=true");

        // Get all the vquestionList where vquestionDescription is null
        defaultVquestionShouldNotBeFound("vquestionDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestionDescription contains DEFAULT_VQUESTION_DESCRIPTION
        defaultVquestionShouldBeFound("vquestionDescription.contains=" + DEFAULT_VQUESTION_DESCRIPTION);

        // Get all the vquestionList where vquestionDescription contains UPDATED_VQUESTION_DESCRIPTION
        defaultVquestionShouldNotBeFound("vquestionDescription.contains=" + UPDATED_VQUESTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVquestionsByVquestionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        // Get all the vquestionList where vquestionDescription does not contain DEFAULT_VQUESTION_DESCRIPTION
        defaultVquestionShouldNotBeFound("vquestionDescription.doesNotContain=" + DEFAULT_VQUESTION_DESCRIPTION);

        // Get all the vquestionList where vquestionDescription does not contain UPDATED_VQUESTION_DESCRIPTION
        defaultVquestionShouldBeFound("vquestionDescription.doesNotContain=" + UPDATED_VQUESTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVquestionsByVanswerIsEqualToSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);
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
        vquestion.addVanswer(vanswer);
        vquestionRepository.saveAndFlush(vquestion);
        Long vanswerId = vanswer.getId();

        // Get all the vquestionList where vanswer equals to vanswerId
        defaultVquestionShouldBeFound("vanswerId.equals=" + vanswerId);

        // Get all the vquestionList where vanswer equals to (vanswerId + 1)
        defaultVquestionShouldNotBeFound("vanswerId.equals=" + (vanswerId + 1));
    }

    @Test
    @Transactional
    void getAllVquestionsByVthumbIsEqualToSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);
        Vthumb vthumb;
        if (TestUtil.findAll(em, Vthumb.class).isEmpty()) {
            vthumb = VthumbResourceIT.createEntity(em);
            em.persist(vthumb);
            em.flush();
        } else {
            vthumb = TestUtil.findAll(em, Vthumb.class).get(0);
        }
        em.persist(vthumb);
        em.flush();
        vquestion.addVthumb(vthumb);
        vquestionRepository.saveAndFlush(vquestion);
        Long vthumbId = vthumb.getId();

        // Get all the vquestionList where vthumb equals to vthumbId
        defaultVquestionShouldBeFound("vthumbId.equals=" + vthumbId);

        // Get all the vquestionList where vthumb equals to (vthumbId + 1)
        defaultVquestionShouldNotBeFound("vthumbId.equals=" + (vthumbId + 1));
    }

    @Test
    @Transactional
    void getAllVquestionsByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);
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
        vquestion.setAppuser(appuser);
        vquestionRepository.saveAndFlush(vquestion);
        Long appuserId = appuser.getId();

        // Get all the vquestionList where appuser equals to appuserId
        defaultVquestionShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the vquestionList where appuser equals to (appuserId + 1)
        defaultVquestionShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    @Test
    @Transactional
    void getAllVquestionsByVtopicIsEqualToSomething() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);
        Vtopic vtopic;
        if (TestUtil.findAll(em, Vtopic.class).isEmpty()) {
            vtopic = VtopicResourceIT.createEntity(em);
            em.persist(vtopic);
            em.flush();
        } else {
            vtopic = TestUtil.findAll(em, Vtopic.class).get(0);
        }
        em.persist(vtopic);
        em.flush();
        vquestion.setVtopic(vtopic);
        vquestionRepository.saveAndFlush(vquestion);
        Long vtopicId = vtopic.getId();

        // Get all the vquestionList where vtopic equals to vtopicId
        defaultVquestionShouldBeFound("vtopicId.equals=" + vtopicId);

        // Get all the vquestionList where vtopic equals to (vtopicId + 1)
        defaultVquestionShouldNotBeFound("vtopicId.equals=" + (vtopicId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVquestionShouldBeFound(String filter) throws Exception {
        restVquestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vquestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vquestion").value(hasItem(DEFAULT_VQUESTION)))
            .andExpect(jsonPath("$.[*].vquestionDescription").value(hasItem(DEFAULT_VQUESTION_DESCRIPTION)));

        // Check, that the count call also returns 1
        restVquestionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVquestionShouldNotBeFound(String filter) throws Exception {
        restVquestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVquestionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVquestion() throws Exception {
        // Get the vquestion
        restVquestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVquestion() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        int databaseSizeBeforeUpdate = vquestionRepository.findAll().size();

        // Update the vquestion
        Vquestion updatedVquestion = vquestionRepository.findById(vquestion.getId()).get();
        // Disconnect from session so that the updates on updatedVquestion are not directly saved in db
        em.detach(updatedVquestion);
        updatedVquestion
            .creationDate(UPDATED_CREATION_DATE)
            .vquestion(UPDATED_VQUESTION)
            .vquestionDescription(UPDATED_VQUESTION_DESCRIPTION);
        VquestionDTO vquestionDTO = vquestionMapper.toDto(updatedVquestion);

        restVquestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vquestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vquestionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeUpdate);
        Vquestion testVquestion = vquestionList.get(vquestionList.size() - 1);
        assertThat(testVquestion.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVquestion.getVquestion()).isEqualTo(UPDATED_VQUESTION);
        assertThat(testVquestion.getVquestionDescription()).isEqualTo(UPDATED_VQUESTION_DESCRIPTION);

        // Validate the Vquestion in Elasticsearch
        verify(mockVquestionSearchRepository).save(testVquestion);
    }

    @Test
    @Transactional
    void putNonExistingVquestion() throws Exception {
        int databaseSizeBeforeUpdate = vquestionRepository.findAll().size();
        vquestion.setId(count.incrementAndGet());

        // Create the Vquestion
        VquestionDTO vquestionDTO = vquestionMapper.toDto(vquestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVquestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vquestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vquestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vquestion in Elasticsearch
        verify(mockVquestionSearchRepository, times(0)).save(vquestion);
    }

    @Test
    @Transactional
    void putWithIdMismatchVquestion() throws Exception {
        int databaseSizeBeforeUpdate = vquestionRepository.findAll().size();
        vquestion.setId(count.incrementAndGet());

        // Create the Vquestion
        VquestionDTO vquestionDTO = vquestionMapper.toDto(vquestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVquestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vquestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vquestion in Elasticsearch
        verify(mockVquestionSearchRepository, times(0)).save(vquestion);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVquestion() throws Exception {
        int databaseSizeBeforeUpdate = vquestionRepository.findAll().size();
        vquestion.setId(count.incrementAndGet());

        // Create the Vquestion
        VquestionDTO vquestionDTO = vquestionMapper.toDto(vquestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVquestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vquestionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vquestion in Elasticsearch
        verify(mockVquestionSearchRepository, times(0)).save(vquestion);
    }

    @Test
    @Transactional
    void partialUpdateVquestionWithPatch() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        int databaseSizeBeforeUpdate = vquestionRepository.findAll().size();

        // Update the vquestion using partial update
        Vquestion partialUpdatedVquestion = new Vquestion();
        partialUpdatedVquestion.setId(vquestion.getId());

        partialUpdatedVquestion.creationDate(UPDATED_CREATION_DATE).vquestionDescription(UPDATED_VQUESTION_DESCRIPTION);

        restVquestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVquestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVquestion))
            )
            .andExpect(status().isOk());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeUpdate);
        Vquestion testVquestion = vquestionList.get(vquestionList.size() - 1);
        assertThat(testVquestion.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVquestion.getVquestion()).isEqualTo(DEFAULT_VQUESTION);
        assertThat(testVquestion.getVquestionDescription()).isEqualTo(UPDATED_VQUESTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateVquestionWithPatch() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        int databaseSizeBeforeUpdate = vquestionRepository.findAll().size();

        // Update the vquestion using partial update
        Vquestion partialUpdatedVquestion = new Vquestion();
        partialUpdatedVquestion.setId(vquestion.getId());

        partialUpdatedVquestion
            .creationDate(UPDATED_CREATION_DATE)
            .vquestion(UPDATED_VQUESTION)
            .vquestionDescription(UPDATED_VQUESTION_DESCRIPTION);

        restVquestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVquestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVquestion))
            )
            .andExpect(status().isOk());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeUpdate);
        Vquestion testVquestion = vquestionList.get(vquestionList.size() - 1);
        assertThat(testVquestion.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVquestion.getVquestion()).isEqualTo(UPDATED_VQUESTION);
        assertThat(testVquestion.getVquestionDescription()).isEqualTo(UPDATED_VQUESTION_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingVquestion() throws Exception {
        int databaseSizeBeforeUpdate = vquestionRepository.findAll().size();
        vquestion.setId(count.incrementAndGet());

        // Create the Vquestion
        VquestionDTO vquestionDTO = vquestionMapper.toDto(vquestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVquestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vquestionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vquestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vquestion in Elasticsearch
        verify(mockVquestionSearchRepository, times(0)).save(vquestion);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVquestion() throws Exception {
        int databaseSizeBeforeUpdate = vquestionRepository.findAll().size();
        vquestion.setId(count.incrementAndGet());

        // Create the Vquestion
        VquestionDTO vquestionDTO = vquestionMapper.toDto(vquestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVquestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vquestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vquestion in Elasticsearch
        verify(mockVquestionSearchRepository, times(0)).save(vquestion);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVquestion() throws Exception {
        int databaseSizeBeforeUpdate = vquestionRepository.findAll().size();
        vquestion.setId(count.incrementAndGet());

        // Create the Vquestion
        VquestionDTO vquestionDTO = vquestionMapper.toDto(vquestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVquestionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vquestionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vquestion in the database
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vquestion in Elasticsearch
        verify(mockVquestionSearchRepository, times(0)).save(vquestion);
    }

    @Test
    @Transactional
    void deleteVquestion() throws Exception {
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);

        int databaseSizeBeforeDelete = vquestionRepository.findAll().size();

        // Delete the vquestion
        restVquestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, vquestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vquestion> vquestionList = vquestionRepository.findAll();
        assertThat(vquestionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Vquestion in Elasticsearch
        verify(mockVquestionSearchRepository, times(1)).deleteById(vquestion.getId());
    }

    @Test
    @Transactional
    void searchVquestion() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        vquestionRepository.saveAndFlush(vquestion);
        when(mockVquestionSearchRepository.search("id:" + vquestion.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(vquestion), PageRequest.of(0, 1), 1));

        // Search the vquestion
        restVquestionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + vquestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vquestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].vquestion").value(hasItem(DEFAULT_VQUESTION)))
            .andExpect(jsonPath("$.[*].vquestionDescription").value(hasItem(DEFAULT_VQUESTION_DESCRIPTION)));
    }
}
