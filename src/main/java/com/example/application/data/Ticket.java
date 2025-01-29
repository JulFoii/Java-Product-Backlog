package com.example.application.data;

public class Ticket {
    private int prioritaet;
    private String item;
    private String description;
    private String sprint;

    public Ticket(int prioritaet, String item, String description, String sprint) {
        this.prioritaet = prioritaet;
        this.item = item;
        this.description = description;

        this.sprint = sprint;
    }

    public int getPrioritaet() {
        return prioritaet;
    }

    public void setPrioritaet(int prioritaet) {
        this.prioritaet = prioritaet;
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

    public String getSprint() {
        return sprint;
    }

    public void setSprint(String sprint) {
        this.sprint = sprint;
    }
}
