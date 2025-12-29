package covidapp.covid.service;

import covidapp.covid.entity.CountryWiseLatest;
import covidapp.covid.repository.CountryWiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class CountryWiseServiceTest {

    @Mock
    private CountryWiseRepository repo;

    @InjectMocks
    private CountryWiseService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // 1️⃣ TEST getAll()
    // ---------------------------------------------------------
    @Test
    void testGetAll() {
        CountryWiseLatest c1 = new CountryWiseLatest();
        c1.setRecovered(100);
        c1.setDeaths(20); // 20/100 = 0.2 -> alert=true

        CountryWiseLatest c2 = new CountryWiseLatest();
        c2.setRecovered(200);
        c2.setDeaths(5); // 5/200=0.025 -> alert=false

        List<CountryWiseLatest> list = Arrays.asList(c1, c2);

        Mockito.when(repo.findAll()).thenReturn(list);

        List<CountryWiseLatest> result = service.getAll();

        assertEquals(2, result.size());
        assertTrue(result.get(0).isRedAlert());
        assertFalse(result.get(1).isRedAlert());
    }

    // ---------------------------------------------------------
    // 2️⃣ TEST getByCountry()
    // ---------------------------------------------------------
    @Test
    void testGetByCountry() {
        CountryWiseLatest data = new CountryWiseLatest();
        data.setRecovered(50);
        data.setDeaths(10); // 10/50 = 0.2 -> alert=true

        Mockito.when(repo.findById("India"))
                .thenReturn(Optional.of(data));

        CountryWiseLatest result = service.getByCountry("India");

        assertNotNull(result);
        assertTrue(result.isRedAlert());
    }

    @Test
    void testGetByCountry_NotFound() {
        Mockito.when(repo.findById("Unknown"))
                .thenReturn(Optional.empty());

        CountryWiseLatest result = service.getByCountry("Unknown");

        assertNull(result);
    }

    // ---------------------------------------------------------
    // 3️⃣ TEST saveCountry()
    // ---------------------------------------------------------
    @Test
    void testSaveCountry() {
        CountryWiseLatest input = new CountryWiseLatest();
        input.setDeaths(15);
        input.setRecovered(100); // 0.15 -> alert=true

        Mockito.when(repo.save(any(CountryWiseLatest.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        CountryWiseLatest result = service.saveCountry(input);

        assertTrue(result.isRedAlert());
        assertNotNull(result);
    }

    // ---------------------------------------------------------
    // 4️⃣ TEST updateCountry() - SUCCESS
    // ---------------------------------------------------------
    @Test
    void testUpdateCountrySuccess() {
        CountryWiseLatest existing = new CountryWiseLatest();
        existing.setRecovered(50);
        existing.setDeaths(5);

        CountryWiseLatest newData = new CountryWiseLatest();
        newData.setDeaths(10);
        newData.setRecovered(100); // final: ratio = 10/100 = 0.1 → NOT > 0.1

        Mockito.when(repo.findById("India"))
                .thenReturn(Optional.of(existing));

        Mockito.when(repo.save(any(CountryWiseLatest.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        CountryWiseLatest updated = service.updateCountry("India", newData);

        assertNotNull(updated);
        assertEquals(100, updated.getRecovered());
        assertEquals(10, updated.getDeaths());

        // ✔ Corrected expectation
        assertFalse(updated.isRedAlert()); // 0.1 is NOT > 0.1
    }

    // ---------------------------------------------------------
    // 5️⃣ TEST updateCountry() - NOT FOUND
    // ---------------------------------------------------------
    @Test
    void testUpdateCountryNotFound() {
        Mockito.when(repo.findById("India"))
                .thenReturn(Optional.empty());

        CountryWiseLatest result =
                service.updateCountry("India", new CountryWiseLatest());

        assertNull(result);
    }
}
