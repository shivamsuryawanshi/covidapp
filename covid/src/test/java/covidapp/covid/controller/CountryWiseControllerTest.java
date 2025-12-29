package covidapp.covid.controller;

import covidapp.covid.entity.CountryWiseLatest;
import covidapp.covid.service.CountryWiseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryWiseController.class)
class CountryWiseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryWiseService service;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------------------------------------------
    // 1️⃣ TEST GET ALL COUNTRIES
    // -------------------------------------------------------------
    @Test
    void testGetAllCountries() throws Exception {
        CountryWiseLatest india = new CountryWiseLatest();
        india.setCountry("India");
        india.setConfirmed(100);
        india.setRecovered(90);
        india.setDeaths(10);

        CountryWiseLatest usa = new CountryWiseLatest();
        usa.setCountry("USA");
        usa.setConfirmed(200);
        usa.setRecovered(150);
        usa.setDeaths(50);

        List<CountryWiseLatest> list = Arrays.asList(india, usa);

        Mockito.when(service.getAll()).thenReturn(list);

        mockMvc.perform(get("/api/country/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country").value("India"))
                .andExpect(jsonPath("$[1].country").value("USA"));
    }

    // -------------------------------------------------------------
    // 2️⃣ TEST GET BY COUNTRY
    // -------------------------------------------------------------
    @Test
    void testGetByCountry() throws Exception {
        CountryWiseLatest india = new CountryWiseLatest();
        india.setCountry("India");
        india.setConfirmed(100);
        india.setRecovered(90);
        india.setDeaths(10);

        Mockito.when(service.getByCountry("India")).thenReturn(india);

        mockMvc.perform(get("/api/country/India"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("India"))
                .andExpect(jsonPath("$.confirmed").value(100));
    }

    // -------------------------------------------------------------
    // 3️⃣ TEST ADD NEW COUNTRY
    // -------------------------------------------------------------
    @Test
    void testAddNewCountry() throws Exception {
        CountryWiseLatest newEntry = new CountryWiseLatest();
        newEntry.setCountry("Japan");
        newEntry.setConfirmed(50);
        newEntry.setRecovered(45);
        newEntry.setDeaths(5);

        Mockito.when(service.saveCountry(any(CountryWiseLatest.class)))
                .thenReturn(newEntry);

        mockMvc.perform(post("/api/country/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEntry)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Japan"));
    }

    // -------------------------------------------------------------
    // 4️⃣ TEST UPDATE COUNTRY - SUCCESS
    // -------------------------------------------------------------
    @Test
    void testUpdateCountrySuccess() throws Exception {
        CountryWiseLatest updated = new CountryWiseLatest();
        updated.setCountry("India");
        updated.setConfirmed(120);
        updated.setRecovered(110);
        updated.setDeaths(10);

        Mockito.when(service.updateCountry(eq("India"), any(CountryWiseLatest.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/country/India")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.confirmed").value(120));
    }

    // -------------------------------------------------------------
    // 5️⃣ TEST UPDATE COUNTRY - NOT FOUND
    // -------------------------------------------------------------
    @Test
    void testUpdateCountryNotFound() throws Exception {
        Mockito.when(service.updateCountry(eq("India"), any(CountryWiseLatest.class)))
                .thenReturn(null);

        mockMvc.perform(put("/api/country/India")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"country\":\"India\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Country not found"));
    }
}
