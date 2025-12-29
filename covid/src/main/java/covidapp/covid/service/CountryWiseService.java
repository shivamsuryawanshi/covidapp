package covidapp.covid.service;

import covidapp.covid.entity.CountryWiseLatest;
import covidapp.covid.repository.CountryWiseRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.List;

@Service
public class CountryWiseService {

    private final CountryWiseRepository repo;

    public CountryWiseService(CountryWiseRepository repo) {
        this.repo = repo;
    }

    public List<CountryWiseLatest> getAll() {
        List<CountryWiseLatest> list = repo.findAll();

        // 🔥 Set red alert for each country
        list.forEach(country -> {
            boolean alert = country.getRecovered() != 0 &&
                    ((double) country.getDeaths() / country.getRecovered()) > 0.1;
            country.setRedAlert(alert);
        });

        return list;
    }

    public CountryWiseLatest getByCountry(String country) {
        CountryWiseLatest data = repo.findById(country).orElse(null);

        if (data != null) {
            boolean alert = data.getRecovered() != 0 &&
                    ((double) data.getDeaths() / data.getRecovered()) > 0.1;
            data.setRedAlert(alert);
        }

        return data;
    }

    public CountryWiseLatest saveCountry(CountryWiseLatest data) {

        // 🔥 Set redAlert before saving (optional)
        boolean alert = data.getRecovered() != 0 &&
                ((double) data.getDeaths() / data.getRecovered()) > 0.1;
        data.setRedAlert(alert);

        return repo.save(data);
    }
    public CountryWiseLatest updateCountry(String countryName, CountryWiseLatest newData) {

        CountryWiseLatest existing = repo.findById(countryName).orElse(null);

        if (existing == null) {
            return null;
        }

        // Update values only if not null
        if (newData.getConfirmed() != null) existing.setConfirmed(newData.getConfirmed());
        if (newData.getDeaths() != null) existing.setDeaths(newData.getDeaths());
        if (newData.getRecovered() != null) existing.setRecovered(newData.getRecovered());
        if (newData.getActive() != null) existing.setActive(newData.getActive());

        // If recovered is null OR zero → force safe value
        int recovered = existing.getRecovered() == null ? 0 : existing.getRecovered();
        int deaths = existing.getDeaths() == null ? 0 : existing.getDeaths();

        boolean alert = recovered != 0 && ((double) deaths / recovered) > 0.1;
        existing.setRedAlert(alert);

        return repo.save(existing);
    }


}
