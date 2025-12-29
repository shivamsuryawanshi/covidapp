package covidapp.covid.controller;
   
import covidapp.covid.entity.CountryWiseLatest;
import covidapp.covid.service.CountryWiseService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/country")
@CrossOrigin("*")
public class CountryWiseController {

    private final CountryWiseService service;

    public CountryWiseController(CountryWiseService service) {

        this.service = service;
    }

    @GetMapping("/all")
    public List<CountryWiseLatest> getAll() {


        return service.getAll();
    }

    @GetMapping("/{country}")
    public CountryWiseLatest getByCountry(@PathVariable String country) {

        return service.getByCountry(country);
    }

    @PostMapping("/add")
    public CountryWiseLatest addNewCountry(@RequestBody CountryWiseLatest data) {
        return service.saveCountry(data);
    }

    // UPDATE COUNTRY DATA
    @PutMapping("/{country}")
    public ResponseEntity<?> updateCountry(
            @PathVariable String country,
            @RequestBody CountryWiseLatest updatedData
    ) {
        CountryWiseLatest updated = service.updateCountry(country, updatedData);

        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Country not found");
        }

        return ResponseEntity.ok(updated);
    }

}

