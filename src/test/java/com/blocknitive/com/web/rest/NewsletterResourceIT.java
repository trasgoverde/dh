package com.blocknitive.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blocknitive.com.IntegrationTest;
import com.blocknitive.com.domain.Newsletter;
import com.blocknitive.com.repository.NewsletterRepository;
import com.blocknitive.com.repository.search.NewsletterSearchRepository;
import com.blocknitive.com.service.criteria.NewsletterCriteria;
import com.blocknitive.com.service.dto.NewsletterDTO;
import com.blocknitive.com.service.mapper.NewsletterMapper;
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
 * Integration tests for the {@link NewsletterResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NewsletterResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/newsletters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/newsletters";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NewsletterRepository newsletterRepository;

    @Autowired
    private NewsletterMapper newsletterMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.NewsletterSearchRepositoryMockConfiguration
     */
    @Autowired
    private NewsletterSearchRepository mockNewsletterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNewsletterMockMvc;

    private Newsletter newsletter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Newsletter createEntity(EntityManager em) {
        Newsletter newsletter = new Newsletter().creationDate(DEFAULT_CREATION_DATE).email(DEFAULT_EMAIL);
        return newsletter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Newsletter createUpdatedEntity(EntityManager em) {
        Newsletter newsletter = new Newsletter().creationDate(UPDATED_CREATION_DATE).email(UPDATED_EMAIL);
        return newsletter;
    }

    @BeforeEach
    public void initTest() {
        newsletter = createEntity(em);
    }

    @Test
    @Transactional
    void createNewsletter() throws Exception {
        int databaseSizeBeforeCreate = newsletterRepository.findAll().size();
        // Create the Newsletter
        NewsletterDTO newsletterDTO = newsletterMapper.toDto(newsletter);
        restNewsletterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsletterDTO)))
            .andExpect(status().isCreated());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeCreate + 1);
        Newsletter testNewsletter = newsletterList.get(newsletterList.size() - 1);
        assertThat(testNewsletter.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testNewsletter.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Newsletter in Elasticsearch
        verify(mockNewsletterSearchRepository, times(1)).save(testNewsletter);
    }

    @Test
    @Transactional
    void createNewsletterWithExistingId() throws Exception {
        // Create the Newsletter with an existing ID
        newsletter.setId(1L);
        NewsletterDTO newsletterDTO = newsletterMapper.toDto(newsletter);

        int databaseSizeBeforeCreate = newsletterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNewsletterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsletterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeCreate);

        // Validate the Newsletter in Elasticsearch
        verify(mockNewsletterSearchRepository, times(0)).save(newsletter);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsletterRepository.findAll().size();
        // set the field null
        newsletter.setCreationDate(null);

        // Create the Newsletter, which fails.
        NewsletterDTO newsletterDTO = newsletterMapper.toDto(newsletter);

        restNewsletterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsletterDTO)))
            .andExpect(status().isBadRequest());

        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsletterRepository.findAll().size();
        // set the field null
        newsletter.setEmail(null);

        // Create the Newsletter, which fails.
        NewsletterDTO newsletterDTO = newsletterMapper.toDto(newsletter);

        restNewsletterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsletterDTO)))
            .andExpect(status().isBadRequest());

        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNewsletters() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList
        restNewsletterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(newsletter.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getNewsletter() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get the newsletter
        restNewsletterMockMvc
            .perform(get(ENTITY_API_URL_ID, newsletter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(newsletter.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNewslettersByIdFiltering() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        Long id = newsletter.getId();

        defaultNewsletterShouldBeFound("id.equals=" + id);
        defaultNewsletterShouldNotBeFound("id.notEquals=" + id);

        defaultNewsletterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNewsletterShouldNotBeFound("id.greaterThan=" + id);

        defaultNewsletterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNewsletterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNewslettersByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where creationDate equals to DEFAULT_CREATION_DATE
        defaultNewsletterShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the newsletterList where creationDate equals to UPDATED_CREATION_DATE
        defaultNewsletterShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllNewslettersByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultNewsletterShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the newsletterList where creationDate not equals to UPDATED_CREATION_DATE
        defaultNewsletterShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllNewslettersByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultNewsletterShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the newsletterList where creationDate equals to UPDATED_CREATION_DATE
        defaultNewsletterShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllNewslettersByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where creationDate is not null
        defaultNewsletterShouldBeFound("creationDate.specified=true");

        // Get all the newsletterList where creationDate is null
        defaultNewsletterShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNewslettersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email equals to DEFAULT_EMAIL
        defaultNewsletterShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the newsletterList where email equals to UPDATED_EMAIL
        defaultNewsletterShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNewslettersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email not equals to DEFAULT_EMAIL
        defaultNewsletterShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the newsletterList where email not equals to UPDATED_EMAIL
        defaultNewsletterShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNewslettersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultNewsletterShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the newsletterList where email equals to UPDATED_EMAIL
        defaultNewsletterShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNewslettersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email is not null
        defaultNewsletterShouldBeFound("email.specified=true");

        // Get all the newsletterList where email is null
        defaultNewsletterShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllNewslettersByEmailContainsSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email contains DEFAULT_EMAIL
        defaultNewsletterShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the newsletterList where email contains UPDATED_EMAIL
        defaultNewsletterShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllNewslettersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email does not contain DEFAULT_EMAIL
        defaultNewsletterShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the newsletterList where email does not contain UPDATED_EMAIL
        defaultNewsletterShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNewsletterShouldBeFound(String filter) throws Exception {
        restNewsletterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(newsletter.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restNewsletterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNewsletterShouldNotBeFound(String filter) throws Exception {
        restNewsletterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNewsletterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNewsletter() throws Exception {
        // Get the newsletter
        restNewsletterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNewsletter() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        int databaseSizeBeforeUpdate = newsletterRepository.findAll().size();

        // Update the newsletter
        Newsletter updatedNewsletter = newsletterRepository.findById(newsletter.getId()).get();
        // Disconnect from session so that the updates on updatedNewsletter are not directly saved in db
        em.detach(updatedNewsletter);
        updatedNewsletter.creationDate(UPDATED_CREATION_DATE).email(UPDATED_EMAIL);
        NewsletterDTO newsletterDTO = newsletterMapper.toDto(updatedNewsletter);

        restNewsletterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, newsletterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(newsletterDTO))
            )
            .andExpect(status().isOk());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeUpdate);
        Newsletter testNewsletter = newsletterList.get(newsletterList.size() - 1);
        assertThat(testNewsletter.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testNewsletter.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Newsletter in Elasticsearch
        verify(mockNewsletterSearchRepository).save(testNewsletter);
    }

    @Test
    @Transactional
    void putNonExistingNewsletter() throws Exception {
        int databaseSizeBeforeUpdate = newsletterRepository.findAll().size();
        newsletter.setId(count.incrementAndGet());

        // Create the Newsletter
        NewsletterDTO newsletterDTO = newsletterMapper.toDto(newsletter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewsletterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, newsletterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(newsletterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Newsletter in Elasticsearch
        verify(mockNewsletterSearchRepository, times(0)).save(newsletter);
    }

    @Test
    @Transactional
    void putWithIdMismatchNewsletter() throws Exception {
        int databaseSizeBeforeUpdate = newsletterRepository.findAll().size();
        newsletter.setId(count.incrementAndGet());

        // Create the Newsletter
        NewsletterDTO newsletterDTO = newsletterMapper.toDto(newsletter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsletterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(newsletterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Newsletter in Elasticsearch
        verify(mockNewsletterSearchRepository, times(0)).save(newsletter);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNewsletter() throws Exception {
        int databaseSizeBeforeUpdate = newsletterRepository.findAll().size();
        newsletter.setId(count.incrementAndGet());

        // Create the Newsletter
        NewsletterDTO newsletterDTO = newsletterMapper.toDto(newsletter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsletterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newsletterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Newsletter in Elasticsearch
        verify(mockNewsletterSearchRepository, times(0)).save(newsletter);
    }

    @Test
    @Transactional
    void partialUpdateNewsletterWithPatch() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        int databaseSizeBeforeUpdate = newsletterRepository.findAll().size();

        // Update the newsletter using partial update
        Newsletter partialUpdatedNewsletter = new Newsletter();
        partialUpdatedNewsletter.setId(newsletter.getId());

        partialUpdatedNewsletter.email(UPDATED_EMAIL);

        restNewsletterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNewsletter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNewsletter))
            )
            .andExpect(status().isOk());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeUpdate);
        Newsletter testNewsletter = newsletterList.get(newsletterList.size() - 1);
        assertThat(testNewsletter.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testNewsletter.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateNewsletterWithPatch() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        int databaseSizeBeforeUpdate = newsletterRepository.findAll().size();

        // Update the newsletter using partial update
        Newsletter partialUpdatedNewsletter = new Newsletter();
        partialUpdatedNewsletter.setId(newsletter.getId());

        partialUpdatedNewsletter.creationDate(UPDATED_CREATION_DATE).email(UPDATED_EMAIL);

        restNewsletterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNewsletter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNewsletter))
            )
            .andExpect(status().isOk());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeUpdate);
        Newsletter testNewsletter = newsletterList.get(newsletterList.size() - 1);
        assertThat(testNewsletter.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testNewsletter.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingNewsletter() throws Exception {
        int databaseSizeBeforeUpdate = newsletterRepository.findAll().size();
        newsletter.setId(count.incrementAndGet());

        // Create the Newsletter
        NewsletterDTO newsletterDTO = newsletterMapper.toDto(newsletter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewsletterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, newsletterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(newsletterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Newsletter in Elasticsearch
        verify(mockNewsletterSearchRepository, times(0)).save(newsletter);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNewsletter() throws Exception {
        int databaseSizeBeforeUpdate = newsletterRepository.findAll().size();
        newsletter.setId(count.incrementAndGet());

        // Create the Newsletter
        NewsletterDTO newsletterDTO = newsletterMapper.toDto(newsletter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsletterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(newsletterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Newsletter in Elasticsearch
        verify(mockNewsletterSearchRepository, times(0)).save(newsletter);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNewsletter() throws Exception {
        int databaseSizeBeforeUpdate = newsletterRepository.findAll().size();
        newsletter.setId(count.incrementAndGet());

        // Create the Newsletter
        NewsletterDTO newsletterDTO = newsletterMapper.toDto(newsletter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNewsletterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(newsletterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Newsletter in Elasticsearch
        verify(mockNewsletterSearchRepository, times(0)).save(newsletter);
    }

    @Test
    @Transactional
    void deleteNewsletter() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        int databaseSizeBeforeDelete = newsletterRepository.findAll().size();

        // Delete the newsletter
        restNewsletterMockMvc
            .perform(delete(ENTITY_API_URL_ID, newsletter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Newsletter in Elasticsearch
        verify(mockNewsletterSearchRepository, times(1)).deleteById(newsletter.getId());
    }

    @Test
    @Transactional
    void searchNewsletter() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);
        when(mockNewsletterSearchRepository.search("id:" + newsletter.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(newsletter), PageRequest.of(0, 1), 1));

        // Search the newsletter
        restNewsletterMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + newsletter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(newsletter.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }
}
