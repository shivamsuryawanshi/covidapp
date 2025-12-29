package covidapp.covid.controller;

import covidapp.covid.entity.UsaCountryWise;
import covidapp.covid.service.UsaCountryWiseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsaCountryWiseController.class)
class UsaCountryWiseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsaCountryWiseService service;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------------------------------------------
    // 1️⃣ TEST GET ALL
    // -------------------------------------------------------------
    @Test
    void testGetAll() throws Exception {
        List<UsaCountryWise> list = Arrays.asList(new UsaCountryWise(), new UsaCountryWise());

        Mockito.when(service.getAll()).thenReturn(list);

        mockMvc.perform(get("/api/usa/all"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 2️⃣ TEST GET BY COUNTRY
    // -------------------------------------------------------------
    @Test
    void testGetByCountry() throws Exception {
        List<UsaCountryWise> list = List.of(new UsaCountryWise());

        Mockito.when(service.getByCountry("Texas")).thenReturn(list);

        mockMvc.perform(get("/api/usa/country/Texas"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 3️⃣ TEST GET BY PROVINCE
    // -------------------------------------------------------------
    @Test
    void testGetByProvince() throws Exception {
        List<UsaCountryWise> list = List.of(new UsaCountryWise());

        Mockito.when(service.getByProvince("California")).thenReturn(list);

        mockMvc.perform(get("/api/usa/province/California"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 4️⃣ TEST GET BY DATE (Request Param)
    // -------------------------------------------------------------
    @Test
    void testGetByDate() throws Exception {
        List<UsaCountryWise> list = List.of(new UsaCountryWise());

        Mockito.when(service.getByDate(anyString())).thenReturn(list);

        mockMvc.perform(get("/api/usa/date")
                        .param("date", "2020-05-01"))
                .andExpect(status().isOk());
    }
}
