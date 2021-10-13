package com.blocknitive.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blocknitive.com.IntegrationTest;
import com.blocknitive.com.domain.Appuser;
import com.blocknitive.com.domain.Post;
import com.blocknitive.com.domain.Proposal;
import com.blocknitive.com.domain.ProposalVote;
import com.blocknitive.com.domain.enumeration.ProposalRole;
import com.blocknitive.com.domain.enumeration.ProposalType;
import com.blocknitive.com.repository.ProposalRepository;
import com.blocknitive.com.repository.search.ProposalSearchRepository;
import com.blocknitive.com.service.criteria.ProposalCriteria;
import com.blocknitive.com.service.dto.ProposalDTO;
import com.blocknitive.com.service.mapper.ProposalMapper;
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
 * Integration tests for the {@link ProposalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProposalResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PROPOSAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROPOSAL_NAME = "BBBBBBBBBB";

    private static final ProposalType DEFAULT_PROPOSAL_TYPE = ProposalType.STUDY;
    private static final ProposalType UPDATED_PROPOSAL_TYPE = ProposalType.APPROVED;

    private static final ProposalRole DEFAULT_PROPOSAL_ROLE = ProposalRole.USER;
    private static final ProposalRole UPDATED_PROPOSAL_ROLE = ProposalRole.ORGANIZER;

    private static final Instant DEFAULT_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_OPEN = false;
    private static final Boolean UPDATED_IS_OPEN = true;

    private static final Boolean DEFAULT_IS_ACCEPTED = false;
    private static final Boolean UPDATED_IS_ACCEPTED = true;

    private static final Boolean DEFAULT_IS_PAID = false;
    private static final Boolean UPDATED_IS_PAID = true;

    private static final String ENTITY_API_URL = "/api/proposals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/proposals";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private ProposalMapper proposalMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.ProposalSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProposalSearchRepository mockProposalSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProposalMockMvc;

    private Proposal proposal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createEntity(EntityManager em) {
        Proposal proposal = new Proposal()
            .creationDate(DEFAULT_CREATION_DATE)
            .proposalName(DEFAULT_PROPOSAL_NAME)
            .proposalType(DEFAULT_PROPOSAL_TYPE)
            .proposalRole(DEFAULT_PROPOSAL_ROLE)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .isOpen(DEFAULT_IS_OPEN)
            .isAccepted(DEFAULT_IS_ACCEPTED)
            .isPaid(DEFAULT_IS_PAID);
        return proposal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createUpdatedEntity(EntityManager em) {
        Proposal proposal = new Proposal()
            .creationDate(UPDATED_CREATION_DATE)
            .proposalName(UPDATED_PROPOSAL_NAME)
            .proposalType(UPDATED_PROPOSAL_TYPE)
            .proposalRole(UPDATED_PROPOSAL_ROLE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .isOpen(UPDATED_IS_OPEN)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .isPaid(UPDATED_IS_PAID);
        return proposal;
    }

    @BeforeEach
    public void initTest() {
        proposal = createEntity(em);
    }

    @Test
    @Transactional
    void createProposal() throws Exception {
        int databaseSizeBeforeCreate = proposalRepository.findAll().size();
        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);
        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isCreated());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate + 1);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProposal.getProposalName()).isEqualTo(DEFAULT_PROPOSAL_NAME);
        assertThat(testProposal.getProposalType()).isEqualTo(DEFAULT_PROPOSAL_TYPE);
        assertThat(testProposal.getProposalRole()).isEqualTo(DEFAULT_PROPOSAL_ROLE);
        assertThat(testProposal.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testProposal.getIsOpen()).isEqualTo(DEFAULT_IS_OPEN);
        assertThat(testProposal.getIsAccepted()).isEqualTo(DEFAULT_IS_ACCEPTED);
        assertThat(testProposal.getIsPaid()).isEqualTo(DEFAULT_IS_PAID);

        // Validate the Proposal in Elasticsearch
        verify(mockProposalSearchRepository, times(1)).save(testProposal);
    }

    @Test
    @Transactional
    void createProposalWithExistingId() throws Exception {
        // Create the Proposal with an existing ID
        proposal.setId(1L);
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        int databaseSizeBeforeCreate = proposalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate);

        // Validate the Proposal in Elasticsearch
        verify(mockProposalSearchRepository, times(0)).save(proposal);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setCreationDate(null);

        // Create the Proposal, which fails.
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProposalNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setProposalName(null);

        // Create the Proposal, which fails.
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProposalTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setProposalType(null);

        // Create the Proposal, which fails.
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProposalRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setProposalRole(null);

        // Create the Proposal, which fails.
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProposals() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].proposalName").value(hasItem(DEFAULT_PROPOSAL_NAME)))
            .andExpect(jsonPath("$.[*].proposalType").value(hasItem(DEFAULT_PROPOSAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].proposalRole").value(hasItem(DEFAULT_PROPOSAL_ROLE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isOpen").value(hasItem(DEFAULT_IS_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID.booleanValue())));
    }

    @Test
    @Transactional
    void getProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get the proposal
        restProposalMockMvc
            .perform(get(ENTITY_API_URL_ID, proposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proposal.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.proposalName").value(DEFAULT_PROPOSAL_NAME))
            .andExpect(jsonPath("$.proposalType").value(DEFAULT_PROPOSAL_TYPE.toString()))
            .andExpect(jsonPath("$.proposalRole").value(DEFAULT_PROPOSAL_ROLE.toString()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.isOpen").value(DEFAULT_IS_OPEN.booleanValue()))
            .andExpect(jsonPath("$.isAccepted").value(DEFAULT_IS_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.isPaid").value(DEFAULT_IS_PAID.booleanValue()));
    }

    @Test
    @Transactional
    void getProposalsByIdFiltering() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        Long id = proposal.getId();

        defaultProposalShouldBeFound("id.equals=" + id);
        defaultProposalShouldNotBeFound("id.notEquals=" + id);

        defaultProposalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProposalShouldNotBeFound("id.greaterThan=" + id);

        defaultProposalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProposalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProposalsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where creationDate equals to DEFAULT_CREATION_DATE
        defaultProposalShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the proposalList where creationDate equals to UPDATED_CREATION_DATE
        defaultProposalShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllProposalsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultProposalShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the proposalList where creationDate not equals to UPDATED_CREATION_DATE
        defaultProposalShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllProposalsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultProposalShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the proposalList where creationDate equals to UPDATED_CREATION_DATE
        defaultProposalShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllProposalsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where creationDate is not null
        defaultProposalShouldBeFound("creationDate.specified=true");

        // Get all the proposalList where creationDate is null
        defaultProposalShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalsByProposalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalName equals to DEFAULT_PROPOSAL_NAME
        defaultProposalShouldBeFound("proposalName.equals=" + DEFAULT_PROPOSAL_NAME);

        // Get all the proposalList where proposalName equals to UPDATED_PROPOSAL_NAME
        defaultProposalShouldNotBeFound("proposalName.equals=" + UPDATED_PROPOSAL_NAME);
    }

    @Test
    @Transactional
    void getAllProposalsByProposalNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalName not equals to DEFAULT_PROPOSAL_NAME
        defaultProposalShouldNotBeFound("proposalName.notEquals=" + DEFAULT_PROPOSAL_NAME);

        // Get all the proposalList where proposalName not equals to UPDATED_PROPOSAL_NAME
        defaultProposalShouldBeFound("proposalName.notEquals=" + UPDATED_PROPOSAL_NAME);
    }

    @Test
    @Transactional
    void getAllProposalsByProposalNameIsInShouldWork() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalName in DEFAULT_PROPOSAL_NAME or UPDATED_PROPOSAL_NAME
        defaultProposalShouldBeFound("proposalName.in=" + DEFAULT_PROPOSAL_NAME + "," + UPDATED_PROPOSAL_NAME);

        // Get all the proposalList where proposalName equals to UPDATED_PROPOSAL_NAME
        defaultProposalShouldNotBeFound("proposalName.in=" + UPDATED_PROPOSAL_NAME);
    }

    @Test
    @Transactional
    void getAllProposalsByProposalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalName is not null
        defaultProposalShouldBeFound("proposalName.specified=true");

        // Get all the proposalList where proposalName is null
        defaultProposalShouldNotBeFound("proposalName.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalsByProposalNameContainsSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalName contains DEFAULT_PROPOSAL_NAME
        defaultProposalShouldBeFound("proposalName.contains=" + DEFAULT_PROPOSAL_NAME);

        // Get all the proposalList where proposalName contains UPDATED_PROPOSAL_NAME
        defaultProposalShouldNotBeFound("proposalName.contains=" + UPDATED_PROPOSAL_NAME);
    }

    @Test
    @Transactional
    void getAllProposalsByProposalNameNotContainsSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalName does not contain DEFAULT_PROPOSAL_NAME
        defaultProposalShouldNotBeFound("proposalName.doesNotContain=" + DEFAULT_PROPOSAL_NAME);

        // Get all the proposalList where proposalName does not contain UPDATED_PROPOSAL_NAME
        defaultProposalShouldBeFound("proposalName.doesNotContain=" + UPDATED_PROPOSAL_NAME);
    }

    @Test
    @Transactional
    void getAllProposalsByProposalTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalType equals to DEFAULT_PROPOSAL_TYPE
        defaultProposalShouldBeFound("proposalType.equals=" + DEFAULT_PROPOSAL_TYPE);

        // Get all the proposalList where proposalType equals to UPDATED_PROPOSAL_TYPE
        defaultProposalShouldNotBeFound("proposalType.equals=" + UPDATED_PROPOSAL_TYPE);
    }

    @Test
    @Transactional
    void getAllProposalsByProposalTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalType not equals to DEFAULT_PROPOSAL_TYPE
        defaultProposalShouldNotBeFound("proposalType.notEquals=" + DEFAULT_PROPOSAL_TYPE);

        // Get all the proposalList where proposalType not equals to UPDATED_PROPOSAL_TYPE
        defaultProposalShouldBeFound("proposalType.notEquals=" + UPDATED_PROPOSAL_TYPE);
    }

    @Test
    @Transactional
    void getAllProposalsByProposalTypeIsInShouldWork() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalType in DEFAULT_PROPOSAL_TYPE or UPDATED_PROPOSAL_TYPE
        defaultProposalShouldBeFound("proposalType.in=" + DEFAULT_PROPOSAL_TYPE + "," + UPDATED_PROPOSAL_TYPE);

        // Get all the proposalList where proposalType equals to UPDATED_PROPOSAL_TYPE
        defaultProposalShouldNotBeFound("proposalType.in=" + UPDATED_PROPOSAL_TYPE);
    }

    @Test
    @Transactional
    void getAllProposalsByProposalTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalType is not null
        defaultProposalShouldBeFound("proposalType.specified=true");

        // Get all the proposalList where proposalType is null
        defaultProposalShouldNotBeFound("proposalType.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalsByProposalRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalRole equals to DEFAULT_PROPOSAL_ROLE
        defaultProposalShouldBeFound("proposalRole.equals=" + DEFAULT_PROPOSAL_ROLE);

        // Get all the proposalList where proposalRole equals to UPDATED_PROPOSAL_ROLE
        defaultProposalShouldNotBeFound("proposalRole.equals=" + UPDATED_PROPOSAL_ROLE);
    }

    @Test
    @Transactional
    void getAllProposalsByProposalRoleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalRole not equals to DEFAULT_PROPOSAL_ROLE
        defaultProposalShouldNotBeFound("proposalRole.notEquals=" + DEFAULT_PROPOSAL_ROLE);

        // Get all the proposalList where proposalRole not equals to UPDATED_PROPOSAL_ROLE
        defaultProposalShouldBeFound("proposalRole.notEquals=" + UPDATED_PROPOSAL_ROLE);
    }

    @Test
    @Transactional
    void getAllProposalsByProposalRoleIsInShouldWork() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalRole in DEFAULT_PROPOSAL_ROLE or UPDATED_PROPOSAL_ROLE
        defaultProposalShouldBeFound("proposalRole.in=" + DEFAULT_PROPOSAL_ROLE + "," + UPDATED_PROPOSAL_ROLE);

        // Get all the proposalList where proposalRole equals to UPDATED_PROPOSAL_ROLE
        defaultProposalShouldNotBeFound("proposalRole.in=" + UPDATED_PROPOSAL_ROLE);
    }

    @Test
    @Transactional
    void getAllProposalsByProposalRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where proposalRole is not null
        defaultProposalShouldBeFound("proposalRole.specified=true");

        // Get all the proposalList where proposalRole is null
        defaultProposalShouldNotBeFound("proposalRole.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalsByReleaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where releaseDate equals to DEFAULT_RELEASE_DATE
        defaultProposalShouldBeFound("releaseDate.equals=" + DEFAULT_RELEASE_DATE);

        // Get all the proposalList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultProposalShouldNotBeFound("releaseDate.equals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllProposalsByReleaseDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where releaseDate not equals to DEFAULT_RELEASE_DATE
        defaultProposalShouldNotBeFound("releaseDate.notEquals=" + DEFAULT_RELEASE_DATE);

        // Get all the proposalList where releaseDate not equals to UPDATED_RELEASE_DATE
        defaultProposalShouldBeFound("releaseDate.notEquals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllProposalsByReleaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where releaseDate in DEFAULT_RELEASE_DATE or UPDATED_RELEASE_DATE
        defaultProposalShouldBeFound("releaseDate.in=" + DEFAULT_RELEASE_DATE + "," + UPDATED_RELEASE_DATE);

        // Get all the proposalList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultProposalShouldNotBeFound("releaseDate.in=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllProposalsByReleaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where releaseDate is not null
        defaultProposalShouldBeFound("releaseDate.specified=true");

        // Get all the proposalList where releaseDate is null
        defaultProposalShouldNotBeFound("releaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalsByIsOpenIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isOpen equals to DEFAULT_IS_OPEN
        defaultProposalShouldBeFound("isOpen.equals=" + DEFAULT_IS_OPEN);

        // Get all the proposalList where isOpen equals to UPDATED_IS_OPEN
        defaultProposalShouldNotBeFound("isOpen.equals=" + UPDATED_IS_OPEN);
    }

    @Test
    @Transactional
    void getAllProposalsByIsOpenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isOpen not equals to DEFAULT_IS_OPEN
        defaultProposalShouldNotBeFound("isOpen.notEquals=" + DEFAULT_IS_OPEN);

        // Get all the proposalList where isOpen not equals to UPDATED_IS_OPEN
        defaultProposalShouldBeFound("isOpen.notEquals=" + UPDATED_IS_OPEN);
    }

    @Test
    @Transactional
    void getAllProposalsByIsOpenIsInShouldWork() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isOpen in DEFAULT_IS_OPEN or UPDATED_IS_OPEN
        defaultProposalShouldBeFound("isOpen.in=" + DEFAULT_IS_OPEN + "," + UPDATED_IS_OPEN);

        // Get all the proposalList where isOpen equals to UPDATED_IS_OPEN
        defaultProposalShouldNotBeFound("isOpen.in=" + UPDATED_IS_OPEN);
    }

    @Test
    @Transactional
    void getAllProposalsByIsOpenIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isOpen is not null
        defaultProposalShouldBeFound("isOpen.specified=true");

        // Get all the proposalList where isOpen is null
        defaultProposalShouldNotBeFound("isOpen.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalsByIsAcceptedIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isAccepted equals to DEFAULT_IS_ACCEPTED
        defaultProposalShouldBeFound("isAccepted.equals=" + DEFAULT_IS_ACCEPTED);

        // Get all the proposalList where isAccepted equals to UPDATED_IS_ACCEPTED
        defaultProposalShouldNotBeFound("isAccepted.equals=" + UPDATED_IS_ACCEPTED);
    }

    @Test
    @Transactional
    void getAllProposalsByIsAcceptedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isAccepted not equals to DEFAULT_IS_ACCEPTED
        defaultProposalShouldNotBeFound("isAccepted.notEquals=" + DEFAULT_IS_ACCEPTED);

        // Get all the proposalList where isAccepted not equals to UPDATED_IS_ACCEPTED
        defaultProposalShouldBeFound("isAccepted.notEquals=" + UPDATED_IS_ACCEPTED);
    }

    @Test
    @Transactional
    void getAllProposalsByIsAcceptedIsInShouldWork() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isAccepted in DEFAULT_IS_ACCEPTED or UPDATED_IS_ACCEPTED
        defaultProposalShouldBeFound("isAccepted.in=" + DEFAULT_IS_ACCEPTED + "," + UPDATED_IS_ACCEPTED);

        // Get all the proposalList where isAccepted equals to UPDATED_IS_ACCEPTED
        defaultProposalShouldNotBeFound("isAccepted.in=" + UPDATED_IS_ACCEPTED);
    }

    @Test
    @Transactional
    void getAllProposalsByIsAcceptedIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isAccepted is not null
        defaultProposalShouldBeFound("isAccepted.specified=true");

        // Get all the proposalList where isAccepted is null
        defaultProposalShouldNotBeFound("isAccepted.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalsByIsPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isPaid equals to DEFAULT_IS_PAID
        defaultProposalShouldBeFound("isPaid.equals=" + DEFAULT_IS_PAID);

        // Get all the proposalList where isPaid equals to UPDATED_IS_PAID
        defaultProposalShouldNotBeFound("isPaid.equals=" + UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    void getAllProposalsByIsPaidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isPaid not equals to DEFAULT_IS_PAID
        defaultProposalShouldNotBeFound("isPaid.notEquals=" + DEFAULT_IS_PAID);

        // Get all the proposalList where isPaid not equals to UPDATED_IS_PAID
        defaultProposalShouldBeFound("isPaid.notEquals=" + UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    void getAllProposalsByIsPaidIsInShouldWork() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isPaid in DEFAULT_IS_PAID or UPDATED_IS_PAID
        defaultProposalShouldBeFound("isPaid.in=" + DEFAULT_IS_PAID + "," + UPDATED_IS_PAID);

        // Get all the proposalList where isPaid equals to UPDATED_IS_PAID
        defaultProposalShouldNotBeFound("isPaid.in=" + UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    void getAllProposalsByIsPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList where isPaid is not null
        defaultProposalShouldBeFound("isPaid.specified=true");

        // Get all the proposalList where isPaid is null
        defaultProposalShouldNotBeFound("isPaid.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalsByProposalVoteIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);
        ProposalVote proposalVote;
        if (TestUtil.findAll(em, ProposalVote.class).isEmpty()) {
            proposalVote = ProposalVoteResourceIT.createEntity(em);
            em.persist(proposalVote);
            em.flush();
        } else {
            proposalVote = TestUtil.findAll(em, ProposalVote.class).get(0);
        }
        em.persist(proposalVote);
        em.flush();
        proposal.addProposalVote(proposalVote);
        proposalRepository.saveAndFlush(proposal);
        Long proposalVoteId = proposalVote.getId();

        // Get all the proposalList where proposalVote equals to proposalVoteId
        defaultProposalShouldBeFound("proposalVoteId.equals=" + proposalVoteId);

        // Get all the proposalList where proposalVote equals to (proposalVoteId + 1)
        defaultProposalShouldNotBeFound("proposalVoteId.equals=" + (proposalVoteId + 1));
    }

    @Test
    @Transactional
    void getAllProposalsByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);
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
        proposal.setAppuser(appuser);
        proposalRepository.saveAndFlush(proposal);
        Long appuserId = appuser.getId();

        // Get all the proposalList where appuser equals to appuserId
        defaultProposalShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the proposalList where appuser equals to (appuserId + 1)
        defaultProposalShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    @Test
    @Transactional
    void getAllProposalsByPostIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);
        Post post;
        if (TestUtil.findAll(em, Post.class).isEmpty()) {
            post = PostResourceIT.createEntity(em);
            em.persist(post);
            em.flush();
        } else {
            post = TestUtil.findAll(em, Post.class).get(0);
        }
        em.persist(post);
        em.flush();
        proposal.setPost(post);
        proposalRepository.saveAndFlush(proposal);
        Long postId = post.getId();

        // Get all the proposalList where post equals to postId
        defaultProposalShouldBeFound("postId.equals=" + postId);

        // Get all the proposalList where post equals to (postId + 1)
        defaultProposalShouldNotBeFound("postId.equals=" + (postId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProposalShouldBeFound(String filter) throws Exception {
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].proposalName").value(hasItem(DEFAULT_PROPOSAL_NAME)))
            .andExpect(jsonPath("$.[*].proposalType").value(hasItem(DEFAULT_PROPOSAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].proposalRole").value(hasItem(DEFAULT_PROPOSAL_ROLE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isOpen").value(hasItem(DEFAULT_IS_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID.booleanValue())));

        // Check, that the count call also returns 1
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProposalShouldNotBeFound(String filter) throws Exception {
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProposal() throws Exception {
        // Get the proposal
        restProposalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal
        Proposal updatedProposal = proposalRepository.findById(proposal.getId()).get();
        // Disconnect from session so that the updates on updatedProposal are not directly saved in db
        em.detach(updatedProposal);
        updatedProposal
            .creationDate(UPDATED_CREATION_DATE)
            .proposalName(UPDATED_PROPOSAL_NAME)
            .proposalType(UPDATED_PROPOSAL_TYPE)
            .proposalRole(UPDATED_PROPOSAL_ROLE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .isOpen(UPDATED_IS_OPEN)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .isPaid(UPDATED_IS_PAID);
        ProposalDTO proposalDTO = proposalMapper.toDto(updatedProposal);

        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proposalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProposal.getProposalName()).isEqualTo(UPDATED_PROPOSAL_NAME);
        assertThat(testProposal.getProposalType()).isEqualTo(UPDATED_PROPOSAL_TYPE);
        assertThat(testProposal.getProposalRole()).isEqualTo(UPDATED_PROPOSAL_ROLE);
        assertThat(testProposal.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testProposal.getIsOpen()).isEqualTo(UPDATED_IS_OPEN);
        assertThat(testProposal.getIsAccepted()).isEqualTo(UPDATED_IS_ACCEPTED);
        assertThat(testProposal.getIsPaid()).isEqualTo(UPDATED_IS_PAID);

        // Validate the Proposal in Elasticsearch
        verify(mockProposalSearchRepository).save(testProposal);
    }

    @Test
    @Transactional
    void putNonExistingProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proposalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Proposal in Elasticsearch
        verify(mockProposalSearchRepository, times(0)).save(proposal);
    }

    @Test
    @Transactional
    void putWithIdMismatchProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Proposal in Elasticsearch
        verify(mockProposalSearchRepository, times(0)).save(proposal);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Proposal in Elasticsearch
        verify(mockProposalSearchRepository, times(0)).save(proposal);
    }

    @Test
    @Transactional
    void partialUpdateProposalWithPatch() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal using partial update
        Proposal partialUpdatedProposal = new Proposal();
        partialUpdatedProposal.setId(proposal.getId());

        partialUpdatedProposal
            .proposalType(UPDATED_PROPOSAL_TYPE)
            .proposalRole(UPDATED_PROPOSAL_ROLE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .isPaid(UPDATED_IS_PAID);

        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProposal))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProposal.getProposalName()).isEqualTo(DEFAULT_PROPOSAL_NAME);
        assertThat(testProposal.getProposalType()).isEqualTo(UPDATED_PROPOSAL_TYPE);
        assertThat(testProposal.getProposalRole()).isEqualTo(UPDATED_PROPOSAL_ROLE);
        assertThat(testProposal.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testProposal.getIsOpen()).isEqualTo(DEFAULT_IS_OPEN);
        assertThat(testProposal.getIsAccepted()).isEqualTo(UPDATED_IS_ACCEPTED);
        assertThat(testProposal.getIsPaid()).isEqualTo(UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    void fullUpdateProposalWithPatch() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal using partial update
        Proposal partialUpdatedProposal = new Proposal();
        partialUpdatedProposal.setId(proposal.getId());

        partialUpdatedProposal
            .creationDate(UPDATED_CREATION_DATE)
            .proposalName(UPDATED_PROPOSAL_NAME)
            .proposalType(UPDATED_PROPOSAL_TYPE)
            .proposalRole(UPDATED_PROPOSAL_ROLE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .isOpen(UPDATED_IS_OPEN)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .isPaid(UPDATED_IS_PAID);

        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProposal))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProposal.getProposalName()).isEqualTo(UPDATED_PROPOSAL_NAME);
        assertThat(testProposal.getProposalType()).isEqualTo(UPDATED_PROPOSAL_TYPE);
        assertThat(testProposal.getProposalRole()).isEqualTo(UPDATED_PROPOSAL_ROLE);
        assertThat(testProposal.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testProposal.getIsOpen()).isEqualTo(UPDATED_IS_OPEN);
        assertThat(testProposal.getIsAccepted()).isEqualTo(UPDATED_IS_ACCEPTED);
        assertThat(testProposal.getIsPaid()).isEqualTo(UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    void patchNonExistingProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proposalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Proposal in Elasticsearch
        verify(mockProposalSearchRepository, times(0)).save(proposal);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Proposal in Elasticsearch
        verify(mockProposalSearchRepository, times(0)).save(proposal);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Proposal in Elasticsearch
        verify(mockProposalSearchRepository, times(0)).save(proposal);
    }

    @Test
    @Transactional
    void deleteProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeDelete = proposalRepository.findAll().size();

        // Delete the proposal
        restProposalMockMvc
            .perform(delete(ENTITY_API_URL_ID, proposal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Proposal in Elasticsearch
        verify(mockProposalSearchRepository, times(1)).deleteById(proposal.getId());
    }

    @Test
    @Transactional
    void searchProposal() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);
        when(mockProposalSearchRepository.search("id:" + proposal.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(proposal), PageRequest.of(0, 1), 1));

        // Search the proposal
        restProposalMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + proposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].proposalName").value(hasItem(DEFAULT_PROPOSAL_NAME)))
            .andExpect(jsonPath("$.[*].proposalType").value(hasItem(DEFAULT_PROPOSAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].proposalRole").value(hasItem(DEFAULT_PROPOSAL_ROLE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isOpen").value(hasItem(DEFAULT_IS_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID.booleanValue())));
    }
}
