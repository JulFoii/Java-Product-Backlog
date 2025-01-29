package com.example.application.data;

public class Ticket {
    private int priority;
    private String item;
    private String description;
    private String sprint;

    public Ticket(int prioritaet, String item, String description, String sprint) {
        this.prioritaet = prioritaet;
        this.item = item;
        this.description = description;

    private String storyPoints;
    private String sprint;

    public Ticket(int priority, String item, String description, String storyPoints, String sprint) {
        this.priority = priority;
        this.item = item;
        this.description = description;
        this.storyPoints = storyPoints;
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

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(String storyPoints) {
        this.storyPoints = storyPoints;
    }

    public String getSprint() {
        return sprint;
    }

    public void setSprint(String sprint) {
        this.sprint = sprint;
    }
}
