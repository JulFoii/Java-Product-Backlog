package com.example.application.controller;

import com.example.application.data.BacklogItem;
import com.example.application.service.BacklogItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin(origins = "*")
public class BacklogItemController {

    private final BacklogItemService service;

    public BacklogItemController(BacklogItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<BacklogItem> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BacklogItem> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public BacklogItem create(@RequestBody BacklogItem item) {
        return service.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BacklogItem> update(@PathVariable Long id, @RequestBody BacklogItem updatedItem) {
        return service.findById(id)
                .map(existing -> {
                    existing.setTitel(updatedItem.getTitel());
                    existing.setBeschreibung(updatedItem.getBeschreibung());
                    existing.setPrioritaet(updatedItem.getPrioritaet());
                    return ResponseEntity.ok(service.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
