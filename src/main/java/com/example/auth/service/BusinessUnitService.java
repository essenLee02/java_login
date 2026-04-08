package com.example.auth.service;

import com.example.auth.model.BusinessUnit;
import com.example.auth.repository.BusinessUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessUnitService {

    private final BusinessUnitRepository repository;

    public BusinessUnitService(BusinessUnitRepository repository) {
        this.repository = repository;
    }

    public long countAll(String search) {
        return repository.countAll(search);
    }

    public List<BusinessUnit> getPage(String search, int page, int size) {
        return repository.findPage(search, page, size);
    }

    public BusinessUnit findById(Long id) {
        return repository.findById(id);
    }

    public void save(BusinessUnit businessUnit) {
        repository.save(businessUnit);
    }

    public void update(Long id, BusinessUnit businessUnit) {
        repository.update(id, businessUnit);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}