package com.mastertek.cabcaller.web.rest;

import com.mastertek.cabcaller.CabcallerApp;

import com.mastertek.cabcaller.domain.CabinGroupUserHistory;
import com.mastertek.cabcaller.repository.CabinGroupUserHistoryRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.mastertek.cabcaller.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CabinGroupUserHistoryResource REST controller.
 *
 * @see CabinGroupUserHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CabcallerApp.class)
public class CabinGroupUserHistoryResourceIntTest {

    private static final Long DEFAULT_GROUP_ID = 1L;
    private static final Long UPDATED_GROUP_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final LocalDate DEFAULT_ACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTION_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CabinGroupUserHistoryRepository cabinGroupUserHistoryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCabinGroupUserHistoryMockMvc;

    private CabinGroupUserHistory cabinGroupUserHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CabinGroupUserHistoryResource cabinGroupUserHistoryResource = new CabinGroupUserHistoryResource(cabinGroupUserHistoryRepository);
        this.restCabinGroupUserHistoryMockMvc = MockMvcBuilders.standaloneSetup(cabinGroupUserHistoryResource)
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
    public static CabinGroupUserHistory createEntity(EntityManager em) {
        CabinGroupUserHistory cabinGroupUserHistory = new CabinGroupUserHistory()
            .groupID(DEFAULT_GROUP_ID)
            .userID(DEFAULT_USER_ID)
            .actionDate(DEFAULT_ACTION_DATE);
        return cabinGroupUserHistory;
    }

