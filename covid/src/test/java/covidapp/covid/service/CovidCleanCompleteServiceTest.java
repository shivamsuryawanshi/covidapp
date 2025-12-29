package covidapp.covid.service;

import covidapp.covid.entity.CovidCleanComplete;
import covidapp.covid.entity.CovidKey;
import covidapp.covid.repository.CovidCleanCompleteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

class CovidCleanCompleteServiceTest {

    @Mock
    private CovidCleanCompleteRepository repo;

    @InjectMocks
    private CovidCleanCompleteService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // 1️⃣ TEST getAll()
    // ---------------------------------------------------------
    @Test
    void testGetAll() {
        List<CovidCleanComplete> list =
                Arrays.asList(new CovidCleanComplete(), new CovidCleanComplete());

        Mockito.when(repo.findAll()).thenReturn(list);

        List<CovidCleanComplete> result = service.getAll();

        assertEquals(2, result.size());
    }

    // ---------------------------------------------------------
    // 2️⃣ TEST getById() - FOUND
    // ---------------------------------------------------------
    @Test
    void testGetByIdFound() {
        CovidKey key = new CovidKey("Maharashtra", "India", "2020-05-01");
        CovidCleanComplete data = new CovidCleanComplete();

        Mockito.when(repo.findById(eq(key)))
                .thenReturn(Optional.of(data));

        CovidCleanComplete result = service.getById(key);

        assertNotNull(result);
    }

    // ---------------------------------------------------------
    // 3️⃣ TEST getById() - NOT FOUND
    // ---------------------------------------------------------
    @Test
    void testGetByIdNotFound() {
        CovidKey key = new CovidKey("Unknown", "Unknown", "2020-01-01");

        Mockito.when(repo.findById(eq(key)))
                .thenReturn(Optional.empty());

        CovidCleanComplete result = service.getById(key);

        assertNull(result);
    }
}
