package com.example.auth.service;

import com.example.auth.model.Item;
import com.example.auth.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository repo;

    public ItemService(ItemRepository repo) {
        this.repo = repo;
    }

    public List<Item> getPage(int page, int size) {
        return repo.findPage(page, size);
    }

    public long countAll() {
        return repo.countAll();
    }

    public void save(Item item) {
        repo.save(item);
    }

    public void update(Long id, Item item) {
        repo.update(id, item);
    }

    public void delete(Long id) {
        repo.delete(id);
    }

    public Item findById(Long id) {
        return repo.findById(id);
    }
}
