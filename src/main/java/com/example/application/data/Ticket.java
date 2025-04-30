package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String item;
    private String description;
    private String sprint;
    private int priority;

    // Standard-Konstruktor (wichtig für JPA)
    public Ticket() {
    }

    // Optional: zusätzlicher Konstruktor
    public Ticket(String item, String description, String sprint, int priority) {
        this.item = item;
        this.description = description;
        this.sprint = sprint;
        this.priority = priority;
    }

    // Getter & Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSprint() {
        return sprint;
    }

    public void setSprint(String sprint) {
        this.sprint = sprint;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
