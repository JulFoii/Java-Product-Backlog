package com.example.application.repository;


import com.example.application.data.BacklogItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BacklogItemRepository extends JpaRepository<BacklogItem, Long> {
}
