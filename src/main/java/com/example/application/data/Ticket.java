package com.example.application.data;

public class Ticket {
    private int priority;
    private String item;
    private String description;
    private String sprint;

    public Ticket(int priority, String item, String description, String sprint) {
        this.priority = priority;
        this.item = item;
        this.description = description;
        this.sprint = sprint;
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
