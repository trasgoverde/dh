package com.blocknitive.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blocknitive.com.IntegrationTest;
import com.blocknitive.com.domain.Appuser;
import com.blocknitive.com.domain.Proposal;
import com.blocknitive.com.domain.ProposalVote;
import com.blocknitive.com.repository.ProposalVoteRepository;
import com.blocknitive.com.repository.search.ProposalVoteSearchRepository;
import com.blocknitive.com.service.criteria.ProposalVoteCriteria;
import com.blocknitive.com.service.dto.ProposalVoteDTO;
import com.blocknitive.com.service.mapper.ProposalVoteMapper;
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
 * Integration tests for the {@link ProposalVoteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProposalVoteResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_VOTE_POINTS = 1L;
    private static final Long UPDATED_VOTE_POINTS = 2L;
    private static final Long SMALLER_VOTE_POINTS = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/proposal-votes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/proposal-votes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProposalVoteRepository proposalVoteRepository;

    @Autowired
    private ProposalVoteMapper proposalVoteMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.ProposalVoteSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProposalVoteSearchRepository mockProposalVoteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProposalVoteMockMvc;

    private ProposalVote proposalVote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProposalVote createEntity(EntityManager em) {
        ProposalVote proposalVote = new ProposalVote().creationDate(DEFAULT_CREATION_DATE).votePoints(DEFAULT_VOTE_POINTS);
        return proposalVote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProposalVote createUpdatedEntity(EntityManager em) {
        ProposalVote proposalVote = new ProposalVote().creationDate(UPDATED_CREATION_DATE).votePoints(UPDATED_VOTE_POINTS);
        return proposalVote;
    }

    @BeforeEach
    public void initTest() {
        proposalVote = createEntity(em);
    }

    @Test
    @Transactional
    void createProposalVote() throws Exception {
        int databaseSizeBeforeCreate = proposalVoteRepository.findAll().size();
        // Create the ProposalVote
        ProposalVoteDTO proposalVoteDTO = proposalVoteMapper.toDto(proposalVote);
        restProposalVoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalVoteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeCreate + 1);
        ProposalVote testProposalVote = proposalVoteList.get(proposalVoteList.size() - 1);
        assertThat(testProposalVote.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProposalVote.getVotePoints()).isEqualTo(DEFAULT_VOTE_POINTS);

        // Validate the ProposalVote in Elasticsearch
        verify(mockProposalVoteSearchRepository, times(1)).save(testProposalVote);
    }

    @Test
    @Transactional
    void createProposalVoteWithExistingId() throws Exception {
        // Create the ProposalVote with an existing ID
        proposalVote.setId(1L);
        ProposalVoteDTO proposalVoteDTO = proposalVoteMapper.toDto(proposalVote);

        int databaseSizeBeforeCreate = proposalVoteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposalVoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProposalVote in Elasticsearch
        verify(mockProposalVoteSearchRepository, times(0)).save(proposalVote);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalVoteRepository.findAll().size();
        // set the field null
        proposalVote.setCreationDate(null);

        // Create the ProposalVote, which fails.
        ProposalVoteDTO proposalVoteDTO = proposalVoteMapper.toDto(proposalVote);

        restProposalVoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalVoteDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVotePointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalVoteRepository.findAll().size();
        // set the field null
        proposalVote.setVotePoints(null);

        // Create the ProposalVote, which fails.
        ProposalVoteDTO proposalVoteDTO = proposalVoteMapper.toDto(proposalVote);

        restProposalVoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalVoteDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProposalVotes() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList
        restProposalVoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposalVote.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].votePoints").value(hasItem(DEFAULT_VOTE_POINTS.intValue())));
    }

    @Test
    @Transactional
    void getProposalVote() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get the proposalVote
        restProposalVoteMockMvc
            .perform(get(ENTITY_API_URL_ID, proposalVote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proposalVote.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.votePoints").value(DEFAULT_VOTE_POINTS.intValue()));
    }

    @Test
    @Transactional
    void getProposalVotesByIdFiltering() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        Long id = proposalVote.getId();

        defaultProposalVoteShouldBeFound("id.equals=" + id);
        defaultProposalVoteShouldNotBeFound("id.notEquals=" + id);

        defaultProposalVoteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProposalVoteShouldNotBeFound("id.greaterThan=" + id);

        defaultProposalVoteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProposalVoteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProposalVotesByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where creationDate equals to DEFAULT_CREATION_DATE
        defaultProposalVoteShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the proposalVoteList where creationDate equals to UPDATED_CREATION_DATE
        defaultProposalVoteShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllProposalVotesByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultProposalVoteShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the proposalVoteList where creationDate not equals to UPDATED_CREATION_DATE
        defaultProposalVoteShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllProposalVotesByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultProposalVoteShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the proposalVoteList where creationDate equals to UPDATED_CREATION_DATE
        defaultProposalVoteShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllProposalVotesByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where creationDate is not null
        defaultProposalVoteShouldBeFound("creationDate.specified=true");

        // Get all the proposalVoteList where creationDate is null
        defaultProposalVoteShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalVotesByVotePointsIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where votePoints equals to DEFAULT_VOTE_POINTS
        defaultProposalVoteShouldBeFound("votePoints.equals=" + DEFAULT_VOTE_POINTS);

        // Get all the proposalVoteList where votePoints equals to UPDATED_VOTE_POINTS
        defaultProposalVoteShouldNotBeFound("votePoints.equals=" + UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    void getAllProposalVotesByVotePointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where votePoints not equals to DEFAULT_VOTE_POINTS
        defaultProposalVoteShouldNotBeFound("votePoints.notEquals=" + DEFAULT_VOTE_POINTS);

        // Get all the proposalVoteList where votePoints not equals to UPDATED_VOTE_POINTS
        defaultProposalVoteShouldBeFound("votePoints.notEquals=" + UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    void getAllProposalVotesByVotePointsIsInShouldWork() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where votePoints in DEFAULT_VOTE_POINTS or UPDATED_VOTE_POINTS
        defaultProposalVoteShouldBeFound("votePoints.in=" + DEFAULT_VOTE_POINTS + "," + UPDATED_VOTE_POINTS);

        // Get all the proposalVoteList where votePoints equals to UPDATED_VOTE_POINTS
        defaultProposalVoteShouldNotBeFound("votePoints.in=" + UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    void getAllProposalVotesByVotePointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where votePoints is not null
        defaultProposalVoteShouldBeFound("votePoints.specified=true");

        // Get all the proposalVoteList where votePoints is null
        defaultProposalVoteShouldNotBeFound("votePoints.specified=false");
    }

    @Test
    @Transactional
    void getAllProposalVotesByVotePointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where votePoints is greater than or equal to DEFAULT_VOTE_POINTS
        defaultProposalVoteShouldBeFound("votePoints.greaterThanOrEqual=" + DEFAULT_VOTE_POINTS);

        // Get all the proposalVoteList where votePoints is greater than or equal to UPDATED_VOTE_POINTS
        defaultProposalVoteShouldNotBeFound("votePoints.greaterThanOrEqual=" + UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    void getAllProposalVotesByVotePointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where votePoints is less than or equal to DEFAULT_VOTE_POINTS
        defaultProposalVoteShouldBeFound("votePoints.lessThanOrEqual=" + DEFAULT_VOTE_POINTS);

        // Get all the proposalVoteList where votePoints is less than or equal to SMALLER_VOTE_POINTS
        defaultProposalVoteShouldNotBeFound("votePoints.lessThanOrEqual=" + SMALLER_VOTE_POINTS);
    }

    @Test
    @Transactional
    void getAllProposalVotesByVotePointsIsLessThanSomething() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where votePoints is less than DEFAULT_VOTE_POINTS
        defaultProposalVoteShouldNotBeFound("votePoints.lessThan=" + DEFAULT_VOTE_POINTS);

        // Get all the proposalVoteList where votePoints is less than UPDATED_VOTE_POINTS
        defaultProposalVoteShouldBeFound("votePoints.lessThan=" + UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    void getAllProposalVotesByVotePointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        // Get all the proposalVoteList where votePoints is greater than DEFAULT_VOTE_POINTS
        defaultProposalVoteShouldNotBeFound("votePoints.greaterThan=" + DEFAULT_VOTE_POINTS);

        // Get all the proposalVoteList where votePoints is greater than SMALLER_VOTE_POINTS
        defaultProposalVoteShouldBeFound("votePoints.greaterThan=" + SMALLER_VOTE_POINTS);
    }

    @Test
    @Transactional
    void getAllProposalVotesByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);
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
        proposalVote.setAppuser(appuser);
        proposalVoteRepository.saveAndFlush(proposalVote);
        Long appuserId = appuser.getId();

        // Get all the proposalVoteList where appuser equals to appuserId
        defaultProposalVoteShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the proposalVoteList where appuser equals to (appuserId + 1)
        defaultProposalVoteShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    @Test
    @Transactional
    void getAllProposalVotesByProposalIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);
        Proposal proposal;
        if (TestUtil.findAll(em, Proposal.class).isEmpty()) {
            proposal = ProposalResourceIT.createEntity(em);
            em.persist(proposal);
            em.flush();
        } else {
            proposal = TestUtil.findAll(em, Proposal.class).get(0);
        }
        em.persist(proposal);
        em.flush();
        proposalVote.setProposal(proposal);
        proposalVoteRepository.saveAndFlush(proposalVote);
        Long proposalId = proposal.getId();

        // Get all the proposalVoteList where proposal equals to proposalId
        defaultProposalVoteShouldBeFound("proposalId.equals=" + proposalId);

        // Get all the proposalVoteList where proposal equals to (proposalId + 1)
        defaultProposalVoteShouldNotBeFound("proposalId.equals=" + (proposalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProposalVoteShouldBeFound(String filter) throws Exception {
        restProposalVoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposalVote.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].votePoints").value(hasItem(DEFAULT_VOTE_POINTS.intValue())));

        // Check, that the count call also returns 1
        restProposalVoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProposalVoteShouldNotBeFound(String filter) throws Exception {
        restProposalVoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProposalVoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProposalVote() throws Exception {
        // Get the proposalVote
        restProposalVoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProposalVote() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        int databaseSizeBeforeUpdate = proposalVoteRepository.findAll().size();

        // Update the proposalVote
        ProposalVote updatedProposalVote = proposalVoteRepository.findById(proposalVote.getId()).get();
        // Disconnect from session so that the updates on updatedProposalVote are not directly saved in db
        em.detach(updatedProposalVote);
        updatedProposalVote.creationDate(UPDATED_CREATION_DATE).votePoints(UPDATED_VOTE_POINTS);
        ProposalVoteDTO proposalVoteDTO = proposalVoteMapper.toDto(updatedProposalVote);

        restProposalVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proposalVoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposalVoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeUpdate);
        ProposalVote testProposalVote = proposalVoteList.get(proposalVoteList.size() - 1);
        assertThat(testProposalVote.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProposalVote.getVotePoints()).isEqualTo(UPDATED_VOTE_POINTS);

        // Validate the ProposalVote in Elasticsearch
        verify(mockProposalVoteSearchRepository).save(testProposalVote);
    }

    @Test
    @Transactional
    void putNonExistingProposalVote() throws Exception {
        int databaseSizeBeforeUpdate = proposalVoteRepository.findAll().size();
        proposalVote.setId(count.incrementAndGet());

        // Create the ProposalVote
        ProposalVoteDTO proposalVoteDTO = proposalVoteMapper.toDto(proposalVote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proposalVoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposalVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProposalVote in Elasticsearch
        verify(mockProposalVoteSearchRepository, times(0)).save(proposalVote);
    }

    @Test
    @Transactional
    void putWithIdMismatchProposalVote() throws Exception {
        int databaseSizeBeforeUpdate = proposalVoteRepository.findAll().size();
        proposalVote.setId(count.incrementAndGet());

        // Create the ProposalVote
        ProposalVoteDTO proposalVoteDTO = proposalVoteMapper.toDto(proposalVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposalVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProposalVote in Elasticsearch
        verify(mockProposalVoteSearchRepository, times(0)).save(proposalVote);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProposalVote() throws Exception {
        int databaseSizeBeforeUpdate = proposalVoteRepository.findAll().size();
        proposalVote.setId(count.incrementAndGet());

        // Create the ProposalVote
        ProposalVoteDTO proposalVoteDTO = proposalVoteMapper.toDto(proposalVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalVoteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalVoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProposalVote in Elasticsearch
        verify(mockProposalVoteSearchRepository, times(0)).save(proposalVote);
    }

    @Test
    @Transactional
    void partialUpdateProposalVoteWithPatch() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        int databaseSizeBeforeUpdate = proposalVoteRepository.findAll().size();

        // Update the proposalVote using partial update
        ProposalVote partialUpdatedProposalVote = new ProposalVote();
        partialUpdatedProposalVote.setId(proposalVote.getId());

        partialUpdatedProposalVote.votePoints(UPDATED_VOTE_POINTS);

        restProposalVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposalVote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProposalVote))
            )
            .andExpect(status().isOk());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeUpdate);
        ProposalVote testProposalVote = proposalVoteList.get(proposalVoteList.size() - 1);
        assertThat(testProposalVote.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProposalVote.getVotePoints()).isEqualTo(UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    void fullUpdateProposalVoteWithPatch() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        int databaseSizeBeforeUpdate = proposalVoteRepository.findAll().size();

        // Update the proposalVote using partial update
        ProposalVote partialUpdatedProposalVote = new ProposalVote();
        partialUpdatedProposalVote.setId(proposalVote.getId());

        partialUpdatedProposalVote.creationDate(UPDATED_CREATION_DATE).votePoints(UPDATED_VOTE_POINTS);

        restProposalVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposalVote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProposalVote))
            )
            .andExpect(status().isOk());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeUpdate);
        ProposalVote testProposalVote = proposalVoteList.get(proposalVoteList.size() - 1);
        assertThat(testProposalVote.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProposalVote.getVotePoints()).isEqualTo(UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingProposalVote() throws Exception {
        int databaseSizeBeforeUpdate = proposalVoteRepository.findAll().size();
        proposalVote.setId(count.incrementAndGet());

        // Create the ProposalVote
        ProposalVoteDTO proposalVoteDTO = proposalVoteMapper.toDto(proposalVote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proposalVoteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proposalVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProposalVote in Elasticsearch
        verify(mockProposalVoteSearchRepository, times(0)).save(proposalVote);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProposalVote() throws Exception {
        int databaseSizeBeforeUpdate = proposalVoteRepository.findAll().size();
        proposalVote.setId(count.incrementAndGet());

        // Create the ProposalVote
        ProposalVoteDTO proposalVoteDTO = proposalVoteMapper.toDto(proposalVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proposalVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProposalVote in Elasticsearch
        verify(mockProposalVoteSearchRepository, times(0)).save(proposalVote);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProposalVote() throws Exception {
        int databaseSizeBeforeUpdate = proposalVoteRepository.findAll().size();
        proposalVote.setId(count.incrementAndGet());

        // Create the ProposalVote
        ProposalVoteDTO proposalVoteDTO = proposalVoteMapper.toDto(proposalVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalVoteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proposalVoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProposalVote in the database
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProposalVote in Elasticsearch
        verify(mockProposalVoteSearchRepository, times(0)).save(proposalVote);
    }

    @Test
    @Transactional
    void deleteProposalVote() throws Exception {
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);

        int databaseSizeBeforeDelete = proposalVoteRepository.findAll().size();

        // Delete the proposalVote
        restProposalVoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, proposalVote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProposalVote> proposalVoteList = proposalVoteRepository.findAll();
        assertThat(proposalVoteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProposalVote in Elasticsearch
        verify(mockProposalVoteSearchRepository, times(1)).deleteById(proposalVote.getId());
    }

    @Test
    @Transactional
    void searchProposalVote() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        proposalVoteRepository.saveAndFlush(proposalVote);
        when(mockProposalVoteSearchRepository.search("id:" + proposalVote.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(proposalVote), PageRequest.of(0, 1), 1));

        // Search the proposalVote
        restProposalVoteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + proposalVote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposalVote.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].votePoints").value(hasItem(DEFAULT_VOTE_POINTS.intValue())));
    }
}
