package com.example.auth.service;

import com.example.auth.model.Province;
import com.example.auth.repository.ProvinceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {

    private final ProvinceRepository repository;

    public ProvinceService(ProvinceRepository repository) {
        this.repository = repository;
    }

    public List<Province> findAll() {
        return repository.findAll();
    }

    public List<Province> getPage(String search, int page, int size) {
        return repository.findPage(search, page, size);
    }

    public long countAll(String search) {
        return repository.countAll(search);
    }

    public Province findById(Long id) {
        return repository.findById(id);
    }

    public String validate(Province province, Long excludeId) {
        if (province.getIdCountry() == null || province.getIdCountry().trim().isEmpty()) {
            return "Country is required";
        }

        if (province.getName() == null || province.getName().trim().isEmpty()) {
            return "Name is required";
        }

        if (repository.existsByName(province.getName().trim(), excludeId)) {
            return "Name already exists";
        }

        if (province.getCode() != null && !province.getCode().trim().isEmpty()) {
            if (repository.existsByCode(province.getCode().trim(), excludeId)) {
                return "Code already exists";
            }
        }

        return null;
    }

    public void save(Province province) {
        repository.save(province);
    }

    public void update(Long id, Province province) {
        repository.update(id, province);
    }
}