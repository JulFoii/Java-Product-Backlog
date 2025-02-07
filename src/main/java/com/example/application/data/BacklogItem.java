package com.example.application.data;

public class BacklogItem {
    private int id;
    private int priority;      // Nun als int (statt String), analog zur Ticket-Klasse
    private String item;       // Titel/Name des Backlog-Elements (aus Ticket)
    private String description;// Beschreibung des Elements (aus Ticket)
    private String sprint;     // Sprint-Information (aus Ticket)

    public BacklogItem() {
    }

    // Konstruktor ohne ID (z. B. für neue Elemente)
    public BacklogItem(int priority, String item, String description, String sprint) {
        this.priority = priority;
        this.item = item;
        this.description = description;
        this.sprint = sprint;
    }

    // Konstruktor mit ID (z. B. für Elemente, die bereits in der Datenbank vorhanden sind)
    public BacklogItem(int id, int priority, String item, String description, String sprint) {
        this.id = id;
        this.priority = priority;
        this.item = item;
        this.description = description;
        this.sprint = sprint;
    }

    // Getter und Setter
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
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
}
