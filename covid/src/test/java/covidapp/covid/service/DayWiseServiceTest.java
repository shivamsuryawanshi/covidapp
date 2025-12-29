package covidapp.covid.service;

import covidapp.covid.entity.DayWise;
import covidapp.covid.repository.DayWiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class DayWiseServiceTest {

    @Mock
    private DayWiseRepository repository;

    @InjectMocks
    private DayWiseService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // 1️⃣ TEST getAll()
    // ---------------------------------------------------------
    @Test
    void testGetAll() {
        List<DayWise> list = Arrays.asList(new DayWise(), new DayWise());

        Mockito.when(repository.findAll()).thenReturn(list);

        List<DayWise> result = service.getAll();

        assertEquals(2, result.size());
    }

    // ---------------------------------------------------------
    // 2️⃣ TEST getById() - SUCCESS
    // ---------------------------------------------------------
    @Test
    void testGetByIdFound() {
        DayWise sample = new DayWise();

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(sample));

        DayWise result = service.getById(1L);

        assertNotNull(result);
    }

    // ---------------------------------------------------------
    // 3️⃣ TEST getById() - NOT FOUND (throws exception)
    // ---------------------------------------------------------
    @Test
    void testGetByIdNotFound() {
        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getById(1L));

        assertEquals("Record not found", ex.getMessage());
    }

    // ---------------------------------------------------------
    // 4️⃣ TEST getByDate()
    // ---------------------------------------------------------
    @Test
    void testGetByDate() {
        DayWise sample = new DayWise();
        LocalDate date = LocalDate.of(2020, 5, 1);

        Mockito.when(repository.findByDate(date))
                .thenReturn(Optional.of(sample));

        DayWise result = service.getByDate(date);

        assertNotNull(result);
    }

    @Test
    void testGetByDateNotFound() {
        LocalDate date = LocalDate.of(2020, 5, 1);

        Mockito.when(repository.findByDate(date))
                .thenReturn(Optional.empty());

        DayWise result = service.getByDate(date);

        assertNull(result);
    }

    // ---------------------------------------------------------
    // 5️⃣ TEST create()
    // ---------------------------------------------------------
    @Test
    void testCreate() {
        DayWise input = new DayWise();

        Mockito.when(repository.save(any(DayWise.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        DayWise result = service.create(input);

        assertNull(result.getId()); // ID must be set to null before save
        assertNotNull(result);
    }

    // ---------------------------------------------------------
    // 6️⃣ TEST update() - SUCCESS
    // ---------------------------------------------------------
    @Test
    void testUpdateSuccess() {
        DayWise existing = new DayWise();
        existing.setId(1L);

        DayWise newData = new DayWise();
        newData.setDate(LocalDate.of(2020, 5, 1));
        newData.setConfirmed(100);
        newData.setDeaths(10);
        newData.setRecovered(20);
        newData.setActive(70);
        newData.setNewCases(5);
        newData.setNewDeaths(1);
        newData.setNewRecovered(2);
        newData.setDeathsPer100Cases(3.5);
        newData.setRecoveredPer100Cases(20.0);
        newData.setDeathsPer100Recovered(6.0);
        newData.setNumberOfCountries(50);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existing));

        Mockito.when(repository.save(any(DayWise.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        DayWise result = service.update(1L, newData);

        assertEquals(newData.getDate(), result.getDate());
        assertEquals(newData.getConfirmed(), result.getConfirmed());
        assertEquals(newData.getDeaths(), result.getDeaths());
        assertEquals(newData.getRecovered(), result.getRecovered());
        assertEquals(newData.getActive(), result.getActive());
        assertEquals(newData.getNewCases(), result.getNewCases());
        assertEquals(newData.getNewDeaths(), result.getNewDeaths());
        assertEquals(newData.getNewRecovered(), result.getNewRecovered());
        assertEquals(newData.getDeathsPer100Cases(), result.getDeathsPer100Cases());
        assertEquals(newData.getRecoveredPer100Cases(), result.getRecoveredPer100Cases());
        assertEquals(newData.getDeathsPer100Recovered(), result.getDeathsPer100Recovered());
        assertEquals(newData.getNumberOfCountries(), result.getNumberOfCountries());
    }

    // ---------------------------------------------------------
    // 7️⃣ TEST update() - NOT FOUND
    // ---------------------------------------------------------
    @Test
    void testUpdateNotFound() {
        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.empty());

        DayWise input = new DayWise();

        assertThrows(RuntimeException.class, () -> service.update(1L, input));
    }

    // ---------------------------------------------------------
    // 8️⃣ TEST delete()
    // ---------------------------------------------------------
    @Test
    void testDelete() {
        Mockito.doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> service.delete(1L));
    }
}
