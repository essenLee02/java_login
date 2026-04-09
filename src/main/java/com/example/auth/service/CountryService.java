package com.example.auth.service;

import com.example.auth.model.Country;
import com.example.auth.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository repository;

    public CountryService(CountryRepository repository) {
        this.repository = repository;
    }

    public List<Country> findAll() {
        return repository.findAll();
    }

    public List<Country> getPage(String search, int page, int size) {
        return repository.findPage(search, page, size);
    }

    public long countAll(String search) {
        return repository.countAll(search);
    }

    public Country findById(Long id) {
        return repository.findById(id);
    }

    public String validate(Country country, Long excludeId) {
        if (country.getName() == null || country.getName().trim().isEmpty()) {
            return "Name is required";
        }

        if (repository.existsByName(country.getName().trim(), excludeId)) {
            return "Name already exists";
        }

        if (country.getCode() != null && !country.getCode().trim().isEmpty()) {
            if (repository.existsByCode(country.getCode().trim(), excludeId)) {
                return "Code already exists";
            }
        }

        return null;
    }

    public void save(Country country) {
        repository.save(country);
    }

    public void update(Long id, Country country) {
        repository.update(id, country);
    }
}