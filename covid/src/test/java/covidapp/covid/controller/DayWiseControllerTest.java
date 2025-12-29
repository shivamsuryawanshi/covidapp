package covidapp.covid.controller;

import covidapp.covid.entity.DayWise;
import covidapp.covid.service.DayWiseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DayWiseController.class)
class DayWiseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DayWiseService service;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------------------------------------------
    // 1️⃣ TEST GET ALL
    // -------------------------------------------------------------
    @Test
    void testGetAll() throws Exception {
        DayWise d1 = new DayWise();
        DayWise d2 = new DayWise();
        List<DayWise> list = Arrays.asList(d1, d2);

        Mockito.when(service.getAll()).thenReturn(list);

        mockMvc.perform(get("/api/daywise"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 2️⃣ TEST GET BY ID
    // -------------------------------------------------------------
    @Test
    void testGetById() throws Exception {
        DayWise sample = new DayWise();

        Mockito.when(service.getById(1L)).thenReturn(sample);

        mockMvc.perform(get("/api/daywise/1"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 3️⃣ TEST GET BY DATE
    // -------------------------------------------------------------
    @Test
    void testGetByDate() throws Exception {
        DayWise sample = new DayWise();

        Mockito.when(service.getByDate(any(LocalDate.class)))
                .thenReturn(sample);

        mockMvc.perform(get("/api/daywise/by-date")
                        .param("date", "2020-05-01"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 4️⃣ TEST CREATE
    // -------------------------------------------------------------
    @Test
    void testCreate() throws Exception {
        DayWise sample = new DayWise();

        Mockito.when(service.create(any(DayWise.class))).thenReturn(sample);

        mockMvc.perform(post("/api/daywise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sample)))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 5️⃣ TEST UPDATE
    // -------------------------------------------------------------
    @Test
    void testUpdate() throws Exception {
        DayWise sample = new DayWise();

        Mockito.when(service.update(eq(1L), any(DayWise.class))).thenReturn(sample);

        mockMvc.perform(put("/api/daywise/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sample)))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------
    // 6️⃣ TEST DELETE
    // -------------------------------------------------------------
    @Test
    void testDelete() throws Exception {

        Mockito.doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/daywise/1"))
                .andExpect(status().isOk());
    }
}
