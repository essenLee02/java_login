package com.example.auth.service;

import com.example.auth.model.Item;
import com.example.auth.repository.ItemRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository repo;

    public ItemService(ItemRepository repo) {
        this.repo = repo;
    }

    public List<Item> listAll() {
        return repo.findAll();
    }

    public Optional<Item> getById(long id) {
        return repo.findById(id);
    }

    /**
     * @return null jika sukses, atau pesan error jika gagal.
     */
    public String create(Item item, String username) {
        String validation = validate(item);
        if (validation != null) return validation;

        // Pre-check unique (code + business unit)
        if (repo.findByCodeAndBusinessUnit(item.getCode(), item.getBusinessUnit()).isPresent()) {
            return "Item code sudah ada untuk Business Unit tersebut.";
        }

        item.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        item.setCreatedBy(username);

        try {
            repo.insert(item);
            return null;
        } catch (DataIntegrityViolationException ex) {
            // Fallback jika constraint DB yang menolak
            return "Gagal simpan: duplikat (code + business unit) atau data tidak valid.";
        }
    }

    /**
     * @return null jika sukses, atau pesan error jika gagal.
     */
    public String update(long id, Item formItem, String username) {
        Item existing = getById(id).orElse(null);
        if (existing == null) return "Data item tidak ditemukan.";

        String validation = validate(formItem);
        if (validation != null) return validation;

        // Pre-check unique (code + business unit), kecuali dirinya sendiri
        boolean duplicate = repo.findByCodeAndBusinessUnit(formItem.getCode(), formItem.getBusinessUnit())
                .filter(x -> x.getId() != id)
                .isPresent();
        if (duplicate) {
            return "Item code sudah ada untuk Business Unit tersebut.";
        }

        existing.setCode(formItem.getCode());
        existing.setDescription(formItem.getDescription());
        existing.setItemType(formItem.getItemType());
        existing.setStock(formItem.getStock());
        existing.setNote(formItem.getNote());
        existing.setBusinessUnit(formItem.getBusinessUnit());
        existing.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        existing.setUpdatedBy(username);

        try {
            repo.update(existing);
            return null;
        } catch (DataIntegrityViolationException ex) {
            return "Gagal update: duplikat (code + business unit) atau data tidak valid.";
        }
    }

    private String validate(Item item) {
        if (item == null) return "Data item kosong.";
        if (isBlank(item.getCode())) return "Code wajib diisi.";
        if (isBlank(item.getDescription())) return "Description wajib diisi.";
        if (isBlank(item.getItemType())) return "Item Type wajib diisi.";
        if (isBlank(item.getBusinessUnit())) return "Business Unit wajib diisi.";
        if (item.getStock() == null) return "Stock wajib diisi.";
        // BigDecimal tidak bisa dibandingkan pakai operator <
        if (item.getStock().compareTo(BigDecimal.ZERO) < 0) return "Stock tidak boleh negatif.";
        return null;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
