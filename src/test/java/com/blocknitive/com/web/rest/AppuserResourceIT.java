package com.blocknitive.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blocknitive.com.IntegrationTest;
import com.blocknitive.com.domain.Activity;
import com.blocknitive.com.domain.Album;
import com.blocknitive.com.domain.Appphoto;
import com.blocknitive.com.domain.Appprofile;
import com.blocknitive.com.domain.Appuser;
import com.blocknitive.com.domain.Blockuser;
import com.blocknitive.com.domain.Blog;
import com.blocknitive.com.domain.Celeb;
import com.blocknitive.com.domain.Comment;
import com.blocknitive.com.domain.Community;
import com.blocknitive.com.domain.Follow;
import com.blocknitive.com.domain.Interest;
import com.blocknitive.com.domain.Message;
import com.blocknitive.com.domain.Notification;
import com.blocknitive.com.domain.Post;
import com.blocknitive.com.domain.Proposal;
import com.blocknitive.com.domain.ProposalVote;
import com.blocknitive.com.domain.User;
import com.blocknitive.com.domain.Vanswer;
import com.blocknitive.com.domain.Vquestion;
import com.blocknitive.com.domain.Vthumb;
import com.blocknitive.com.domain.Vtopic;
import com.blocknitive.com.repository.AppuserRepository;
import com.blocknitive.com.repository.search.AppuserSearchRepository;
import com.blocknitive.com.service.criteria.AppuserCriteria;
import com.blocknitive.com.service.dto.AppuserDTO;
import com.blocknitive.com.service.mapper.AppuserMapper;
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
 * Integration tests for the {@link AppuserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AppuserResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ASSIGNED_VOTES_POINTS = 1L;
    private static final Long UPDATED_ASSIGNED_VOTES_POINTS = 2L;
    private static final Long SMALLER_ASSIGNED_VOTES_POINTS = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/appusers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/appusers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppuserRepository appuserRepository;

    @Autowired
    private AppuserMapper appuserMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.AppuserSearchRepositoryMockConfiguration
     */
    @Autowired
    private AppuserSearchRepository mockAppuserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppuserMockMvc;

    private Appuser appuser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appuser createEntity(EntityManager em) {
        Appuser appuser = new Appuser().creationDate(DEFAULT_CREATION_DATE).assignedVotesPoints(DEFAULT_ASSIGNED_VOTES_POINTS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        appuser.setUser(user);
        return appuser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appuser createUpdatedEntity(EntityManager em) {
        Appuser appuser = new Appuser().creationDate(UPDATED_CREATION_DATE).assignedVotesPoints(UPDATED_ASSIGNED_VOTES_POINTS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        appuser.setUser(user);
        return appuser;
    }

    @BeforeEach
    public void initTest() {
        appuser = createEntity(em);
    }

    @Test
    @Transactional
    void createAppuser() throws Exception {
        int databaseSizeBeforeCreate = appuserRepository.findAll().size();
        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);
        restAppuserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appuserDTO)))
            .andExpect(status().isCreated());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeCreate + 1);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAppuser.getAssignedVotesPoints()).isEqualTo(DEFAULT_ASSIGNED_VOTES_POINTS);

        // Validate the Appuser in Elasticsearch
        verify(mockAppuserSearchRepository, times(1)).save(testAppuser);
    }

    @Test
    @Transactional
    void createAppuserWithExistingId() throws Exception {
        // Create the Appuser with an existing ID
        appuser.setId(1L);
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        int databaseSizeBeforeCreate = appuserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppuserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appuserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeCreate);

        // Validate the Appuser in Elasticsearch
        verify(mockAppuserSearchRepository, times(0)).save(appuser);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = appuserRepository.findAll().size();
        // set the field null
        appuser.setCreationDate(null);

        // Create the Appuser, which fails.
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        restAppuserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appuserDTO)))
            .andExpect(status().isBadRequest());

        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppusers() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedVotesPoints").value(hasItem(DEFAULT_ASSIGNED_VOTES_POINTS.intValue())));
    }

    @Test
    @Transactional
    void getAppuser() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get the appuser
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL_ID, appuser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appuser.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.assignedVotesPoints").value(DEFAULT_ASSIGNED_VOTES_POINTS.intValue()));
    }

    @Test
    @Transactional
    void getAppusersByIdFiltering() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        Long id = appuser.getId();

        defaultAppuserShouldBeFound("id.equals=" + id);
        defaultAppuserShouldNotBeFound("id.notEquals=" + id);

        defaultAppuserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppuserShouldNotBeFound("id.greaterThan=" + id);

        defaultAppuserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppuserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppusersByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where creationDate equals to DEFAULT_CREATION_DATE
        defaultAppuserShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the appuserList where creationDate equals to UPDATED_CREATION_DATE
        defaultAppuserShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllAppusersByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultAppuserShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the appuserList where creationDate not equals to UPDATED_CREATION_DATE
        defaultAppuserShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllAppusersByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultAppuserShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the appuserList where creationDate equals to UPDATED_CREATION_DATE
        defaultAppuserShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllAppusersByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where creationDate is not null
        defaultAppuserShouldBeFound("creationDate.specified=true");

        // Get all the appuserList where creationDate is null
        defaultAppuserShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByAssignedVotesPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where assignedVotesPoints equals to DEFAULT_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldBeFound("assignedVotesPoints.equals=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the appuserList where assignedVotesPoints equals to UPDATED_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldNotBeFound("assignedVotesPoints.equals=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    void getAllAppusersByAssignedVotesPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where assignedVotesPoints not equals to DEFAULT_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldNotBeFound("assignedVotesPoints.notEquals=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the appuserList where assignedVotesPoints not equals to UPDATED_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldBeFound("assignedVotesPoints.notEquals=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    void getAllAppusersByAssignedVotesPointsIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where assignedVotesPoints in DEFAULT_ASSIGNED_VOTES_POINTS or UPDATED_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldBeFound("assignedVotesPoints.in=" + DEFAULT_ASSIGNED_VOTES_POINTS + "," + UPDATED_ASSIGNED_VOTES_POINTS);

        // Get all the appuserList where assignedVotesPoints equals to UPDATED_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldNotBeFound("assignedVotesPoints.in=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    void getAllAppusersByAssignedVotesPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where assignedVotesPoints is not null
        defaultAppuserShouldBeFound("assignedVotesPoints.specified=true");

        // Get all the appuserList where assignedVotesPoints is null
        defaultAppuserShouldNotBeFound("assignedVotesPoints.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByAssignedVotesPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where assignedVotesPoints is greater than or equal to DEFAULT_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldBeFound("assignedVotesPoints.greaterThanOrEqual=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the appuserList where assignedVotesPoints is greater than or equal to UPDATED_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldNotBeFound("assignedVotesPoints.greaterThanOrEqual=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    void getAllAppusersByAssignedVotesPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where assignedVotesPoints is less than or equal to DEFAULT_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldBeFound("assignedVotesPoints.lessThanOrEqual=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the appuserList where assignedVotesPoints is less than or equal to SMALLER_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldNotBeFound("assignedVotesPoints.lessThanOrEqual=" + SMALLER_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    void getAllAppusersByAssignedVotesPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where assignedVotesPoints is less than DEFAULT_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldNotBeFound("assignedVotesPoints.lessThan=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the appuserList where assignedVotesPoints is less than UPDATED_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldBeFound("assignedVotesPoints.lessThan=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    void getAllAppusersByAssignedVotesPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where assignedVotesPoints is greater than DEFAULT_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldNotBeFound("assignedVotesPoints.greaterThan=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the appuserList where assignedVotesPoints is greater than SMALLER_ASSIGNED_VOTES_POINTS
        defaultAppuserShouldBeFound("assignedVotesPoints.greaterThan=" + SMALLER_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    void getAllAppusersByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = appuser.getUser();
        appuserRepository.saveAndFlush(appuser);
        Long userId = user.getId();

        // Get all the appuserList where user equals to userId
        defaultAppuserShouldBeFound("userId.equals=" + userId);

        // Get all the appuserList where user equals to (userId + 1)
        defaultAppuserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByAppprofileIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Appprofile appprofile;
        if (TestUtil.findAll(em, Appprofile.class).isEmpty()) {
            appprofile = AppprofileResourceIT.createEntity(em);
            em.persist(appprofile);
            em.flush();
        } else {
            appprofile = TestUtil.findAll(em, Appprofile.class).get(0);
        }
        em.persist(appprofile);
        em.flush();
        appuser.setAppprofile(appprofile);
        appprofile.setAppuser(appuser);
        appuserRepository.saveAndFlush(appuser);
        Long appprofileId = appprofile.getId();

        // Get all the appuserList where appprofile equals to appprofileId
        defaultAppuserShouldBeFound("appprofileId.equals=" + appprofileId);

        // Get all the appuserList where appprofile equals to (appprofileId + 1)
        defaultAppuserShouldNotBeFound("appprofileId.equals=" + (appprofileId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByAppphotoIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Appphoto appphoto;
        if (TestUtil.findAll(em, Appphoto.class).isEmpty()) {
            appphoto = AppphotoResourceIT.createEntity(em);
            em.persist(appphoto);
            em.flush();
        } else {
            appphoto = TestUtil.findAll(em, Appphoto.class).get(0);
        }
        em.persist(appphoto);
        em.flush();
        appuser.setAppphoto(appphoto);
        appphoto.setAppuser(appuser);
        appuserRepository.saveAndFlush(appuser);
        Long appphotoId = appphoto.getId();

        // Get all the appuserList where appphoto equals to appphotoId
        defaultAppuserShouldBeFound("appphotoId.equals=" + appphotoId);

        // Get all the appuserList where appphoto equals to (appphotoId + 1)
        defaultAppuserShouldNotBeFound("appphotoId.equals=" + (appphotoId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByCommunityIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
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
        appuser.addCommunity(community);
        appuserRepository.saveAndFlush(appuser);
        Long communityId = community.getId();

        // Get all the appuserList where community equals to communityId
        defaultAppuserShouldBeFound("communityId.equals=" + communityId);

        // Get all the appuserList where community equals to (communityId + 1)
        defaultAppuserShouldNotBeFound("communityId.equals=" + (communityId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByBlogIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
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
        appuser.addBlog(blog);
        appuserRepository.saveAndFlush(appuser);
        Long blogId = blog.getId();

        // Get all the appuserList where blog equals to blogId
        defaultAppuserShouldBeFound("blogId.equals=" + blogId);

        // Get all the appuserList where blog equals to (blogId + 1)
        defaultAppuserShouldNotBeFound("blogId.equals=" + (blogId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByNotificationIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Notification notification;
        if (TestUtil.findAll(em, Notification.class).isEmpty()) {
            notification = NotificationResourceIT.createEntity(em);
            em.persist(notification);
            em.flush();
        } else {
            notification = TestUtil.findAll(em, Notification.class).get(0);
        }
        em.persist(notification);
        em.flush();
        appuser.addNotification(notification);
        appuserRepository.saveAndFlush(appuser);
        Long notificationId = notification.getId();

        // Get all the appuserList where notification equals to notificationId
        defaultAppuserShouldBeFound("notificationId.equals=" + notificationId);

        // Get all the appuserList where notification equals to (notificationId + 1)
        defaultAppuserShouldNotBeFound("notificationId.equals=" + (notificationId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByAlbumIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Album album;
        if (TestUtil.findAll(em, Album.class).isEmpty()) {
            album = AlbumResourceIT.createEntity(em);
            em.persist(album);
            em.flush();
        } else {
            album = TestUtil.findAll(em, Album.class).get(0);
        }
        em.persist(album);
        em.flush();
        appuser.addAlbum(album);
        appuserRepository.saveAndFlush(appuser);
        Long albumId = album.getId();

        // Get all the appuserList where album equals to albumId
        defaultAppuserShouldBeFound("albumId.equals=" + albumId);

        // Get all the appuserList where album equals to (albumId + 1)
        defaultAppuserShouldNotBeFound("albumId.equals=" + (albumId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Comment comment;
        if (TestUtil.findAll(em, Comment.class).isEmpty()) {
            comment = CommentResourceIT.createEntity(em);
            em.persist(comment);
            em.flush();
        } else {
            comment = TestUtil.findAll(em, Comment.class).get(0);
        }
        em.persist(comment);
        em.flush();
        appuser.addComment(comment);
        appuserRepository.saveAndFlush(appuser);
        Long commentId = comment.getId();

        // Get all the appuserList where comment equals to commentId
        defaultAppuserShouldBeFound("commentId.equals=" + commentId);

        // Get all the appuserList where comment equals to (commentId + 1)
        defaultAppuserShouldNotBeFound("commentId.equals=" + (commentId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByPostIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
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
        appuser.addPost(post);
        appuserRepository.saveAndFlush(appuser);
        Long postId = post.getId();

        // Get all the appuserList where post equals to postId
        defaultAppuserShouldBeFound("postId.equals=" + postId);

        // Get all the appuserList where post equals to (postId + 1)
        defaultAppuserShouldNotBeFound("postId.equals=" + (postId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersBySenderIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Message sender;
        if (TestUtil.findAll(em, Message.class).isEmpty()) {
            sender = MessageResourceIT.createEntity(em);
            em.persist(sender);
            em.flush();
        } else {
            sender = TestUtil.findAll(em, Message.class).get(0);
        }
        em.persist(sender);
        em.flush();
        appuser.addSender(sender);
        appuserRepository.saveAndFlush(appuser);
        Long senderId = sender.getId();

        // Get all the appuserList where sender equals to senderId
        defaultAppuserShouldBeFound("senderId.equals=" + senderId);

        // Get all the appuserList where sender equals to (senderId + 1)
        defaultAppuserShouldNotBeFound("senderId.equals=" + (senderId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByReceiverIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Message receiver;
        if (TestUtil.findAll(em, Message.class).isEmpty()) {
            receiver = MessageResourceIT.createEntity(em);
            em.persist(receiver);
            em.flush();
        } else {
            receiver = TestUtil.findAll(em, Message.class).get(0);
        }
        em.persist(receiver);
        em.flush();
        appuser.addReceiver(receiver);
        appuserRepository.saveAndFlush(appuser);
        Long receiverId = receiver.getId();

        // Get all the appuserList where receiver equals to receiverId
        defaultAppuserShouldBeFound("receiverId.equals=" + receiverId);

        // Get all the appuserList where receiver equals to (receiverId + 1)
        defaultAppuserShouldNotBeFound("receiverId.equals=" + (receiverId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByFollowedIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Follow followed;
        if (TestUtil.findAll(em, Follow.class).isEmpty()) {
            followed = FollowResourceIT.createEntity(em);
            em.persist(followed);
            em.flush();
        } else {
            followed = TestUtil.findAll(em, Follow.class).get(0);
        }
        em.persist(followed);
        em.flush();
        appuser.addFollowed(followed);
        appuserRepository.saveAndFlush(appuser);
        Long followedId = followed.getId();

        // Get all the appuserList where followed equals to followedId
        defaultAppuserShouldBeFound("followedId.equals=" + followedId);

        // Get all the appuserList where followed equals to (followedId + 1)
        defaultAppuserShouldNotBeFound("followedId.equals=" + (followedId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByFollowingIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Follow following;
        if (TestUtil.findAll(em, Follow.class).isEmpty()) {
            following = FollowResourceIT.createEntity(em);
            em.persist(following);
            em.flush();
        } else {
            following = TestUtil.findAll(em, Follow.class).get(0);
        }
        em.persist(following);
        em.flush();
        appuser.addFollowing(following);
        appuserRepository.saveAndFlush(appuser);
        Long followingId = following.getId();

        // Get all the appuserList where following equals to followingId
        defaultAppuserShouldBeFound("followingId.equals=" + followingId);

        // Get all the appuserList where following equals to (followingId + 1)
        defaultAppuserShouldNotBeFound("followingId.equals=" + (followingId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByBlockeduserIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Blockuser blockeduser;
        if (TestUtil.findAll(em, Blockuser.class).isEmpty()) {
            blockeduser = BlockuserResourceIT.createEntity(em);
            em.persist(blockeduser);
            em.flush();
        } else {
            blockeduser = TestUtil.findAll(em, Blockuser.class).get(0);
        }
        em.persist(blockeduser);
        em.flush();
        appuser.addBlockeduser(blockeduser);
        appuserRepository.saveAndFlush(appuser);
        Long blockeduserId = blockeduser.getId();

        // Get all the appuserList where blockeduser equals to blockeduserId
        defaultAppuserShouldBeFound("blockeduserId.equals=" + blockeduserId);

        // Get all the appuserList where blockeduser equals to (blockeduserId + 1)
        defaultAppuserShouldNotBeFound("blockeduserId.equals=" + (blockeduserId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByBlockinguserIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Blockuser blockinguser;
        if (TestUtil.findAll(em, Blockuser.class).isEmpty()) {
            blockinguser = BlockuserResourceIT.createEntity(em);
            em.persist(blockinguser);
            em.flush();
        } else {
            blockinguser = TestUtil.findAll(em, Blockuser.class).get(0);
        }
        em.persist(blockinguser);
        em.flush();
        appuser.addBlockinguser(blockinguser);
        appuserRepository.saveAndFlush(appuser);
        Long blockinguserId = blockinguser.getId();

        // Get all the appuserList where blockinguser equals to blockinguserId
        defaultAppuserShouldBeFound("blockinguserId.equals=" + blockinguserId);

        // Get all the appuserList where blockinguser equals to (blockinguserId + 1)
        defaultAppuserShouldNotBeFound("blockinguserId.equals=" + (blockinguserId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByVtopicIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
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
        appuser.addVtopic(vtopic);
        appuserRepository.saveAndFlush(appuser);
        Long vtopicId = vtopic.getId();

        // Get all the appuserList where vtopic equals to vtopicId
        defaultAppuserShouldBeFound("vtopicId.equals=" + vtopicId);

        // Get all the appuserList where vtopic equals to (vtopicId + 1)
        defaultAppuserShouldNotBeFound("vtopicId.equals=" + (vtopicId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByVquestionIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
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
        appuser.addVquestion(vquestion);
        appuserRepository.saveAndFlush(appuser);
        Long vquestionId = vquestion.getId();

        // Get all the appuserList where vquestion equals to vquestionId
        defaultAppuserShouldBeFound("vquestionId.equals=" + vquestionId);

        // Get all the appuserList where vquestion equals to (vquestionId + 1)
        defaultAppuserShouldNotBeFound("vquestionId.equals=" + (vquestionId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByVanswerIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
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
        appuser.addVanswer(vanswer);
        appuserRepository.saveAndFlush(appuser);
        Long vanswerId = vanswer.getId();

        // Get all the appuserList where vanswer equals to vanswerId
        defaultAppuserShouldBeFound("vanswerId.equals=" + vanswerId);

        // Get all the appuserList where vanswer equals to (vanswerId + 1)
        defaultAppuserShouldNotBeFound("vanswerId.equals=" + (vanswerId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByVthumbIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
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
        appuser.addVthumb(vthumb);
        appuserRepository.saveAndFlush(appuser);
        Long vthumbId = vthumb.getId();

        // Get all the appuserList where vthumb equals to vthumbId
        defaultAppuserShouldBeFound("vthumbId.equals=" + vthumbId);

        // Get all the appuserList where vthumb equals to (vthumbId + 1)
        defaultAppuserShouldNotBeFound("vthumbId.equals=" + (vthumbId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByProposalIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
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
        appuser.addProposal(proposal);
        appuserRepository.saveAndFlush(appuser);
        Long proposalId = proposal.getId();

        // Get all the appuserList where proposal equals to proposalId
        defaultAppuserShouldBeFound("proposalId.equals=" + proposalId);

        // Get all the appuserList where proposal equals to (proposalId + 1)
        defaultAppuserShouldNotBeFound("proposalId.equals=" + (proposalId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByProposalVoteIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
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
        appuser.addProposalVote(proposalVote);
        appuserRepository.saveAndFlush(appuser);
        Long proposalVoteId = proposalVote.getId();

        // Get all the appuserList where proposalVote equals to proposalVoteId
        defaultAppuserShouldBeFound("proposalVoteId.equals=" + proposalVoteId);

        // Get all the appuserList where proposalVote equals to (proposalVoteId + 1)
        defaultAppuserShouldNotBeFound("proposalVoteId.equals=" + (proposalVoteId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByInterestIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Interest interest;
        if (TestUtil.findAll(em, Interest.class).isEmpty()) {
            interest = InterestResourceIT.createEntity(em);
            em.persist(interest);
            em.flush();
        } else {
            interest = TestUtil.findAll(em, Interest.class).get(0);
        }
        em.persist(interest);
        em.flush();
        appuser.addInterest(interest);
        appuserRepository.saveAndFlush(appuser);
        Long interestId = interest.getId();

        // Get all the appuserList where interest equals to interestId
        defaultAppuserShouldBeFound("interestId.equals=" + interestId);

        // Get all the appuserList where interest equals to (interestId + 1)
        defaultAppuserShouldNotBeFound("interestId.equals=" + (interestId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByActivityIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Activity activity;
        if (TestUtil.findAll(em, Activity.class).isEmpty()) {
            activity = ActivityResourceIT.createEntity(em);
            em.persist(activity);
            em.flush();
        } else {
            activity = TestUtil.findAll(em, Activity.class).get(0);
        }
        em.persist(activity);
        em.flush();
        appuser.addActivity(activity);
        appuserRepository.saveAndFlush(appuser);
        Long activityId = activity.getId();

        // Get all the appuserList where activity equals to activityId
        defaultAppuserShouldBeFound("activityId.equals=" + activityId);

        // Get all the appuserList where activity equals to (activityId + 1)
        defaultAppuserShouldNotBeFound("activityId.equals=" + (activityId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByCelebIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        Celeb celeb;
        if (TestUtil.findAll(em, Celeb.class).isEmpty()) {
            celeb = CelebResourceIT.createEntity(em);
            em.persist(celeb);
            em.flush();
        } else {
            celeb = TestUtil.findAll(em, Celeb.class).get(0);
        }
        em.persist(celeb);
        em.flush();
        appuser.addCeleb(celeb);
        appuserRepository.saveAndFlush(appuser);
        Long celebId = celeb.getId();

        // Get all the appuserList where celeb equals to celebId
        defaultAppuserShouldBeFound("celebId.equals=" + celebId);

        // Get all the appuserList where celeb equals to (celebId + 1)
        defaultAppuserShouldNotBeFound("celebId.equals=" + (celebId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppuserShouldBeFound(String filter) throws Exception {
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedVotesPoints").value(hasItem(DEFAULT_ASSIGNED_VOTES_POINTS.intValue())));

        // Check, that the count call also returns 1
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppuserShouldNotBeFound(String filter) throws Exception {
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppuser() throws Exception {
        // Get the appuser
        restAppuserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppuser() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();

        // Update the appuser
        Appuser updatedAppuser = appuserRepository.findById(appuser.getId()).get();
        // Disconnect from session so that the updates on updatedAppuser are not directly saved in db
        em.detach(updatedAppuser);
        updatedAppuser.creationDate(UPDATED_CREATION_DATE).assignedVotesPoints(UPDATED_ASSIGNED_VOTES_POINTS);
        AppuserDTO appuserDTO = appuserMapper.toDto(updatedAppuser);

        restAppuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appuserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isOk());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAppuser.getAssignedVotesPoints()).isEqualTo(UPDATED_ASSIGNED_VOTES_POINTS);

        // Validate the Appuser in Elasticsearch
        verify(mockAppuserSearchRepository).save(testAppuser);
    }

    @Test
    @Transactional
    void putNonExistingAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appuserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appuser in Elasticsearch
        verify(mockAppuserSearchRepository, times(0)).save(appuser);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appuser in Elasticsearch
        verify(mockAppuserSearchRepository, times(0)).save(appuser);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appuserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appuser in Elasticsearch
        verify(mockAppuserSearchRepository, times(0)).save(appuser);
    }

    @Test
    @Transactional
    void partialUpdateAppuserWithPatch() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();

        // Update the appuser using partial update
        Appuser partialUpdatedAppuser = new Appuser();
        partialUpdatedAppuser.setId(appuser.getId());

        partialUpdatedAppuser.assignedVotesPoints(UPDATED_ASSIGNED_VOTES_POINTS);

        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppuser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppuser))
            )
            .andExpect(status().isOk());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAppuser.getAssignedVotesPoints()).isEqualTo(UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    void fullUpdateAppuserWithPatch() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();

        // Update the appuser using partial update
        Appuser partialUpdatedAppuser = new Appuser();
        partialUpdatedAppuser.setId(appuser.getId());

        partialUpdatedAppuser.creationDate(UPDATED_CREATION_DATE).assignedVotesPoints(UPDATED_ASSIGNED_VOTES_POINTS);

        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppuser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppuser))
            )
            .andExpect(status().isOk());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAppuser.getAssignedVotesPoints()).isEqualTo(UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appuserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appuser in Elasticsearch
        verify(mockAppuserSearchRepository, times(0)).save(appuser);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appuser in Elasticsearch
        verify(mockAppuserSearchRepository, times(0)).save(appuser);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appuser in Elasticsearch
        verify(mockAppuserSearchRepository, times(0)).save(appuser);
    }

    @Test
    @Transactional
    void deleteAppuser() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeDelete = appuserRepository.findAll().size();

        // Delete the appuser
        restAppuserMockMvc
            .perform(delete(ENTITY_API_URL_ID, appuser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Appuser in Elasticsearch
        verify(mockAppuserSearchRepository, times(1)).deleteById(appuser.getId());
    }

    @Test
    @Transactional
    void searchAppuser() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);
        when(mockAppuserSearchRepository.search("id:" + appuser.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(appuser), PageRequest.of(0, 1), 1));

        // Search the appuser
        restAppuserMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + appuser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedVotesPoints").value(hasItem(DEFAULT_ASSIGNED_VOTES_POINTS.intValue())));
    }
}
