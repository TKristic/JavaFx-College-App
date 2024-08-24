package com.example.tvzprojekt.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Promjena<T> implements Serializable {

    private T objekt;
    private StatusiPromjene status;
    private String username;
    private LocalDateTime vrijemePromjene;

    public Promjena(T objekt, StatusiPromjene status, String username, LocalDateTime vrijemePromjene) {
        this.objekt = objekt;
        this.status = status;
        this.username = username;
        this.vrijemePromjene = vrijemePromjene;
    }

    public String getObjekt() {
        return objekt.toString();
    }

    public StatusiPromjene getStatus() {
        return status;
    }

    public void setStatus(StatusiPromjene status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getVrijemePromjene() {
        return vrijemePromjene;
    }


    public void setVrijemePromjene(LocalDateTime vrijemePromjene) {
        this.vrijemePromjene = vrijemePromjene;
    }
}
