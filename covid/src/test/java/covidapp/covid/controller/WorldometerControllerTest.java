package covidapp.covid.controller;

import covidapp.covid.entity.WorldometerData;
import covidapp.covid.service.WorldometerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorldometerController.class)
class WorldometerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorldometerService service;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------------------------------------------
    // 1️⃣ TEST GET ALL
    // -------------------------------------------------------------
    @Test
    void testGetAll() throws Exception {
        List<WorldometerData> list =
                Arrays.asList(new WorldometerData(), new WorldometerData());

        Mockito.when(service.getAll()).thenReturn(list);

        mockMvc.perform(get("/api/worldometer/all"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 2️⃣ TEST GET BY COUNTRY
    // -------------------------------------------------------------
    @Test
    void testGetByCountry() throws Exception {
        WorldometerData sample = new WorldometerData();

        Mockito.when(service.getByCountry(eq("India")))
                .thenReturn(sample);

        mockMvc.perform(get("/api/worldometer/country/India"))
                .andExpect(status().isOk());
    }
}
