package com.example.tvzprojekt.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class IspisPromjene implements Serializable {
    private static final long serialVersionUID = 1L;
    private StatusiPromjene status;
    private String klasa;
    private String username;
    private LocalDateTime vrijemePromjene;

    public StatusiPromjene getStatus() {
        return status;
    }

    public String getKlasa() {
        return klasa;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getVrijemePromjene() {
        return vrijemePromjene;
    }

    public IspisPromjene(StatusiPromjene status, String klasa, String username, LocalDateTime vrijemePromjene) {
        this.status = status;
        this.klasa = klasa;
        this.username = username;
        this.vrijemePromjene = vrijemePromjene;
    }
}
