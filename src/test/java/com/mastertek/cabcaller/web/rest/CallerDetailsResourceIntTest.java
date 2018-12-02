package com.mastertek.cabcaller.web.rest;

import com.mastertek.cabcaller.CabcallerApp;

import com.mastertek.cabcaller.domain.CallerDetails;
import com.mastertek.cabcaller.repository.CallerDetailsRepository;
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
 * Test class for the CallerDetailsResource REST controller.
 *
 * @see CallerDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CabcallerApp.class)
public class CallerDetailsResourceIntTest {

    private static final LocalDate DEFAULT_CALL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CALL_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CallerDetailsRepository callerDetailsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCallerDetailsMockMvc;

    private CallerDetails callerDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CallerDetailsResource callerDetailsResource = new CallerDetailsResource(callerDetailsRepository);
        this.restCallerDetailsMockMvc = MockMvcBuilders.standaloneSetup(callerDetailsResource)
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
    public static CallerDetails createEntity(EntityManager em) {
        CallerDetails callerDetails = new CallerDetails()
            .callDate(DEFAULT_CALL_DATE);
        return callerDetails;
    }

    @Before
    public void initTest() {
        callerDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createCallerDetails() throws Exception {
        int databaseSizeBeforeCreate = callerDetailsRepository.findAll().size();

        // Create the CallerDetails
        restCallerDetailsMockMvc.perform(post("/api/caller-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(callerDetails)))
            .andExpect(status().isCreated());

        // Validate the CallerDetails in the database
        List<CallerDetails> callerDetailsList = callerDetailsRepository.findAll();
        assertThat(callerDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        CallerDetails testCallerDetails = callerDetailsList.get(callerDetailsList.size() - 1);
        assertThat(testCallerDetails.getCallDate()).isEqualTo(DEFAULT_CALL_DATE);
    }

    @Test
    @Transactional
    public void createCallerDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = callerDetailsRepository.findAll().size();

        // Create the CallerDetails with an existing ID
        callerDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCallerDetailsMockMvc.perform(post("/api/caller-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(callerDetails)))
            .andExpect(status().isBadRequest());

        // Validate the CallerDetails in the database
        List<CallerDetails> callerDetailsList = callerDetailsRepository.findAll();
        assertThat(callerDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCallerDetails() throws Exception {
        // Initialize the database
        callerDetailsRepository.saveAndFlush(callerDetails);

        // Get all the callerDetailsList
        restCallerDetailsMockMvc.perform(get("/api/caller-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(callerDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].callDate").value(hasItem(DEFAULT_CALL_DATE.toString())));
    }

    @Test
    @Transactional
    public void getCallerDetails() throws Exception {
        // Initialize the database
        callerDetailsRepository.saveAndFlush(callerDetails);

        // Get the callerDetails
        restCallerDetailsMockMvc.perform(get("/api/caller-details/{id}", callerDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(callerDetails.getId().intValue()))
            .andExpect(jsonPath("$.callDate").value(DEFAULT_CALL_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCallerDetails() throws Exception {
        // Get the callerDetails
        restCallerDetailsMockMvc.perform(get("/api/caller-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCallerDetails() throws Exception {
        // Initialize the database
        callerDetailsRepository.saveAndFlush(callerDetails);
        int databaseSizeBeforeUpdate = callerDetailsRepository.findAll().size();

        // Update the callerDetails
        CallerDetails updatedCallerDetails = callerDetailsRepository.findOne(callerDetails.getId());
        // Disconnect from session so that the updates on updatedCallerDetails are not directly saved in db
        em.detach(updatedCallerDetails);
        updatedCallerDetails
            .callDate(UPDATED_CALL_DATE);

        restCallerDetailsMockMvc.perform(put("/api/caller-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCallerDetails)))
            .andExpect(status().isOk());

        // Validate the CallerDetails in the database
        List<CallerDetails> callerDetailsList = callerDetailsRepository.findAll();
        assertThat(callerDetailsList).hasSize(databaseSizeBeforeUpdate);
        CallerDetails testCallerDetails = callerDetailsList.get(callerDetailsList.size() - 1);
        assertThat(testCallerDetails.getCallDate()).isEqualTo(UPDATED_CALL_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCallerDetails() throws Exception {
        int databaseSizeBeforeUpdate = callerDetailsRepository.findAll().size();

        // Create the CallerDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCallerDetailsMockMvc.perform(put("/api/caller-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(callerDetails)))
            .andExpect(status().isCreated());

        // Validate the CallerDetails in the database
        List<CallerDetails> callerDetailsList = callerDetailsRepository.findAll();
        assertThat(callerDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCallerDetails() throws Exception {
        // Initialize the database
        callerDetailsRepository.saveAndFlush(callerDetails);
        int databaseSizeBeforeDelete = callerDetailsRepository.findAll().size();

        // Get the callerDetails
        restCallerDetailsMockMvc.perform(delete("/api/caller-details/{id}", callerDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CallerDetails> callerDetailsList = callerDetailsRepository.findAll();
        assertThat(callerDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CallerDetails.class);
        CallerDetails callerDetails1 = new CallerDetails();
        callerDetails1.setId(1L);
        CallerDetails callerDetails2 = new CallerDetails();
        callerDetails2.setId(callerDetails1.getId());
        assertThat(callerDetails1).isEqualTo(callerDetails2);
        callerDetails2.setId(2L);
        assertThat(callerDetails1).isNotEqualTo(callerDetails2);
        callerDetails1.setId(null);
        assertThat(callerDetails1).isNotEqualTo(callerDetails2);
    }
}
