package com.mastertek.cabcaller.web.rest;

import com.mastertek.cabcaller.CabcallerApp;

import com.mastertek.cabcaller.domain.Cabin;
import com.mastertek.cabcaller.repository.CabinRepository;
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
 * Test class for the CabinResource REST controller.
 *
 * @see CabinResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CabcallerApp.class)
public class CabinResourceIntTest {

    private static final String DEFAULT_CAB_NO = "AAAAAAAAAA";
    private static final String UPDATED_CAB_NO = "BBBBBBBBBB";

    private static final String DEFAULT_BUTTON_ID = "AAAAAAAAAA";
    private static final String UPDATED_BUTTON_ID = "BBBBBBBBBB";

    @Autowired
    private CabinRepository cabinRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCabinMockMvc;

    private Cabin cabin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CabinResource cabinResource = new CabinResource(cabinRepository);
        this.restCabinMockMvc = MockMvcBuilders.standaloneSetup(cabinResource)
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
    public static Cabin createEntity(EntityManager em) {
        Cabin cabin = new Cabin()
            .cabNo(DEFAULT_CAB_NO)
            .buttonId(DEFAULT_BUTTON_ID);
        return cabin;
    }

    @Before
    public void initTest() {
        cabin = createEntity(em);
    }

    @Test
    @Transactional
    public void createCabin() throws Exception {
        int databaseSizeBeforeCreate = cabinRepository.findAll().size();

        // Create the Cabin
        restCabinMockMvc.perform(post("/api/cabins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabin)))
            .andExpect(status().isCreated());

        // Validate the Cabin in the database
        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeCreate + 1);
        Cabin testCabin = cabinList.get(cabinList.size() - 1);
        assertThat(testCabin.getCabNo()).isEqualTo(DEFAULT_CAB_NO);
        assertThat(testCabin.getButtonId()).isEqualTo(DEFAULT_BUTTON_ID);
    }

    @Test
    @Transactional
    public void createCabinWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cabinRepository.findAll().size();

        // Create the Cabin with an existing ID
        cabin.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCabinMockMvc.perform(post("/api/cabins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabin)))
            .andExpect(status().isBadRequest());

        // Validate the Cabin in the database
        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCabNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cabinRepository.findAll().size();
        // set the field null
        cabin.setCabNo(null);

        // Create the Cabin, which fails.

        restCabinMockMvc.perform(post("/api/cabins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabin)))
            .andExpect(status().isBadRequest());

        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkButtonIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = cabinRepository.findAll().size();
        // set the field null
        cabin.setButtonId(null);

        // Create the Cabin, which fails.

        restCabinMockMvc.perform(post("/api/cabins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabin)))
            .andExpect(status().isBadRequest());

        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCabins() throws Exception {
        // Initialize the database
        cabinRepository.saveAndFlush(cabin);

        // Get all the cabinList
        restCabinMockMvc.perform(get("/api/cabins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cabin.getId().intValue())))
            .andExpect(jsonPath("$.[*].cabNo").value(hasItem(DEFAULT_CAB_NO.toString())))
            .andExpect(jsonPath("$.[*].buttonId").value(hasItem(DEFAULT_BUTTON_ID.toString())));
    }

    @Test
    @Transactional
    public void getCabin() throws Exception {
        // Initialize the database
        cabinRepository.saveAndFlush(cabin);

        // Get the cabin
        restCabinMockMvc.perform(get("/api/cabins/{id}", cabin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cabin.getId().intValue()))
            .andExpect(jsonPath("$.cabNo").value(DEFAULT_CAB_NO.toString()))
            .andExpect(jsonPath("$.buttonId").value(DEFAULT_BUTTON_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCabin() throws Exception {
        // Get the cabin
        restCabinMockMvc.perform(get("/api/cabins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCabin() throws Exception {
        // Initialize the database
        cabinRepository.saveAndFlush(cabin);
        int databaseSizeBeforeUpdate = cabinRepository.findAll().size();

        // Update the cabin
        Cabin updatedCabin = cabinRepository.findOne(cabin.getId());
        // Disconnect from session so that the updates on updatedCabin are not directly saved in db
        em.detach(updatedCabin);
        updatedCabin
            .cabNo(UPDATED_CAB_NO)
            .buttonId(UPDATED_BUTTON_ID);

        restCabinMockMvc.perform(put("/api/cabins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCabin)))
            .andExpect(status().isOk());

        // Validate the Cabin in the database
        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeUpdate);
        Cabin testCabin = cabinList.get(cabinList.size() - 1);
        assertThat(testCabin.getCabNo()).isEqualTo(UPDATED_CAB_NO);
        assertThat(testCabin.getButtonId()).isEqualTo(UPDATED_BUTTON_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingCabin() throws Exception {
        int databaseSizeBeforeUpdate = cabinRepository.findAll().size();

        // Create the Cabin

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCabinMockMvc.perform(put("/api/cabins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabin)))
            .andExpect(status().isCreated());

        // Validate the Cabin in the database
        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCabin() throws Exception {
        // Initialize the database
        cabinRepository.saveAndFlush(cabin);
        int databaseSizeBeforeDelete = cabinRepository.findAll().size();

        // Get the cabin
        restCabinMockMvc.perform(delete("/api/cabins/{id}", cabin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cabin.class);
        Cabin cabin1 = new Cabin();
        cabin1.setId(1L);
        Cabin cabin2 = new Cabin();
        cabin2.setId(cabin1.getId());
        assertThat(cabin1).isEqualTo(cabin2);
        cabin2.setId(2L);
        assertThat(cabin1).isNotEqualTo(cabin2);
        cabin1.setId(null);
        assertThat(cabin1).isNotEqualTo(cabin2);
    }
}
