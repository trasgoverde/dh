package com.blocknitive.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blocknitive.com.IntegrationTest;
import com.blocknitive.com.domain.Album;
import com.blocknitive.com.domain.Calbum;
import com.blocknitive.com.domain.Photo;
import com.blocknitive.com.repository.PhotoRepository;
import com.blocknitive.com.repository.search.PhotoSearchRepository;
import com.blocknitive.com.service.criteria.PhotoCriteria;
import com.blocknitive.com.service.dto.PhotoDTO;
import com.blocknitive.com.service.mapper.PhotoMapper;
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
 * Integration tests for the {@link PhotoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PhotoResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/photos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/photos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoMapper photoMapper;

    /**
     * This repository is mocked in the com.blocknitive.com.repository.search test package.
     *
     * @see com.blocknitive.com.repository.search.PhotoSearchRepositoryMockConfiguration
     */
    @Autowired
    private PhotoSearchRepository mockPhotoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhotoMockMvc;

    private Photo photo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photo createEntity(EntityManager em) {
        Photo photo = new Photo().creationDate(DEFAULT_CREATION_DATE).image(DEFAULT_IMAGE).imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return photo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photo createUpdatedEntity(EntityManager em) {
        Photo photo = new Photo().creationDate(UPDATED_CREATION_DATE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return photo;
    }

    @BeforeEach
    public void initTest() {
        photo = createEntity(em);
    }

    @Test
    @Transactional
    void createPhoto() throws Exception {
        int databaseSizeBeforeCreate = photoRepository.findAll().size();
        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);
        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isCreated());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeCreate + 1);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPhoto.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPhoto.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the Photo in Elasticsearch
        verify(mockPhotoSearchRepository, times(1)).save(testPhoto);
    }

    @Test
    @Transactional
    void createPhotoWithExistingId() throws Exception {
        // Create the Photo with an existing ID
        photo.setId(1L);
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        int databaseSizeBeforeCreate = photoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Photo in Elasticsearch
        verify(mockPhotoSearchRepository, times(0)).save(photo);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setCreationDate(null);

        // Create the Photo, which fails.
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isBadRequest());

        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPhotos() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getPhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get the photo
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL_ID, photo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(photo.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getPhotosByIdFiltering() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        Long id = photo.getId();

        defaultPhotoShouldBeFound("id.equals=" + id);
        defaultPhotoShouldNotBeFound("id.notEquals=" + id);

        defaultPhotoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPhotoShouldNotBeFound("id.greaterThan=" + id);

        defaultPhotoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPhotoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPhotosByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where creationDate equals to DEFAULT_CREATION_DATE
        defaultPhotoShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the photoList where creationDate equals to UPDATED_CREATION_DATE
        defaultPhotoShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllPhotosByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultPhotoShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the photoList where creationDate not equals to UPDATED_CREATION_DATE
        defaultPhotoShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllPhotosByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultPhotoShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the photoList where creationDate equals to UPDATED_CREATION_DATE
        defaultPhotoShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllPhotosByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where creationDate is not null
        defaultPhotoShouldBeFound("creationDate.specified=true");

        // Get all the photoList where creationDate is null
        defaultPhotoShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPhotosByAlbumIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);
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
        photo.setAlbum(album);
        photoRepository.saveAndFlush(photo);
        Long albumId = album.getId();

        // Get all the photoList where album equals to albumId
        defaultPhotoShouldBeFound("albumId.equals=" + albumId);

        // Get all the photoList where album equals to (albumId + 1)
        defaultPhotoShouldNotBeFound("albumId.equals=" + (albumId + 1));
    }

    @Test
    @Transactional
    void getAllPhotosByCalbumIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);
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
        photo.setCalbum(calbum);
        photoRepository.saveAndFlush(photo);
        Long calbumId = calbum.getId();

        // Get all the photoList where calbum equals to calbumId
        defaultPhotoShouldBeFound("calbumId.equals=" + calbumId);

        // Get all the photoList where calbum equals to (calbumId + 1)
        defaultPhotoShouldNotBeFound("calbumId.equals=" + (calbumId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPhotoShouldBeFound(String filter) throws Exception {
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPhotoShouldNotBeFound(String filter) throws Exception {
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPhoto() throws Exception {
        // Get the photo
        restPhotoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo
        Photo updatedPhoto = photoRepository.findById(photo.getId()).get();
        // Disconnect from session so that the updates on updatedPhoto are not directly saved in db
        em.detach(updatedPhoto);
        updatedPhoto.creationDate(UPDATED_CREATION_DATE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        PhotoDTO photoDTO = photoMapper.toDto(updatedPhoto);

        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, photoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPhoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPhoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the Photo in Elasticsearch
        verify(mockPhotoSearchRepository).save(testPhoto);
    }

    @Test
    @Transactional
    void putNonExistingPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, photoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Photo in Elasticsearch
        verify(mockPhotoSearchRepository, times(0)).save(photo);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Photo in Elasticsearch
        verify(mockPhotoSearchRepository, times(0)).save(photo);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Photo in Elasticsearch
        verify(mockPhotoSearchRepository, times(0)).save(photo);
    }

    @Test
    @Transactional
    void partialUpdatePhotoWithPatch() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo using partial update
        Photo partialUpdatedPhoto = new Photo();
        partialUpdatedPhoto.setId(photo.getId());

        partialUpdatedPhoto.creationDate(UPDATED_CREATION_DATE);

        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoto))
            )
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPhoto.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPhoto.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePhotoWithPatch() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo using partial update
        Photo partialUpdatedPhoto = new Photo();
        partialUpdatedPhoto.setId(photo.getId());

        partialUpdatedPhoto.creationDate(UPDATED_CREATION_DATE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoto))
            )
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPhoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPhoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, photoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Photo in Elasticsearch
        verify(mockPhotoSearchRepository, times(0)).save(photo);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Photo in Elasticsearch
        verify(mockPhotoSearchRepository, times(0)).save(photo);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Photo in Elasticsearch
        verify(mockPhotoSearchRepository, times(0)).save(photo);
    }

    @Test
    @Transactional
    void deletePhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeDelete = photoRepository.findAll().size();

        // Delete the photo
        restPhotoMockMvc
            .perform(delete(ENTITY_API_URL_ID, photo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Photo in Elasticsearch
        verify(mockPhotoSearchRepository, times(1)).deleteById(photo.getId());
    }

    @Test
    @Transactional
    void searchPhoto() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        photoRepository.saveAndFlush(photo);
        when(mockPhotoSearchRepository.search("id:" + photo.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(photo), PageRequest.of(0, 1), 1));

        // Search the photo
        restPhotoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + photo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
}
