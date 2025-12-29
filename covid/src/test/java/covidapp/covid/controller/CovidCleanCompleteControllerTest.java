package covidapp.covid.controller;

import covidapp.covid.entity.CovidCleanComplete;
import covidapp.covid.entity.CovidKey;
import covidapp.covid.service.CovidCleanCompleteService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CovidCleanCompleteController.class)
class CovidCleanCompleteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CovidCleanCompleteService service;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------------------------------------------
    // 1️⃣ TEST GET ALL DATA  (NO SETTERS NEEDED)
    // -------------------------------------------------------------
    @Test
    void testGetAllData() throws Exception {

        CovidCleanComplete sample1 = new CovidCleanComplete();
        CovidCleanComplete sample2 = new CovidCleanComplete();

        List<CovidCleanComplete> list = Arrays.asList(sample1, sample2);

        Mockito.when(service.getAll()).thenReturn(list);

        mockMvc.perform(get("/api/clean/all"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 2️⃣ TEST GET BY COMPOSITE KEY (NO SETTERS NEEDED)
    // -------------------------------------------------------------
    @Test
    void testGetById() throws Exception {

        CovidCleanComplete sample = new CovidCleanComplete();

        Mockito.when(service.getById(any(CovidKey.class)))
                .thenReturn(sample);

        mockMvc.perform(get("/api/clean/Maharashtra/India/2020-05-01"))
                .andExpect(status().isOk());
    }
}
