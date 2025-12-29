package covidapp.covid.repository;

import covidapp.covid.entity.CountryWiseLatest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class CountryWiseRepositoryTest {

    @Autowired
    private CountryWiseRepository repo;

    // ---------------------------------------------------------
    // TEST #1 : Save & Find By Id
    // ---------------------------------------------------------
    @Test
    void testSaveAndFindById() {
        CountryWiseLatest c = new CountryWiseLatest();
        c.setCountry("India");
        c.setConfirmed(1000);
        c.setDeaths(20);
        c.setRecovered(900);
        c.setActive(80);

        repo.save(c);

        CountryWiseLatest result = repo.findById("India").orElse(null);

        assertNotNull(result);
        assertEquals("India", result.getCountry());
        assertEquals(1000, result.getConfirmed());
        assertEquals(20, result.getDeaths());
    }

    // ---------------------------------------------------------
    // TEST #2 : Find All
    // ---------------------------------------------------------
    @Test
    void testFindAll() {
        CountryWiseLatest c1 = new CountryWiseLatest();
        c1.setCountry("India");
        c1.setConfirmed(500);

        CountryWiseLatest c2 = new CountryWiseLatest();
        c2.setCountry("USA");
        c2.setConfirmed(1000);

        repo.save(c1);
        repo.save(c2);

        List<CountryWiseLatest> result = repo.findAll();

        assertEquals(2, result.size());
    }

    // ---------------------------------------------------------
    // TEST #3 : Update Existing Record
    // ---------------------------------------------------------
    @Test
    void testUpdate() {
        CountryWiseLatest c = new CountryWiseLatest();
        c.setCountry("Japan");
        c.setConfirmed(300);

        repo.save(c);

        // Update
        c.setConfirmed(400);
        repo.save(c);

        CountryWiseLatest updated = repo.findById("Japan").orElse(null);

        assertNotNull(updated);
        assertEquals(400, updated.getConfirmed());
    }

    // ---------------------------------------------------------
    // TEST #4 : Delete Record
    // ---------------------------------------------------------
    @Test
    void testDelete() {
        CountryWiseLatest c = new CountryWiseLatest();
        c.setCountry("Brazil");
        c.setConfirmed(700);

        repo.save(c);

        repo.deleteById("Brazil");

        assertFalse(repo.findById("Brazil").isPresent());
    }
}
