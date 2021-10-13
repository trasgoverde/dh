package com.blocknitive.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blocknitive.com.IntegrationTest;
import com.blocknitive.com.domain.Appuser;
import com.blocknitive.com.domain.Blockuser;
import com.blocknitive.com.domain.Blog;
import com.blocknitive.com.domain.Cactivity;
import com.blocknitive.com.domain.Calbum;
import com.blocknitive.com.domain.Cceleb;
import com.blocknitive.com.domain.Cinterest;
import com.blocknitive.com.domain.Cmessage;
import com.blocknitive.com.domain.Community;
import com.blocknitive.com.domain.Follow;
import com.blocknitive.com.repository.CommunityRepository;
import com.blocknitive.com.repository.search.CommunitySearchRepository;
import com.blocknitive.com.service.criteria.CommunityCriteria;
import com.blocknitive.com.service.dto.CommunityDTO;
import com.blocknitive.com.service.mapper.CommunityMapper;
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
 * Integration tests for the {@link CommunityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommunityResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMUNITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMMUNITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMMUNITY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_COMMUNITY_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/communities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/communities";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityMapper communityMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.CommunitySearchRepositoryMockConfiguration
     */
    @Autowired
    private CommunitySearchRepository mockCommunitySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunityMockMvc;

    private Community community;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Community createEntity(EntityManager em) {
        Community community = new Community()
            .creationDate(DEFAULT_CREATION_DATE)
            .communityName(DEFAULT_COMMUNITY_NAME)
            .communityDescription(DEFAULT_COMMUNITY_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        community.setAppuser(appuser);
        // Add required entity
        Calbum calbum;
        if (TestUtil.findAll(em, Calbum.class).isEmpty()) {
            calbum = CalbumResourceIT.createEntity(em);
            em.persist(calbum);
            em.flush();
        } else {
            calbum = TestUtil.findAll(em, Calbum.class).get(0);
        }
        community.getCalbums().add(calbum);
        return community;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Community createUpdatedEntity(EntityManager em) {
        Community community = new Community()
            .creationDate(UPDATED_CREATION_DATE)
            .communityName(UPDATED_COMMUNITY_NAME)
            .communityDescription(UPDATED_COMMUNITY_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        community.setAppuser(appuser);
        // Add required entity
        Calbum calbum;
        if (TestUtil.findAll(em, Calbum.class).isEmpty()) {
            calbum = CalbumResourceIT.createUpdatedEntity(em);
            em.persist(calbum);
            em.flush();
        } else {
            calbum = TestUtil.findAll(em, Calbum.class).get(0);
        }
        community.getCalbums().add(calbum);
        return community;
    }

    @BeforeEach
    public void initTest() {
        community = createEntity(em);
    }

    @Test
    @Transactional
    void createCommunity() throws Exception {
        int databaseSizeBeforeCreate = communityRepository.findAll().size();
        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);
        restCommunityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityDTO)))
            .andExpect(status().isCreated());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeCreate + 1);
        Community testCommunity = communityList.get(communityList.size() - 1);
        assertThat(testCommunity.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCommunity.getCommunityName()).isEqualTo(DEFAULT_COMMUNITY_NAME);
        assertThat(testCommunity.getCommunityDescription()).isEqualTo(DEFAULT_COMMUNITY_DESCRIPTION);
        assertThat(testCommunity.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCommunity.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testCommunity.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the Community in Elasticsearch
        verify(mockCommunitySearchRepository, times(1)).save(testCommunity);
    }

    @Test
    @Transactional
    void createCommunityWithExistingId() throws Exception {
        // Create the Community with an existing ID
        community.setId(1L);
        CommunityDTO communityDTO = communityMapper.toDto(community);

        int databaseSizeBeforeCreate = communityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeCreate);

        // Validate the Community in Elasticsearch
        verify(mockCommunitySearchRepository, times(0)).save(community);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = communityRepository.findAll().size();
        // set the field null
        community.setCreationDate(null);

        // Create the Community, which fails.
        CommunityDTO communityDTO = communityMapper.toDto(community);

        restCommunityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityDTO)))
            .andExpect(status().isBadRequest());

        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommunityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = communityRepository.findAll().size();
        // set the field null
        community.setCommunityName(null);

        // Create the Community, which fails.
        CommunityDTO communityDTO = communityMapper.toDto(community);

        restCommunityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityDTO)))
            .andExpect(status().isBadRequest());

        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommunityDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = communityRepository.findAll().size();
        // set the field null
        community.setCommunityDescription(null);

        // Create the Community, which fails.
        CommunityDTO communityDTO = communityMapper.toDto(community);

        restCommunityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityDTO)))
            .andExpect(status().isBadRequest());

        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommunities() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(community.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].communityName").value(hasItem(DEFAULT_COMMUNITY_NAME)))
            .andExpect(jsonPath("$.[*].communityDescription").value(hasItem(DEFAULT_COMMUNITY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getCommunity() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get the community
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL_ID, community.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(community.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.communityName").value(DEFAULT_COMMUNITY_NAME))
            .andExpect(jsonPath("$.communityDescription").value(DEFAULT_COMMUNITY_DESCRIPTION))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getCommunitiesByIdFiltering() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        Long id = community.getId();

        defaultCommunityShouldBeFound("id.equals=" + id);
        defaultCommunityShouldNotBeFound("id.notEquals=" + id);

        defaultCommunityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommunityShouldNotBeFound("id.greaterThan=" + id);

        defaultCommunityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommunityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where creationDate equals to DEFAULT_CREATION_DATE
        defaultCommunityShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the communityList where creationDate equals to UPDATED_CREATION_DATE
        defaultCommunityShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultCommunityShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the communityList where creationDate not equals to UPDATED_CREATION_DATE
        defaultCommunityShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultCommunityShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the communityList where creationDate equals to UPDATED_CREATION_DATE
        defaultCommunityShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where creationDate is not null
        defaultCommunityShouldBeFound("creationDate.specified=true");

        // Get all the communityList where creationDate is null
        defaultCommunityShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityName equals to DEFAULT_COMMUNITY_NAME
        defaultCommunityShouldBeFound("communityName.equals=" + DEFAULT_COMMUNITY_NAME);

        // Get all the communityList where communityName equals to UPDATED_COMMUNITY_NAME
        defaultCommunityShouldNotBeFound("communityName.equals=" + UPDATED_COMMUNITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityName not equals to DEFAULT_COMMUNITY_NAME
        defaultCommunityShouldNotBeFound("communityName.notEquals=" + DEFAULT_COMMUNITY_NAME);

        // Get all the communityList where communityName not equals to UPDATED_COMMUNITY_NAME
        defaultCommunityShouldBeFound("communityName.notEquals=" + UPDATED_COMMUNITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityNameIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityName in DEFAULT_COMMUNITY_NAME or UPDATED_COMMUNITY_NAME
        defaultCommunityShouldBeFound("communityName.in=" + DEFAULT_COMMUNITY_NAME + "," + UPDATED_COMMUNITY_NAME);

        // Get all the communityList where communityName equals to UPDATED_COMMUNITY_NAME
        defaultCommunityShouldNotBeFound("communityName.in=" + UPDATED_COMMUNITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityName is not null
        defaultCommunityShouldBeFound("communityName.specified=true");

        // Get all the communityList where communityName is null
        defaultCommunityShouldNotBeFound("communityName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityNameContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityName contains DEFAULT_COMMUNITY_NAME
        defaultCommunityShouldBeFound("communityName.contains=" + DEFAULT_COMMUNITY_NAME);

        // Get all the communityList where communityName contains UPDATED_COMMUNITY_NAME
        defaultCommunityShouldNotBeFound("communityName.contains=" + UPDATED_COMMUNITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityNameNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityName does not contain DEFAULT_COMMUNITY_NAME
        defaultCommunityShouldNotBeFound("communityName.doesNotContain=" + DEFAULT_COMMUNITY_NAME);

        // Get all the communityList where communityName does not contain UPDATED_COMMUNITY_NAME
        defaultCommunityShouldBeFound("communityName.doesNotContain=" + UPDATED_COMMUNITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityDescription equals to DEFAULT_COMMUNITY_DESCRIPTION
        defaultCommunityShouldBeFound("communityDescription.equals=" + DEFAULT_COMMUNITY_DESCRIPTION);

        // Get all the communityList where communityDescription equals to UPDATED_COMMUNITY_DESCRIPTION
        defaultCommunityShouldNotBeFound("communityDescription.equals=" + UPDATED_COMMUNITY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityDescription not equals to DEFAULT_COMMUNITY_DESCRIPTION
        defaultCommunityShouldNotBeFound("communityDescription.notEquals=" + DEFAULT_COMMUNITY_DESCRIPTION);

        // Get all the communityList where communityDescription not equals to UPDATED_COMMUNITY_DESCRIPTION
        defaultCommunityShouldBeFound("communityDescription.notEquals=" + UPDATED_COMMUNITY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityDescription in DEFAULT_COMMUNITY_DESCRIPTION or UPDATED_COMMUNITY_DESCRIPTION
        defaultCommunityShouldBeFound("communityDescription.in=" + DEFAULT_COMMUNITY_DESCRIPTION + "," + UPDATED_COMMUNITY_DESCRIPTION);

        // Get all the communityList where communityDescription equals to UPDATED_COMMUNITY_DESCRIPTION
        defaultCommunityShouldNotBeFound("communityDescription.in=" + UPDATED_COMMUNITY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityDescription is not null
        defaultCommunityShouldBeFound("communityDescription.specified=true");

        // Get all the communityList where communityDescription is null
        defaultCommunityShouldNotBeFound("communityDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityDescriptionContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityDescription contains DEFAULT_COMMUNITY_DESCRIPTION
        defaultCommunityShouldBeFound("communityDescription.contains=" + DEFAULT_COMMUNITY_DESCRIPTION);

        // Get all the communityList where communityDescription contains UPDATED_COMMUNITY_DESCRIPTION
        defaultCommunityShouldNotBeFound("communityDescription.contains=" + UPDATED_COMMUNITY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where communityDescription does not contain DEFAULT_COMMUNITY_DESCRIPTION
        defaultCommunityShouldNotBeFound("communityDescription.doesNotContain=" + DEFAULT_COMMUNITY_DESCRIPTION);

        // Get all the communityList where communityDescription does not contain UPDATED_COMMUNITY_DESCRIPTION
        defaultCommunityShouldBeFound("communityDescription.doesNotContain=" + UPDATED_COMMUNITY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommunitiesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where isActive equals to DEFAULT_IS_ACTIVE
        defaultCommunityShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the communityList where isActive equals to UPDATED_IS_ACTIVE
        defaultCommunityShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultCommunityShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the communityList where isActive not equals to UPDATED_IS_ACTIVE
        defaultCommunityShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultCommunityShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the communityList where isActive equals to UPDATED_IS_ACTIVE
        defaultCommunityShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where isActive is not null
        defaultCommunityShouldBeFound("isActive.specified=true");

        // Get all the communityList where isActive is null
        defaultCommunityShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByBlogIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        Blog blog;
        if (TestUtil.findAll(em, Blog.class).isEmpty()) {
            blog = BlogResourceIT.createEntity(em);
            em.persist(blog);
            em.flush();
        } else {
            blog = TestUtil.findAll(em, Blog.class).get(0);
        }
        em.persist(blog);
        em.flush();
        community.addBlog(blog);
        communityRepository.saveAndFlush(community);
        Long blogId = blog.getId();

        // Get all the communityList where blog equals to blogId
        defaultCommunityShouldBeFound("blogId.equals=" + blogId);

        // Get all the communityList where blog equals to (blogId + 1)
        defaultCommunityShouldNotBeFound("blogId.equals=" + (blogId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByCsenderIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        Cmessage csender;
        if (TestUtil.findAll(em, Cmessage.class).isEmpty()) {
            csender = CmessageResourceIT.createEntity(em);
            em.persist(csender);
            em.flush();
        } else {
            csender = TestUtil.findAll(em, Cmessage.class).get(0);
        }
        em.persist(csender);
        em.flush();
        community.addCsender(csender);
        communityRepository.saveAndFlush(community);
        Long csenderId = csender.getId();

        // Get all the communityList where csender equals to csenderId
        defaultCommunityShouldBeFound("csenderId.equals=" + csenderId);

        // Get all the communityList where csender equals to (csenderId + 1)
        defaultCommunityShouldNotBeFound("csenderId.equals=" + (csenderId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByCreceiverIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        Cmessage creceiver;
        if (TestUtil.findAll(em, Cmessage.class).isEmpty()) {
            creceiver = CmessageResourceIT.createEntity(em);
            em.persist(creceiver);
            em.flush();
        } else {
            creceiver = TestUtil.findAll(em, Cmessage.class).get(0);
        }
        em.persist(creceiver);
        em.flush();
        community.addCreceiver(creceiver);
        communityRepository.saveAndFlush(community);
        Long creceiverId = creceiver.getId();

        // Get all the communityList where creceiver equals to creceiverId
        defaultCommunityShouldBeFound("creceiverId.equals=" + creceiverId);

        // Get all the communityList where creceiver equals to (creceiverId + 1)
        defaultCommunityShouldNotBeFound("creceiverId.equals=" + (creceiverId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByCfollowedIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        Follow cfollowed;
        if (TestUtil.findAll(em, Follow.class).isEmpty()) {
            cfollowed = FollowResourceIT.createEntity(em);
            em.persist(cfollowed);
            em.flush();
        } else {
            cfollowed = TestUtil.findAll(em, Follow.class).get(0);
        }
        em.persist(cfollowed);
        em.flush();
        community.addCfollowed(cfollowed);
        communityRepository.saveAndFlush(community);
        Long cfollowedId = cfollowed.getId();

        // Get all the communityList where cfollowed equals to cfollowedId
        defaultCommunityShouldBeFound("cfollowedId.equals=" + cfollowedId);

        // Get all the communityList where cfollowed equals to (cfollowedId + 1)
        defaultCommunityShouldNotBeFound("cfollowedId.equals=" + (cfollowedId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByCfollowingIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        Follow cfollowing;
        if (TestUtil.findAll(em, Follow.class).isEmpty()) {
            cfollowing = FollowResourceIT.createEntity(em);
            em.persist(cfollowing);
            em.flush();
        } else {
            cfollowing = TestUtil.findAll(em, Follow.class).get(0);
        }
        em.persist(cfollowing);
        em.flush();
        community.addCfollowing(cfollowing);
        communityRepository.saveAndFlush(community);
        Long cfollowingId = cfollowing.getId();

        // Get all the communityList where cfollowing equals to cfollowingId
        defaultCommunityShouldBeFound("cfollowingId.equals=" + cfollowingId);

        // Get all the communityList where cfollowing equals to (cfollowingId + 1)
        defaultCommunityShouldNotBeFound("cfollowingId.equals=" + (cfollowingId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByCblockeduserIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        Blockuser cblockeduser;
        if (TestUtil.findAll(em, Blockuser.class).isEmpty()) {
            cblockeduser = BlockuserResourceIT.createEntity(em);
            em.persist(cblockeduser);
            em.flush();
        } else {
            cblockeduser = TestUtil.findAll(em, Blockuser.class).get(0);
        }
        em.persist(cblockeduser);
        em.flush();
        community.addCblockeduser(cblockeduser);
        communityRepository.saveAndFlush(community);
        Long cblockeduserId = cblockeduser.getId();

        // Get all the communityList where cblockeduser equals to cblockeduserId
        defaultCommunityShouldBeFound("cblockeduserId.equals=" + cblockeduserId);

        // Get all the communityList where cblockeduser equals to (cblockeduserId + 1)
        defaultCommunityShouldNotBeFound("cblockeduserId.equals=" + (cblockeduserId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByCblockinguserIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        Blockuser cblockinguser;
        if (TestUtil.findAll(em, Blockuser.class).isEmpty()) {
            cblockinguser = BlockuserResourceIT.createEntity(em);
            em.persist(cblockinguser);
            em.flush();
        } else {
            cblockinguser = TestUtil.findAll(em, Blockuser.class).get(0);
        }
        em.persist(cblockinguser);
        em.flush();
        community.addCblockinguser(cblockinguser);
        communityRepository.saveAndFlush(community);
        Long cblockinguserId = cblockinguser.getId();

        // Get all the communityList where cblockinguser equals to cblockinguserId
        defaultCommunityShouldBeFound("cblockinguserId.equals=" + cblockinguserId);

        // Get all the communityList where cblockinguser equals to (cblockinguserId + 1)
        defaultCommunityShouldNotBeFound("cblockinguserId.equals=" + (cblockinguserId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
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
        community.setAppuser(appuser);
        communityRepository.saveAndFlush(community);
        Long appuserId = appuser.getId();

        // Get all the communityList where appuser equals to appuserId
        defaultCommunityShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the communityList where appuser equals to (appuserId + 1)
        defaultCommunityShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByCalbumIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        Calbum calbum;
        if (TestUtil.findAll(em, Calbum.class).isEmpty()) {
            calbum = CalbumResourceIT.createEntity(em);
            em.persist(calbum);
            em.flush();
        } else {
            calbum = TestUtil.findAll(em, Calbum.class).get(0);
        }
        em.persist(calbum);
        em.flush();
        community.addCalbum(calbum);
        communityRepository.saveAndFlush(community);
        Long calbumId = calbum.getId();

        // Get all the communityList where calbum equals to calbumId
        defaultCommunityShouldBeFound("calbumId.equals=" + calbumId);

        // Get all the communityList where calbum equals to (calbumId + 1)
        defaultCommunityShouldNotBeFound("calbumId.equals=" + (calbumId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByCinterestIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        Cinterest cinterest;
        if (TestUtil.findAll(em, Cinterest.class).isEmpty()) {
            cinterest = CinterestResourceIT.createEntity(em);
            em.persist(cinterest);
            em.flush();
        } else {
            cinterest = TestUtil.findAll(em, Cinterest.class).get(0);
        }
        em.persist(cinterest);
        em.flush();
        community.addCinterest(cinterest);
        communityRepository.saveAndFlush(community);
        Long cinterestId = cinterest.getId();

        // Get all the communityList where cinterest equals to cinterestId
        defaultCommunityShouldBeFound("cinterestId.equals=" + cinterestId);

        // Get all the communityList where cinterest equals to (cinterestId + 1)
        defaultCommunityShouldNotBeFound("cinterestId.equals=" + (cinterestId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByCactivityIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        Cactivity cactivity;
        if (TestUtil.findAll(em, Cactivity.class).isEmpty()) {
            cactivity = CactivityResourceIT.createEntity(em);
            em.persist(cactivity);
            em.flush();
        } else {
            cactivity = TestUtil.findAll(em, Cactivity.class).get(0);
        }
        em.persist(cactivity);
        em.flush();
        community.addCactivity(cactivity);
        communityRepository.saveAndFlush(community);
        Long cactivityId = cactivity.getId();

        // Get all the communityList where cactivity equals to cactivityId
        defaultCommunityShouldBeFound("cactivityId.equals=" + cactivityId);

        // Get all the communityList where cactivity equals to (cactivityId + 1)
        defaultCommunityShouldNotBeFound("cactivityId.equals=" + (cactivityId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByCcelebIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        Cceleb cceleb;
        if (TestUtil.findAll(em, Cceleb.class).isEmpty()) {
            cceleb = CcelebResourceIT.createEntity(em);
            em.persist(cceleb);
            em.flush();
        } else {
            cceleb = TestUtil.findAll(em, Cceleb.class).get(0);
        }
        em.persist(cceleb);
        em.flush();
        community.addCceleb(cceleb);
        communityRepository.saveAndFlush(community);
        Long ccelebId = cceleb.getId();

        // Get all the communityList where cceleb equals to ccelebId
        defaultCommunityShouldBeFound("ccelebId.equals=" + ccelebId);

        // Get all the communityList where cceleb equals to (ccelebId + 1)
        defaultCommunityShouldNotBeFound("ccelebId.equals=" + (ccelebId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommunityShouldBeFound(String filter) throws Exception {
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(community.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].communityName").value(hasItem(DEFAULT_COMMUNITY_NAME)))
            .andExpect(jsonPath("$.[*].communityDescription").value(hasItem(DEFAULT_COMMUNITY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommunityShouldNotBeFound(String filter) throws Exception {
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommunity() throws Exception {
        // Get the community
        restCommunityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommunity() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        int databaseSizeBeforeUpdate = communityRepository.findAll().size();

        // Update the community
        Community updatedCommunity = communityRepository.findById(community.getId()).get();
        // Disconnect from session so that the updates on updatedCommunity are not directly saved in db
        em.detach(updatedCommunity);
        updatedCommunity
            .creationDate(UPDATED_CREATION_DATE)
            .communityName(UPDATED_COMMUNITY_NAME)
            .communityDescription(UPDATED_COMMUNITY_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE);
        CommunityDTO communityDTO = communityMapper.toDto(updatedCommunity);

        restCommunityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
        Community testCommunity = communityList.get(communityList.size() - 1);
        assertThat(testCommunity.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCommunity.getCommunityName()).isEqualTo(UPDATED_COMMUNITY_NAME);
        assertThat(testCommunity.getCommunityDescription()).isEqualTo(UPDATED_COMMUNITY_DESCRIPTION);
        assertThat(testCommunity.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCommunity.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCommunity.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the Community in Elasticsearch
        verify(mockCommunitySearchRepository).save(testCommunity);
    }

    @Test
    @Transactional
    void putNonExistingCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Community in Elasticsearch
        verify(mockCommunitySearchRepository, times(0)).save(community);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Community in Elasticsearch
        verify(mockCommunitySearchRepository, times(0)).save(community);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Community in Elasticsearch
        verify(mockCommunitySearchRepository, times(0)).save(community);
    }

    @Test
    @Transactional
    void partialUpdateCommunityWithPatch() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        int databaseSizeBeforeUpdate = communityRepository.findAll().size();

        // Update the community using partial update
        Community partialUpdatedCommunity = new Community();
        partialUpdatedCommunity.setId(community.getId());

        partialUpdatedCommunity
            .communityName(UPDATED_COMMUNITY_NAME)
            .communityDescription(UPDATED_COMMUNITY_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunity))
            )
            .andExpect(status().isOk());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
        Community testCommunity = communityList.get(communityList.size() - 1);
        assertThat(testCommunity.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCommunity.getCommunityName()).isEqualTo(UPDATED_COMMUNITY_NAME);
        assertThat(testCommunity.getCommunityDescription()).isEqualTo(UPDATED_COMMUNITY_DESCRIPTION);
        assertThat(testCommunity.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCommunity.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCommunity.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateCommunityWithPatch() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        int databaseSizeBeforeUpdate = communityRepository.findAll().size();

        // Update the community using partial update
        Community partialUpdatedCommunity = new Community();
        partialUpdatedCommunity.setId(community.getId());

        partialUpdatedCommunity
            .creationDate(UPDATED_CREATION_DATE)
            .communityName(UPDATED_COMMUNITY_NAME)
            .communityDescription(UPDATED_COMMUNITY_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE);

        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunity))
            )
            .andExpect(status().isOk());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
        Community testCommunity = communityList.get(communityList.size() - 1);
        assertThat(testCommunity.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCommunity.getCommunityName()).isEqualTo(UPDATED_COMMUNITY_NAME);
        assertThat(testCommunity.getCommunityDescription()).isEqualTo(UPDATED_COMMUNITY_DESCRIPTION);
        assertThat(testCommunity.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCommunity.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCommunity.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Community in Elasticsearch
        verify(mockCommunitySearchRepository, times(0)).save(community);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Community in Elasticsearch
        verify(mockCommunitySearchRepository, times(0)).save(community);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(communityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Community in Elasticsearch
        verify(mockCommunitySearchRepository, times(0)).save(community);
    }

    @Test
    @Transactional
    void deleteCommunity() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        int databaseSizeBeforeDelete = communityRepository.findAll().size();

        // Delete the community
        restCommunityMockMvc
            .perform(delete(ENTITY_API_URL_ID, community.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Community in Elasticsearch
        verify(mockCommunitySearchRepository, times(1)).deleteById(community.getId());
    }

    @Test
    @Transactional
    void searchCommunity() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        communityRepository.saveAndFlush(community);
        when(mockCommunitySearchRepository.search("id:" + community.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(community), PageRequest.of(0, 1), 1));

        // Search the community
        restCommunityMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + community.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(community.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].communityName").value(hasItem(DEFAULT_COMMUNITY_NAME)))
            .andExpect(jsonPath("$.[*].communityDescription").value(hasItem(DEFAULT_COMMUNITY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
