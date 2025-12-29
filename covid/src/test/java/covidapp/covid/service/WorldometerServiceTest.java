package covidapp.covid.service;

import covidapp.covid.entity.WorldometerData;
import covidapp.covid.repository.WorldometerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class WorldometerServiceTest {

    @Mock
    private WorldometerRepository repo;

    @InjectMocks
    private WorldometerService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // 1️⃣ TEST getAll()
    // ---------------------------------------------------------
    @Test
    void testGetAll() {
        List<WorldometerData> list =
                Arrays.asList(new WorldometerData(), new WorldometerData());

        when(repo.findAll()).thenReturn(list);

        List<WorldometerData> result = service.getAll();

        assertEquals(2, result.size());
        verify(repo, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // 2️⃣ TEST getByCountry()
    // ---------------------------------------------------------
    @Test
    void testGetByCountry() {
        WorldometerData data = new WorldometerData();

        when(repo.findByCountryRegion(eq("India"))).thenReturn(data);

        WorldometerData result = service.getByCountry("India");

        assertNotNull(result);
        verify(repo, times(1)).findByCountryRegion("India");
    }

    // ---------------------------------------------------------
    // 3️⃣ TEST getByCountry() - NOT FOUND
    // ---------------------------------------------------------
    @Test
    void testGetByCountryNotFound() {
        when(repo.findByCountryRegion(eq("Unknown"))).thenReturn(null);

        WorldometerData result = service.getByCountry("Unknown");

        assertNull(result);
        verify(repo, times(1)).findByCountryRegion("Unknown");
    }
}
