package com.sonbook.song.web.rest;

import com.sonbook.song.Application;
import com.sonbook.song.domain.Chant;
import com.sonbook.song.repository.ChantRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ChantResource REST controller.
 *
 * @see ChantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ChantResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;
    private static final String DEFAULT_LYRIC = "AAAAA";
    private static final String UPDATED_LYRIC = "BBBBB";

    private static final Integer DEFAULT_IMAGE = 1;
    private static final Integer UPDATED_IMAGE = 2;

    @Inject
    private ChantRepository chantRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restChantMockMvc;

    private Chant chant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChantResource chantResource = new ChantResource();
        ReflectionTestUtils.setField(chantResource, "chantRepository", chantRepository);
        this.restChantMockMvc = MockMvcBuilders.standaloneSetup(chantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        chantRepository.deleteAll();
        chant = new Chant();
        chant.setName(DEFAULT_NAME);
        chant.setNumber(DEFAULT_NUMBER);
        chant.setLyric(DEFAULT_LYRIC);
        chant.setImage(DEFAULT_IMAGE);
    }

    @Test
    public void createChant() throws Exception {
        int databaseSizeBeforeCreate = chantRepository.findAll().size();

        // Create the Chant

        restChantMockMvc.perform(post("/api/chants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chant)))
                .andExpect(status().isCreated());

        // Validate the Chant in the database
        List<Chant> chants = chantRepository.findAll();
        assertThat(chants).hasSize(databaseSizeBeforeCreate + 1);
        Chant testChant = chants.get(chants.size() - 1);
        assertThat(testChant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChant.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testChant.getLyric()).isEqualTo(DEFAULT_LYRIC);
        assertThat(testChant.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    public void getAllChants() throws Exception {
        // Initialize the database
        chantRepository.save(chant);

        // Get all the chants
        restChantMockMvc.perform(get("/api/chants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(chant.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
                .andExpect(jsonPath("$.[*].lyric").value(hasItem(DEFAULT_LYRIC.toString())))
                .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)));
    }

    @Test
    public void getChant() throws Exception {
        // Initialize the database
        chantRepository.save(chant);

        // Get the chant
        restChantMockMvc.perform(get("/api/chants/{id}", chant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(chant.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.lyric").value(DEFAULT_LYRIC.toString()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE));
    }

    @Test
    public void getNonExistingChant() throws Exception {
        // Get the chant
        restChantMockMvc.perform(get("/api/chants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateChant() throws Exception {
        // Initialize the database
        chantRepository.save(chant);

		int databaseSizeBeforeUpdate = chantRepository.findAll().size();

        // Update the chant
        chant.setName(UPDATED_NAME);
        chant.setNumber(UPDATED_NUMBER);
        chant.setLyric(UPDATED_LYRIC);
        chant.setImage(UPDATED_IMAGE);

        restChantMockMvc.perform(put("/api/chants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chant)))
                .andExpect(status().isOk());

        // Validate the Chant in the database
        List<Chant> chants = chantRepository.findAll();
        assertThat(chants).hasSize(databaseSizeBeforeUpdate);
        Chant testChant = chants.get(chants.size() - 1);
        assertThat(testChant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChant.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testChant.getLyric()).isEqualTo(UPDATED_LYRIC);
        assertThat(testChant.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    public void deleteChant() throws Exception {
        // Initialize the database
        chantRepository.save(chant);

		int databaseSizeBeforeDelete = chantRepository.findAll().size();

        // Get the chant
        restChantMockMvc.perform(delete("/api/chants/{id}", chant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Chant> chants = chantRepository.findAll();
        assertThat(chants).hasSize(databaseSizeBeforeDelete - 1);
    }
}
