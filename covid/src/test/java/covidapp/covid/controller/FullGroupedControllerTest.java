package covidapp.covid.controller;

import covidapp.covid.entity.FullGrouped;
import covidapp.covid.service.FullGroupedService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FullGroupedController.class)
class FullGroupedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FullGroupedService service;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------------------------------------------
    // 1️⃣ TEST GET ALL
    // -------------------------------------------------------------
    @Test
    void testGetAll() throws Exception {
        FullGrouped f1 = new FullGrouped();
        FullGrouped f2 = new FullGrouped();

        List<FullGrouped> list = Arrays.asList(f1, f2);

        Mockito.when(service.getAll()).thenReturn(list);

        mockMvc.perform(get("/api/fullgrouped/all"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 2️⃣ TEST GET BY COUNTRY
    // -------------------------------------------------------------
    @Test
    void testGetByCountry() throws Exception {
        List<FullGrouped> result = List.of(new FullGrouped());

        Mockito.when(service.getByCountry("India")).thenReturn(result);

        mockMvc.perform(get("/api/fullgrouped/country/India"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 3️⃣ TEST GET BY DATE
    // -------------------------------------------------------------
    @Test
    void testGetByDate() throws Exception {
        List<FullGrouped> result = List.of(new FullGrouped());

        Mockito.when(service.getByDate(any(LocalDate.class))).thenReturn(result);

        mockMvc.perform(get("/api/fullgrouped/date/2020-05-01"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 4️⃣ TEST GET BY REGION
    // -------------------------------------------------------------
    @Test
    void testGetByRegion() throws Exception {
        List<FullGrouped> result = List.of(new FullGrouped());

        Mockito.when(service.getByRegion("Asia")).thenReturn(result);

        mockMvc.perform(get("/api/fullgrouped/region/Asia"))
                .andExpect(status().isOk());
    }
}
