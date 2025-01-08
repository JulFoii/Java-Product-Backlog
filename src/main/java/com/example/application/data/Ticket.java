package com.example.application.data;

public class Ticket {
    private int prioritaet;
    private String item;
    private String beschreibung;
    private String zeitschaetzung;
    private String sprint;

    public Ticket(int prioritaet, String item, String beschreibung, String zeitschaetzung, String sprint) {
        this.prioritaet = prioritaet;
        this.item = item;
        this.beschreibung = beschreibung;
        this.zeitschaetzung = zeitschaetzung;
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

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getZeitschaetzung() {
        return zeitschaetzung;
    }

    public void setZeitschaetzung(String zeitschaetzung) {
        this.zeitschaetzung = zeitschaetzung;
    }
    public String getSprint() {
        return sprint;
    }

    public void setSprint(String sprint) {
        this.sprint = sprint;
    }
}
