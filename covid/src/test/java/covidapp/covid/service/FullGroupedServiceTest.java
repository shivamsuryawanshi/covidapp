package covidapp.covid.service;

import covidapp.covid.entity.FullGrouped;
import covidapp.covid.repository.FullGroupedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FullGroupedServiceTest {

    @Mock
    private FullGroupedRepository repo;

    @InjectMocks
    private FullGroupedService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // 1️⃣ TEST getAll()
    // ---------------------------------------------------------
    @Test
    void testGetAll() {
        List<FullGrouped> list =
                Arrays.asList(new FullGrouped(), new FullGrouped());

        when(repo.findAll()).thenReturn(list);

        List<FullGrouped> result = service.getAll();

        assertEquals(2, result.size());
    }

    // ---------------------------------------------------------
    // 2️⃣ TEST getByCountry()
    // ---------------------------------------------------------
    @Test
    void testGetByCountry() {
        List<FullGrouped> list =
                Arrays.asList(new FullGrouped(), new FullGrouped());

        when(repo.findByCountryRegion(eq("India"))).thenReturn(list);

        List<FullGrouped> result = service.getByCountry("India");

        assertEquals(2, result.size());
    }

    // ---------------------------------------------------------
    // 3️⃣ TEST getByDate()
    // ---------------------------------------------------------
    @Test
    void testGetByDate() {
        List<FullGrouped> list =
                Arrays.asList(new FullGrouped(), new FullGrouped());

        LocalDate date = LocalDate.of(2020, 6, 1);

        when(repo.findByDate(eq(date))).thenReturn(list);

        List<FullGrouped> result = service.getByDate(date);

        assertEquals(2, result.size());
    }

    // ---------------------------------------------------------
    // 4️⃣ TEST getByRegion()
    // ---------------------------------------------------------
    @Test
    void testGetByRegion() {
        List<FullGrouped> list =
                Arrays.asList(new FullGrouped(), new FullGrouped(), new FullGrouped());

        when(repo.findByWhoRegion(eq("Europe"))).thenReturn(list);

        List<FullGrouped> result = service.getByRegion("Europe");

        assertEquals(3, result.size());
    }
}
