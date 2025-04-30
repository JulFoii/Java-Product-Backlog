package com.example.application.service;


import com.example.application.data.BacklogItem;
import com.example.application.repository.BacklogItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BacklogItemService {

    private final BacklogItemRepository repository;

    public BacklogItemService(BacklogItemRepository repository) {
        this.repository = repository;
    }

    public List<BacklogItem> findAll() {
        return repository.findAll();
    }

    public Optional<BacklogItem> findById(Long id) {
        return repository.findById(id);
    }

    public BacklogItem save(BacklogItem item) {
        return repository.save(item);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
