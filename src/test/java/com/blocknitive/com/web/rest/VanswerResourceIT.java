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
import com.blocknitive.com.repository.VanswerRepository;
import com.blocknitive.com.repository.search.VanswerSearchRepository;
import com.blocknitive.com.service.criteria.VanswerCriteria;
import com.blocknitive.com.service.dto.VanswerDTO;
import com.blocknitive.com.service.mapper.VanswerMapper;
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
 * Integration tests for the {@link VanswerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VanswerResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_URL_VANSWER = "AAAAAAAAAA";
    private static final String UPDATED_URL_VANSWER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    private static final String ENTITY_API_URL = "/api/vanswers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/vanswers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VanswerRepository vanswerRepository;

    @Autowired
    private VanswerMapper vanswerMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.VanswerSearchRepositoryMockConfiguration
     */
    @Autowired
    private VanswerSearchRepository mockVanswerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVanswerMockMvc;

    private Vanswer vanswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vanswer createEntity(EntityManager em) {
        Vanswer vanswer = new Vanswer().creationDate(DEFAULT_CREATION_DATE).urlVanswer(DEFAULT_URL_VANSWER).accepted(DEFAULT_ACCEPTED);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vanswer.setAppuser(appuser);
        // Add required entity
        Vquestion vquestion;
        if (TestUtil.findAll(em, Vquestion.class).isEmpty()) {
            vquestion = VquestionResourceIT.createEntity(em);
            em.persist(vquestion);
            em.flush();
        } else {
            vquestion = TestUtil.findAll(em, Vquestion.class).get(0);
        }
        vanswer.setVquestion(vquestion);
        return vanswer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vanswer createUpdatedEntity(EntityManager em) {
        Vanswer vanswer = new Vanswer().creationDate(UPDATED_CREATION_DATE).urlVanswer(UPDATED_URL_VANSWER).accepted(UPDATED_ACCEPTED);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        vanswer.setAppuser(appuser);
        // Add required entity
        Vquestion vquestion;
        if (TestUtil.findAll(em, Vquestion.class).isEmpty()) {
            vquestion = VquestionResourceIT.createUpdatedEntity(em);
            em.persist(vquestion);
            em.flush();
        } else {
            vquestion = TestUtil.findAll(em, Vquestion.class).get(0);
        }
        vanswer.setVquestion(vquestion);
        return vanswer;
    }

    @BeforeEach
    public void initTest() {
        vanswer = createEntity(em);
    }

    @Test
    @Transactional
    void createVanswer() throws Exception {
        int databaseSizeBeforeCreate = vanswerRepository.findAll().size();
        // Create the Vanswer
        VanswerDTO vanswerDTO = vanswerMapper.toDto(vanswer);
        restVanswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vanswerDTO)))
            .andExpect(status().isCreated());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeCreate + 1);
        Vanswer testVanswer = vanswerList.get(vanswerList.size() - 1);
        assertThat(testVanswer.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testVanswer.getUrlVanswer()).isEqualTo(DEFAULT_URL_VANSWER);
        assertThat(testVanswer.getAccepted()).isEqualTo(DEFAULT_ACCEPTED);

        // Validate the Vanswer in Elasticsearch
        verify(mockVanswerSearchRepository, times(1)).save(testVanswer);
    }

    @Test
    @Transactional
    void createVanswerWithExistingId() throws Exception {
        // Create the Vanswer with an existing ID
        vanswer.setId(1L);
        VanswerDTO vanswerDTO = vanswerMapper.toDto(vanswer);

        int databaseSizeBeforeCreate = vanswerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVanswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vanswerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Vanswer in Elasticsearch
        verify(mockVanswerSearchRepository, times(0)).save(vanswer);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vanswerRepository.findAll().size();
        // set the field null
        vanswer.setCreationDate(null);

        // Create the Vanswer, which fails.
        VanswerDTO vanswerDTO = vanswerMapper.toDto(vanswer);

        restVanswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vanswerDTO)))
            .andExpect(status().isBadRequest());

        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlVanswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = vanswerRepository.findAll().size();
        // set the field null
        vanswer.setUrlVanswer(null);

        // Create the Vanswer, which fails.
        VanswerDTO vanswerDTO = vanswerMapper.toDto(vanswer);

        restVanswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vanswerDTO)))
            .andExpect(status().isBadRequest());

        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVanswers() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList
        restVanswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vanswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].urlVanswer").value(hasItem(DEFAULT_URL_VANSWER)))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())));
    }

    @Test
    @Transactional
    void getVanswer() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get the vanswer
        restVanswerMockMvc
            .perform(get(ENTITY_API_URL_ID, vanswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vanswer.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.urlVanswer").value(DEFAULT_URL_VANSWER))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()));
    }

    @Test
    @Transactional
    void getVanswersByIdFiltering() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        Long id = vanswer.getId();

        defaultVanswerShouldBeFound("id.equals=" + id);
        defaultVanswerShouldNotBeFound("id.notEquals=" + id);

        defaultVanswerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVanswerShouldNotBeFound("id.greaterThan=" + id);

        defaultVanswerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVanswerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVanswersByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where creationDate equals to DEFAULT_CREATION_DATE
        defaultVanswerShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the vanswerList where creationDate equals to UPDATED_CREATION_DATE
        defaultVanswerShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVanswersByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultVanswerShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the vanswerList where creationDate not equals to UPDATED_CREATION_DATE
        defaultVanswerShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVanswersByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultVanswerShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the vanswerList where creationDate equals to UPDATED_CREATION_DATE
        defaultVanswerShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllVanswersByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where creationDate is not null
        defaultVanswerShouldBeFound("creationDate.specified=true");

        // Get all the vanswerList where creationDate is null
        defaultVanswerShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVanswersByUrlVanswerIsEqualToSomething() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where urlVanswer equals to DEFAULT_URL_VANSWER
        defaultVanswerShouldBeFound("urlVanswer.equals=" + DEFAULT_URL_VANSWER);

        // Get all the vanswerList where urlVanswer equals to UPDATED_URL_VANSWER
        defaultVanswerShouldNotBeFound("urlVanswer.equals=" + UPDATED_URL_VANSWER);
    }

    @Test
    @Transactional
    void getAllVanswersByUrlVanswerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where urlVanswer not equals to DEFAULT_URL_VANSWER
        defaultVanswerShouldNotBeFound("urlVanswer.notEquals=" + DEFAULT_URL_VANSWER);

        // Get all the vanswerList where urlVanswer not equals to UPDATED_URL_VANSWER
        defaultVanswerShouldBeFound("urlVanswer.notEquals=" + UPDATED_URL_VANSWER);
    }

    @Test
    @Transactional
    void getAllVanswersByUrlVanswerIsInShouldWork() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where urlVanswer in DEFAULT_URL_VANSWER or UPDATED_URL_VANSWER
        defaultVanswerShouldBeFound("urlVanswer.in=" + DEFAULT_URL_VANSWER + "," + UPDATED_URL_VANSWER);

        // Get all the vanswerList where urlVanswer equals to UPDATED_URL_VANSWER
        defaultVanswerShouldNotBeFound("urlVanswer.in=" + UPDATED_URL_VANSWER);
    }

    @Test
    @Transactional
    void getAllVanswersByUrlVanswerIsNullOrNotNull() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where urlVanswer is not null
        defaultVanswerShouldBeFound("urlVanswer.specified=true");

        // Get all the vanswerList where urlVanswer is null
        defaultVanswerShouldNotBeFound("urlVanswer.specified=false");
    }

    @Test
    @Transactional
    void getAllVanswersByUrlVanswerContainsSomething() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where urlVanswer contains DEFAULT_URL_VANSWER
        defaultVanswerShouldBeFound("urlVanswer.contains=" + DEFAULT_URL_VANSWER);

        // Get all the vanswerList where urlVanswer contains UPDATED_URL_VANSWER
        defaultVanswerShouldNotBeFound("urlVanswer.contains=" + UPDATED_URL_VANSWER);
    }

    @Test
    @Transactional
    void getAllVanswersByUrlVanswerNotContainsSomething() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where urlVanswer does not contain DEFAULT_URL_VANSWER
        defaultVanswerShouldNotBeFound("urlVanswer.doesNotContain=" + DEFAULT_URL_VANSWER);

        // Get all the vanswerList where urlVanswer does not contain UPDATED_URL_VANSWER
        defaultVanswerShouldBeFound("urlVanswer.doesNotContain=" + UPDATED_URL_VANSWER);
    }

    @Test
    @Transactional
    void getAllVanswersByAcceptedIsEqualToSomething() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where accepted equals to DEFAULT_ACCEPTED
        defaultVanswerShouldBeFound("accepted.equals=" + DEFAULT_ACCEPTED);

        // Get all the vanswerList where accepted equals to UPDATED_ACCEPTED
        defaultVanswerShouldNotBeFound("accepted.equals=" + UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    void getAllVanswersByAcceptedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where accepted not equals to DEFAULT_ACCEPTED
        defaultVanswerShouldNotBeFound("accepted.notEquals=" + DEFAULT_ACCEPTED);

        // Get all the vanswerList where accepted not equals to UPDATED_ACCEPTED
        defaultVanswerShouldBeFound("accepted.notEquals=" + UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    void getAllVanswersByAcceptedIsInShouldWork() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where accepted in DEFAULT_ACCEPTED or UPDATED_ACCEPTED
        defaultVanswerShouldBeFound("accepted.in=" + DEFAULT_ACCEPTED + "," + UPDATED_ACCEPTED);

        // Get all the vanswerList where accepted equals to UPDATED_ACCEPTED
        defaultVanswerShouldNotBeFound("accepted.in=" + UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    void getAllVanswersByAcceptedIsNullOrNotNull() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        // Get all the vanswerList where accepted is not null
        defaultVanswerShouldBeFound("accepted.specified=true");

        // Get all the vanswerList where accepted is null
        defaultVanswerShouldNotBeFound("accepted.specified=false");
    }

    @Test
    @Transactional
    void getAllVanswersByVthumbIsEqualToSomething() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);
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
        vanswer.addVthumb(vthumb);
        vanswerRepository.saveAndFlush(vanswer);
        Long vthumbId = vthumb.getId();

        // Get all the vanswerList where vthumb equals to vthumbId
        defaultVanswerShouldBeFound("vthumbId.equals=" + vthumbId);

        // Get all the vanswerList where vthumb equals to (vthumbId + 1)
        defaultVanswerShouldNotBeFound("vthumbId.equals=" + (vthumbId + 1));
    }

    @Test
    @Transactional
    void getAllVanswersByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);
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
        vanswer.setAppuser(appuser);
        vanswerRepository.saveAndFlush(vanswer);
        Long appuserId = appuser.getId();

        // Get all the vanswerList where appuser equals to appuserId
        defaultVanswerShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the vanswerList where appuser equals to (appuserId + 1)
        defaultVanswerShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    @Test
    @Transactional
    void getAllVanswersByVquestionIsEqualToSomething() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);
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
        vanswer.setVquestion(vquestion);
        vanswerRepository.saveAndFlush(vanswer);
        Long vquestionId = vquestion.getId();

        // Get all the vanswerList where vquestion equals to vquestionId
        defaultVanswerShouldBeFound("vquestionId.equals=" + vquestionId);

        // Get all the vanswerList where vquestion equals to (vquestionId + 1)
        defaultVanswerShouldNotBeFound("vquestionId.equals=" + (vquestionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVanswerShouldBeFound(String filter) throws Exception {
        restVanswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vanswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].urlVanswer").value(hasItem(DEFAULT_URL_VANSWER)))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())));

        // Check, that the count call also returns 1
        restVanswerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVanswerShouldNotBeFound(String filter) throws Exception {
        restVanswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVanswerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVanswer() throws Exception {
        // Get the vanswer
        restVanswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVanswer() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        int databaseSizeBeforeUpdate = vanswerRepository.findAll().size();

        // Update the vanswer
        Vanswer updatedVanswer = vanswerRepository.findById(vanswer.getId()).get();
        // Disconnect from session so that the updates on updatedVanswer are not directly saved in db
        em.detach(updatedVanswer);
        updatedVanswer.creationDate(UPDATED_CREATION_DATE).urlVanswer(UPDATED_URL_VANSWER).accepted(UPDATED_ACCEPTED);
        VanswerDTO vanswerDTO = vanswerMapper.toDto(updatedVanswer);

        restVanswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vanswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vanswerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeUpdate);
        Vanswer testVanswer = vanswerList.get(vanswerList.size() - 1);
        assertThat(testVanswer.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVanswer.getUrlVanswer()).isEqualTo(UPDATED_URL_VANSWER);
        assertThat(testVanswer.getAccepted()).isEqualTo(UPDATED_ACCEPTED);

        // Validate the Vanswer in Elasticsearch
        verify(mockVanswerSearchRepository).save(testVanswer);
    }

    @Test
    @Transactional
    void putNonExistingVanswer() throws Exception {
        int databaseSizeBeforeUpdate = vanswerRepository.findAll().size();
        vanswer.setId(count.incrementAndGet());

        // Create the Vanswer
        VanswerDTO vanswerDTO = vanswerMapper.toDto(vanswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVanswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vanswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vanswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vanswer in Elasticsearch
        verify(mockVanswerSearchRepository, times(0)).save(vanswer);
    }

    @Test
    @Transactional
    void putWithIdMismatchVanswer() throws Exception {
        int databaseSizeBeforeUpdate = vanswerRepository.findAll().size();
        vanswer.setId(count.incrementAndGet());

        // Create the Vanswer
        VanswerDTO vanswerDTO = vanswerMapper.toDto(vanswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVanswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vanswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vanswer in Elasticsearch
        verify(mockVanswerSearchRepository, times(0)).save(vanswer);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVanswer() throws Exception {
        int databaseSizeBeforeUpdate = vanswerRepository.findAll().size();
        vanswer.setId(count.incrementAndGet());

        // Create the Vanswer
        VanswerDTO vanswerDTO = vanswerMapper.toDto(vanswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVanswerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vanswerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vanswer in Elasticsearch
        verify(mockVanswerSearchRepository, times(0)).save(vanswer);
    }

    @Test
    @Transactional
    void partialUpdateVanswerWithPatch() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        int databaseSizeBeforeUpdate = vanswerRepository.findAll().size();

        // Update the vanswer using partial update
        Vanswer partialUpdatedVanswer = new Vanswer();
        partialUpdatedVanswer.setId(vanswer.getId());

        partialUpdatedVanswer.urlVanswer(UPDATED_URL_VANSWER).accepted(UPDATED_ACCEPTED);

        restVanswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVanswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVanswer))
            )
            .andExpect(status().isOk());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeUpdate);
        Vanswer testVanswer = vanswerList.get(vanswerList.size() - 1);
        assertThat(testVanswer.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testVanswer.getUrlVanswer()).isEqualTo(UPDATED_URL_VANSWER);
        assertThat(testVanswer.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    void fullUpdateVanswerWithPatch() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        int databaseSizeBeforeUpdate = vanswerRepository.findAll().size();

        // Update the vanswer using partial update
        Vanswer partialUpdatedVanswer = new Vanswer();
        partialUpdatedVanswer.setId(vanswer.getId());

        partialUpdatedVanswer.creationDate(UPDATED_CREATION_DATE).urlVanswer(UPDATED_URL_VANSWER).accepted(UPDATED_ACCEPTED);

        restVanswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVanswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVanswer))
            )
            .andExpect(status().isOk());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeUpdate);
        Vanswer testVanswer = vanswerList.get(vanswerList.size() - 1);
        assertThat(testVanswer.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVanswer.getUrlVanswer()).isEqualTo(UPDATED_URL_VANSWER);
        assertThat(testVanswer.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    void patchNonExistingVanswer() throws Exception {
        int databaseSizeBeforeUpdate = vanswerRepository.findAll().size();
        vanswer.setId(count.incrementAndGet());

        // Create the Vanswer
        VanswerDTO vanswerDTO = vanswerMapper.toDto(vanswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVanswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vanswerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vanswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vanswer in Elasticsearch
        verify(mockVanswerSearchRepository, times(0)).save(vanswer);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVanswer() throws Exception {
        int databaseSizeBeforeUpdate = vanswerRepository.findAll().size();
        vanswer.setId(count.incrementAndGet());

        // Create the Vanswer
        VanswerDTO vanswerDTO = vanswerMapper.toDto(vanswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVanswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vanswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vanswer in Elasticsearch
        verify(mockVanswerSearchRepository, times(0)).save(vanswer);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVanswer() throws Exception {
        int databaseSizeBeforeUpdate = vanswerRepository.findAll().size();
        vanswer.setId(count.incrementAndGet());

        // Create the Vanswer
        VanswerDTO vanswerDTO = vanswerMapper.toDto(vanswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVanswerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vanswerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vanswer in the database
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Vanswer in Elasticsearch
        verify(mockVanswerSearchRepository, times(0)).save(vanswer);
    }

    @Test
    @Transactional
    void deleteVanswer() throws Exception {
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);

        int databaseSizeBeforeDelete = vanswerRepository.findAll().size();

        // Delete the vanswer
        restVanswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, vanswer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vanswer> vanswerList = vanswerRepository.findAll();
        assertThat(vanswerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Vanswer in Elasticsearch
        verify(mockVanswerSearchRepository, times(1)).deleteById(vanswer.getId());
    }

    @Test
    @Transactional
    void searchVanswer() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        vanswerRepository.saveAndFlush(vanswer);
        when(mockVanswerSearchRepository.search("id:" + vanswer.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(vanswer), PageRequest.of(0, 1), 1));

        // Search the vanswer
        restVanswerMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + vanswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vanswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].urlVanswer").value(hasItem(DEFAULT_URL_VANSWER)))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())));
    }
}
