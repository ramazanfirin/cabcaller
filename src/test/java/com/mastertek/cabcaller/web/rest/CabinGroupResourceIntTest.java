package com.mastertek.cabcaller.web.rest;

import com.mastertek.cabcaller.CabcallerApp;

import com.mastertek.cabcaller.domain.CabinGroup;
import com.mastertek.cabcaller.repository.CabinGroupRepository;
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
 * Test class for the CabinGroupResource REST controller.
 *
 * @see CabinGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CabcallerApp.class)
public class CabinGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CabinGroupRepository cabinGroupRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCabinGroupMockMvc;

    private CabinGroup cabinGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CabinGroupResource cabinGroupResource = new CabinGroupResource(cabinGroupRepository);
        this.restCabinGroupMockMvc = MockMvcBuilders.standaloneSetup(cabinGroupResource)
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
    public static CabinGroup createEntity(EntityManager em) {
        CabinGroup cabinGroup = new CabinGroup()
            .name(DEFAULT_NAME);
        return cabinGroup;
    }

    @Before
    public void initTest() {
        cabinGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createCabinGroup() throws Exception {
        int databaseSizeBeforeCreate = cabinGroupRepository.findAll().size();

        // Create the CabinGroup
        restCabinGroupMockMvc.perform(post("/api/cabin-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroup)))
            .andExpect(status().isCreated());

        // Validate the CabinGroup in the database
        List<CabinGroup> cabinGroupList = cabinGroupRepository.findAll();
        assertThat(cabinGroupList).hasSize(databaseSizeBeforeCreate + 1);
        CabinGroup testCabinGroup = cabinGroupList.get(cabinGroupList.size() - 1);
        assertThat(testCabinGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCabinGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cabinGroupRepository.findAll().size();

        // Create the CabinGroup with an existing ID
        cabinGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCabinGroupMockMvc.perform(post("/api/cabin-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroup)))
            .andExpect(status().isBadRequest());

        // Validate the CabinGroup in the database
        List<CabinGroup> cabinGroupList = cabinGroupRepository.findAll();
        assertThat(cabinGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cabinGroupRepository.findAll().size();
        // set the field null
        cabinGroup.setName(null);

        // Create the CabinGroup, which fails.

        restCabinGroupMockMvc.perform(post("/api/cabin-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroup)))
            .andExpect(status().isBadRequest());

        List<CabinGroup> cabinGroupList = cabinGroupRepository.findAll();
        assertThat(cabinGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCabinGroups() throws Exception {
        // Initialize the database
        cabinGroupRepository.saveAndFlush(cabinGroup);

        // Get all the cabinGroupList
        restCabinGroupMockMvc.perform(get("/api/cabin-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cabinGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCabinGroup() throws Exception {
        // Initialize the database
        cabinGroupRepository.saveAndFlush(cabinGroup);

        // Get the cabinGroup
        restCabinGroupMockMvc.perform(get("/api/cabin-groups/{id}", cabinGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cabinGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCabinGroup() throws Exception {
        // Get the cabinGroup
        restCabinGroupMockMvc.perform(get("/api/cabin-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCabinGroup() throws Exception {
        // Initialize the database
        cabinGroupRepository.saveAndFlush(cabinGroup);
        int databaseSizeBeforeUpdate = cabinGroupRepository.findAll().size();

        // Update the cabinGroup
        CabinGroup updatedCabinGroup = cabinGroupRepository.findOne(cabinGroup.getId());
        // Disconnect from session so that the updates on updatedCabinGroup are not directly saved in db
        em.detach(updatedCabinGroup);
        updatedCabinGroup
            .name(UPDATED_NAME);

        restCabinGroupMockMvc.perform(put("/api/cabin-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCabinGroup)))
            .andExpect(status().isOk());

        // Validate the CabinGroup in the database
        List<CabinGroup> cabinGroupList = cabinGroupRepository.findAll();
        assertThat(cabinGroupList).hasSize(databaseSizeBeforeUpdate);
        CabinGroup testCabinGroup = cabinGroupList.get(cabinGroupList.size() - 1);
        assertThat(testCabinGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCabinGroup() throws Exception {
        int databaseSizeBeforeUpdate = cabinGroupRepository.findAll().size();

        // Create the CabinGroup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCabinGroupMockMvc.perform(put("/api/cabin-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinGroup)))
            .andExpect(status().isCreated());

        // Validate the CabinGroup in the database
        List<CabinGroup> cabinGroupList = cabinGroupRepository.findAll();
        assertThat(cabinGroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCabinGroup() throws Exception {
        // Initialize the database
        cabinGroupRepository.saveAndFlush(cabinGroup);
        int databaseSizeBeforeDelete = cabinGroupRepository.findAll().size();

        // Get the cabinGroup
        restCabinGroupMockMvc.perform(delete("/api/cabin-groups/{id}", cabinGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CabinGroup> cabinGroupList = cabinGroupRepository.findAll();
        assertThat(cabinGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CabinGroup.class);
        CabinGroup cabinGroup1 = new CabinGroup();
        cabinGroup1.setId(1L);
        CabinGroup cabinGroup2 = new CabinGroup();
        cabinGroup2.setId(cabinGroup1.getId());
        assertThat(cabinGroup1).isEqualTo(cabinGroup2);
        cabinGroup2.setId(2L);
        assertThat(cabinGroup1).isNotEqualTo(cabinGroup2);
        cabinGroup1.setId(null);
        assertThat(cabinGroup1).isNotEqualTo(cabinGroup2);
    }
}