    @Before
    public void initTest() {
        cabinGroupUserHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createCabinGroupUserHistory() throws Exception {
        int databaseSizeBeforeCreate = cabinGroupUserHistoryRepository.findAll().size();

        // Create the CabinGroupUserHistory
        restCabinGroupUserHistoryMockMvc.perform(post("/api/cabin-group-user-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroupUserHistory)))
            .andExpect(status().isCreated());

        // Validate the CabinGroupUserHistory in the database
        List<CabinGroupUserHistory> cabinGroupUserHistoryList = cabinGroupUserHistoryRepository.findAll();
        assertThat(cabinGroupUserHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        CabinGroupUserHistory testCabinGroupUserHistory = cabinGroupUserHistoryList.get(cabinGroupUserHistoryList.size() - 1);
        assertThat(testCabinGroupUserHistory.getGroupID()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testCabinGroupUserHistory.getUserID()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testCabinGroupUserHistory.getActionDate()).isEqualTo(DEFAULT_ACTION_DATE);
    }

    @Test
    @Transactional
    public void createCabinGroupUserHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cabinGroupUserHistoryRepository.findAll().size();

        // Create the CabinGroupUserHistory with an existing ID
        cabinGroupUserHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCabinGroupUserHistoryMockMvc.perform(post("/api/cabin-group-user-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroupUserHistory)))
            .andExpect(status().isBadRequest());

        // Validate the CabinGroupUserHistory in the database
        List<CabinGroupUserHistory> cabinGroupUserHistoryList = cabinGroupUserHistoryRepository.findAll();
        assertThat(cabinGroupUserHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGroupIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = cabinGroupUserHistoryRepository.findAll().size();
        // set the field null
        cabinGroupUserHistory.setGroupID(null);

        // Create the CabinGroupUserHistory, which fails.

        restCabinGroupUserHistoryMockMvc.perform(post("/api/cabin-group-user-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroupUserHistory)))
            .andExpect(status().isBadRequest());

        List<CabinGroupUserHistory> cabinGroupUserHistoryList = cabinGroupUserHistoryRepository.findAll();
        assertThat(cabinGroupUserHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = cabinGroupUserHistoryRepository.findAll().size();
        // set the field null
        cabinGroupUserHistory.setUserID(null);

        // Create the CabinGroupUserHistory, which fails.

        restCabinGroupUserHistoryMockMvc.perform(post("/api/cabin-group-user-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroupUserHistory)))
            .andExpect(status().isBadRequest());

        List<CabinGroupUserHistory> cabinGroupUserHistoryList = cabinGroupUserHistoryRepository.findAll();
        assertThat(cabinGroupUserHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCabinGroupUserHistories() throws Exception {
        // Initialize the database
        cabinGroupUserHistoryRepository.saveAndFlush(cabinGroupUserHistory);

        // Get all the cabinGroupUserHistoryList
        restCabinGroupUserHistoryMockMvc.perform(get("/api/cabin-group-user-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cabinGroupUserHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupID").value(hasItem(DEFAULT_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(DEFAULT_ACTION_DATE.toString())));
    }

    @Test
    @Transactional
    public void getCabinGroupUserHistory() throws Exception {
        // Initialize the database
        cabinGroupUserHistoryRepository.saveAndFlush(cabinGroupUserHistory);

        // Get the cabinGroupUserHistory
        restCabinGroupUserHistoryMockMvc.perform(get("/api/cabin-group-user-histories/{id}", cabinGroupUserHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cabinGroupUserHistory.getId().intValue()))
            .andExpect(jsonPath("$.groupID").value(DEFAULT_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.userID").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.actionDate").value(DEFAULT_ACTION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCabinGroupUserHistory() throws Exception {
        // Get the cabinGroupUserHistory
        restCabinGroupUserHistoryMockMvc.perform(get("/api/cabin-group-user-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCabinGroupUserHistory() throws Exception {
        // Initialize the database
        cabinGroupUserHistoryRepository.saveAndFlush(cabinGroupUserHistory);
        int databaseSizeBeforeUpdate = cabinGroupUserHistoryRepository.findAll().size();

        // Update the cabinGroupUserHistory
        CabinGroupUserHistory updatedCabinGroupUserHistory = cabinGroupUserHistoryRepository.findOne(cabinGroupUserHistory.getId());
        // Disconnect from session so that the updates on updatedCabinGroupUserHistory are not directly saved in db
        em.detach(updatedCabinGroupUserHistory);
        updatedCabinGroupUserHistory
            .groupID(UPDATED_GROUP_ID)
            .userID(UPDATED_USER_ID)
            .actionDate(UPDATED_ACTION_DATE);

        restCabinGroupUserHistoryMockMvc.perform(put("/api/cabin-group-user-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCabinGroupUserHistory)))
            .andExpect(status().isOk());

        // Validate the CabinGroupUserHistory in the database
        List<CabinGroupUserHistory> cabinGroupUserHistoryList = cabinGroupUserHistoryRepository.findAll();
        assertThat(cabinGroupUserHistoryList).hasSize(databaseSizeBeforeUpdate);
        CabinGroupUserHistory testCabinGroupUserHistory = cabinGroupUserHistoryList.get(cabinGroupUserHistoryList.size() - 1);
        assertThat(testCabinGroupUserHistory.getGroupID()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testCabinGroupUserHistory.getUserID()).isEqualTo(UPDATED_USER_ID);
        assertThat(testCabinGroupUserHistory.getActionDate()).isEqualTo(UPDATED_ACTION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCabinGroupUserHistory() throws Exception {
        int databaseSizeBeforeUpdate = cabinGroupUserHistoryRepository.findAll().size();

        // Create the CabinGroupUserHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCabinGroupUserHistoryMockMvc.perform(put("/api/cabin-group-user-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroupUserHistory)))
            .andExpect(status().isCreated());

        // Validate the CabinGroupUserHistory in the database
        List<CabinGroupUserHistory> cabinGroupUserHistoryList = cabinGroupUserHistoryRepository.findAll();
        assertThat(cabinGroupUserHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCabinGroupUserHistory() throws Exception {
        // Initialize the database
        cabinGroupUserHistoryRepository.saveAndFlush(cabinGroupUserHistory);
        int databaseSizeBeforeDelete = cabinGroupUserHistoryRepository.findAll().size();

        // Get the cabinGroupUserHistory
        restCabinGroupUserHistoryMockMvc.perform(delete("/api/cabin-group-user-histories/{id}", cabinGroupUserHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CabinGroupUserHistory> cabinGroupUserHistoryList = cabinGroupUserHistoryRepository.findAll();
        assertThat(cabinGroupUserHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CabinGroupUserHistory.class);
        CabinGroupUserHistory cabinGroupUserHistory1 = new CabinGroupUserHistory();
        cabinGroupUserHistory1.setId(1L);
        CabinGroupUserHistory cabinGroupUserHistory2 = new CabinGroupUserHistory();
        cabinGroupUserHistory2.setId(cabinGroupUserHistory1.getId());
        assertThat(cabinGroupUserHistory1).isEqualTo(cabinGroupUserHistory2);
        cabinGroupUserHistory2.setId(2L);
        assertThat(cabinGroupUserHistory1).isNotEqualTo(cabinGroupUserHistory2);
        cabinGroupUserHistory1.setId(null);
        assertThat(cabinGroupUserHistory1).isNotEqualTo(cabinGroupUserHistory2);
    }
}
