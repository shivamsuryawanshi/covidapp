//package covidapp.covid.repository;
//
//import covidapp.covid.entity.CovidCleanComplete;
//import covidapp.covid.entity.CovidKey;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class CovidCleanCompleteRepositoryTest {
//
//    @Autowired
//    private CovidCleanCompleteRepository repo;
//
//    // ---------------------------------------------------------
//    // TEST #1 : Save & Find By Id
//    // ---------------------------------------------------------
//    @Test
//    void testSaveAndFindById() {
//        CovidKey key = new CovidKey("Maharashtra", "India", "2020-05-01");
//
//        CovidCleanComplete rec = new CovidCleanComplete();
//        rec.setId(key);
//        rec.setLat(19.7515);
//        rec.setLon(75.7139);
//        rec.setConfirmed(1000);
//        rec.setDeaths(20);
//        rec.setRecovered(950);
//        rec.setActive(30);
//        rec.setWhoRegion("SEARO");
//
//        repo.save(rec);
//
//        CovidCleanComplete result = repo.findById(key).orElse(null);
//
//        assertNotNull(result);
//        assertEquals(1000, result.getConfirmed());
//        assertEquals(30, result.getActive());
//        assertEquals("SEARO", result.getWhoRegion());
//    }
//
//    // ---------------------------------------------------------
//    // TEST #2 : Find All
//    // ---------------------------------------------------------
//    @Test
//    void testFindAll() {
//        CovidCleanComplete rec1 = new CovidCleanComplete();
//        rec1.setId(new CovidKey("Delhi", "India", "2020-04-01"));
//        rec1.setConfirmed(500);
//
//        CovidCleanComplete rec2 = new CovidCleanComplete();
//        rec2.setId(new CovidKey("Texas", "USA", "2020-04-01"));
//        rec2.setConfirmed(1200);
//
//        repo.save(rec1);
//        repo.save(rec2);
//
//        List<CovidCleanComplete> all = repo.findAll();
//        assertEquals(2, all.size());
//    }
//
//    // ---------------------------------------------------------
//    // TEST #3 : Update Existing Record
//    // ---------------------------------------------------------
//    @Test
//    void testUpdate() {
//        CovidKey key = new CovidKey("Tokyo", "Japan", "2020-05-15");
//
//        CovidCleanComplete rec = new CovidCleanComplete();
//        rec.setId(key);
//        rec.setConfirmed(300);
//        rec.setActive(100);
//
//        repo.save(rec);
//
//        // Update
//        rec.setConfirmed(450);
//        repo.save(rec);
//
//        CovidCleanComplete updated = repo.findById(key).orElse(null);
//
//        assertNotNull(updated);
//        assertEquals(450, updated.getConfirmed());
//    }
//
//    // ---------------------------------------------------------
//    // TEST #4 : Delete Record
//    // ---------------------------------------------------------
//    @Test
//    void testDelete() {
//        CovidKey key = new CovidKey("Rio", "Brazil", "2020-03-30");
//
//        CovidCleanComplete rec = new CovidCleanComplete();
//        rec.setId(key);
//        rec.setConfirmed(700);
//
//        repo.save(rec);
//
//        repo.deleteById(key);
//
//        assertFalse(repo.findById(key).isPresent());
//    }
//}
