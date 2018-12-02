package com.mastertek.cabcaller.web.rest;

import com.mastertek.cabcaller.CabcallerApp;

import com.mastertek.cabcaller.domain.CabinGroupUser;
import com.mastertek.cabcaller.repository.CabinGroupUserRepository;
import com.mastertek.cabcaller.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mastertek.cabcaller.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CabinGroupUserResource REST controller.
 *
 * @see CabinGroupUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CabcallerApp.class)
public class CabinGroupUserResourceIntTest {

    private static final Long DEFAULT_GROUP_ID = 1L;
    private static final Long UPDATED_GROUP_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Autowired
    private CabinGroupUserRepository cabinGroupUserRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCabinGroupUserMockMvc;

    private CabinGroupUser cabinGroupUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CabinGroupUserResource cabinGroupUserResource = new CabinGroupUserResource(cabinGroupUserRepository);
        this.restCabinGroupUserMockMvc = MockMvcBuilders.standaloneSetup(cabinGroupUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CabinGroupUser createEntity(EntityManager em) {
        CabinGroupUser cabinGroupUser = new CabinGroupUser()
            .groupID(DEFAULT_GROUP_ID)
            .userID(DEFAULT_USER_ID);
        return cabinGroupUser;
    }

    @Before
    public void initTest() {
        cabinGroupUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createCabinGroupUser() throws Exception {
        int databaseSizeBeforeCreate = cabinGroupUserRepository.findAll().size();

        // Create the CabinGroupUser
        restCabinGroupUserMockMvc.perform(post("/api/cabin-group-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroupUser)))
            .andExpect(status().isCreated());

        // Validate the CabinGroupUser in the database
        List<CabinGroupUser> cabinGroupUserList = cabinGroupUserRepository.findAll();
        assertThat(cabinGroupUserList).hasSize(databaseSizeBeforeCreate + 1);
        CabinGroupUser testCabinGroupUser = cabinGroupUserList.get(cabinGroupUserList.size() - 1);
        assertThat(testCabinGroupUser.getGroupID()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testCabinGroupUser.getUserID()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    public void createCabinGroupUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cabinGroupUserRepository.findAll().size();

        // Create the CabinGroupUser with an existing ID
        cabinGroupUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCabinGroupUserMockMvc.perform(post("/api/cabin-group-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroupUser)))
            .andExpect(status().isBadRequest());

        // Validate the CabinGroupUser in the database
        List<CabinGroupUser> cabinGroupUserList = cabinGroupUserRepository.findAll();
        assertThat(cabinGroupUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGroupIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = cabinGroupUserRepository.findAll().size();
        // set the field null
        cabinGroupUser.setGroupID(null);

        // Create the CabinGroupUser, which fails.

        restCabinGroupUserMockMvc.perform(post("/api/cabin-group-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroupUser)))
            .andExpect(status().isBadRequest());

        List<CabinGroupUser> cabinGroupUserList = cabinGroupUserRepository.findAll();
        assertThat(cabinGroupUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = cabinGroupUserRepository.findAll().size();
        // set the field null
        cabinGroupUser.setUserID(null);

        // Create the CabinGroupUser, which fails.

        restCabinGroupUserMockMvc.perform(post("/api/cabin-group-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroupUser)))
            .andExpect(status().isBadRequest());

        List<CabinGroupUser> cabinGroupUserList = cabinGroupUserRepository.findAll();
        assertThat(cabinGroupUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCabinGroupUsers() throws Exception {
        // Initialize the database
        cabinGroupUserRepository.saveAndFlush(cabinGroupUser);

        // Get all the cabinGroupUserList
        restCabinGroupUserMockMvc.perform(get("/api/cabin-group-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cabinGroupUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupID").value(hasItem(DEFAULT_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getCabinGroupUser() throws Exception {
        // Initialize the database
        cabinGroupUserRepository.saveAndFlush(cabinGroupUser);

        // Get the cabinGroupUser
        restCabinGroupUserMockMvc.perform(get("/api/cabin-group-users/{id}", cabinGroupUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cabinGroupUser.getId().intValue()))
            .andExpect(jsonPath("$.groupID").value(DEFAULT_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.userID").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCabinGroupUser() throws Exception {
        // Get the cabinGroupUser
        restCabinGroupUserMockMvc.perform(get("/api/cabin-group-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCabinGroupUser() throws Exception {
        // Initialize the database
        cabinGroupUserRepository.saveAndFlush(cabinGroupUser);
        int databaseSizeBeforeUpdate = cabinGroupUserRepository.findAll().size();

        // Update the cabinGroupUser
        CabinGroupUser updatedCabinGroupUser = cabinGroupUserRepository.findOne(cabinGroupUser.getId());
        // Disconnect from session so that the updates on updatedCabinGroupUser are not directly saved in db
        em.detach(updatedCabinGroupUser);
        updatedCabinGroupUser
            .groupID(UPDATED_GROUP_ID)
            .userID(UPDATED_USER_ID);

        restCabinGroupUserMockMvc.perform(put("/api/cabin-group-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCabinGroupUser)))
            .andExpect(status().isOk());

        // Validate the CabinGroupUser in the database
        List<CabinGroupUser> cabinGroupUserList = cabinGroupUserRepository.findAll();
        assertThat(cabinGroupUserList).hasSize(databaseSizeBeforeUpdate);
        CabinGroupUser testCabinGroupUser = cabinGroupUserList.get(cabinGroupUserList.size() - 1);
        assertThat(testCabinGroupUser.getGroupID()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testCabinGroupUser.getUserID()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingCabinGroupUser() throws Exception {
        int databaseSizeBeforeUpdate = cabinGroupUserRepository.findAll().size();

        // Create the CabinGroupUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCabinGroupUserMockMvc.perform(put("/api/cabin-group-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroupUser)))
            .andExpect(status().isCreated());

        // Validate the CabinGroupUser in the database
        List<CabinGroupUser> cabinGroupUserList = cabinGroupUserRepository.findAll();
        assertThat(cabinGroupUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCabinGroupUser() throws Exception {
        // Initialize the database
        cabinGroupUserRepository.saveAndFlush(cabinGroupUser);
        int databaseSizeBeforeDelete = cabinGroupUserRepository.findAll().size();

        // Get the cabinGroupUser
        restCabinGroupUserMockMvc.perform(delete("/api/cabin-group-users/{id}", cabinGroupUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CabinGroupUser> cabinGroupUserList = cabinGroupUserRepository.findAll();
        assertThat(cabinGroupUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CabinGroupUser.class);
        CabinGroupUser cabinGroupUser1 = new CabinGroupUser();
        cabinGroupUser1.setId(1L);
        CabinGroupUser cabinGroupUser2 = new CabinGroupUser();
        cabinGroupUser2.setId(cabinGroupUser1.getId());
        assertThat(cabinGroupUser1).isEqualTo(cabinGroupUser2);
        cabinGroupUser2.setId(2L);
        assertThat(cabinGroupUser1).isNotEqualTo(cabinGroupUser2);
        cabinGroupUser1.setId(null);
        assertThat(cabinGroupUser1).isNotEqualTo(cabinGroupUser2);
    }
}
