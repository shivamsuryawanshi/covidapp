package covidapp.covid.service;

import covidapp.covid.entity.UsaCountryWise;
import covidapp.covid.repository.UsaCountryWiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UsaCountryWiseServiceTest {

    @Mock
    private UsaCountryWiseRepository repo;

    @InjectMocks
    private UsaCountryWiseService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // 1️⃣ TEST getAll()
    // ---------------------------------------------------------
    @Test
    void testGetAll() {
        List<UsaCountryWise> list =
                Arrays.asList(new UsaCountryWise(), new UsaCountryWise());

        when(repo.findAll()).thenReturn(list);

        List<UsaCountryWise> result = service.getAll();

        assertEquals(2, result.size());
    }

    // ---------------------------------------------------------
    // 2️⃣ TEST getByCountry()
    // ---------------------------------------------------------
    @Test
    void testGetByCountry() {
        List<UsaCountryWise> list =
                Arrays.asList(new UsaCountryWise(), new UsaCountryWise());

        when(repo.findByCountryRegion(eq("USA"))).thenReturn(list);

        List<UsaCountryWise> result = service.getByCountry("USA");

        assertEquals(2, result.size());
    }

    // ---------------------------------------------------------
    // 3️⃣ TEST getByProvince()
    // ---------------------------------------------------------
    @Test
    void testGetByProvince() {
        List<UsaCountryWise> list =
                Arrays.asList(new UsaCountryWise(), new UsaCountryWise(), new UsaCountryWise());

        when(repo.findByProvinceState(eq("California"))).thenReturn(list);

        List<UsaCountryWise> result = service.getByProvince("California");

        assertEquals(3, result.size());
    }

    // ---------------------------------------------------------
    // 4️⃣ TEST getByDate()
    // ---------------------------------------------------------
    @Test
    void testGetByDate() {
        List<UsaCountryWise> list =
                Arrays.asList(new UsaCountryWise(), new UsaCountryWise());

        when(repo.findByDate(eq("2020-05-01"))).thenReturn(list);

        List<UsaCountryWise> result = service.getByDate("2020-05-01");

        assertEquals(2, result.size());
    }
}
