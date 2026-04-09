package com.example.auth.service;

import com.example.auth.model.City;
import com.example.auth.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    private final CityRepository repository;

    public CityService(CityRepository repository) {
        this.repository = repository;
    }

    public List<City> getPage(String search, int page, int size) {
        return repository.findPage(search, page, size);
    }

    public long countAll(String search) {
        return repository.countAll(search);
    }

    public City findById(Long id) {
        return repository.findById(id);
    }

    public String validate(City city, Long excludeId) {
        if (city.getIdCountry() == null || city.getIdCountry().trim().isEmpty()) {
            return "Country is required";
        }

        if (city.getIdProvince() == null || city.getIdProvince().trim().isEmpty()) {
            return "Province is required";
        }

        if (city.getName() == null || city.getName().trim().isEmpty()) {
            return "Name is required";
        }

        if (repository.existsByName(city.getName().trim(), excludeId)) {
            return "Name already exists";
        }

        if (city.getCode() != null && !city.getCode().trim().isEmpty()) {
            if (repository.existsByCode(city.getCode().trim(), excludeId)) {
                return "Code already exists";
            }
        }

        return null;
    }

    public void save(City city) {
        repository.save(city);
    }

    public void update(Long id, City city) {
        repository.update(id, city);
    }
}