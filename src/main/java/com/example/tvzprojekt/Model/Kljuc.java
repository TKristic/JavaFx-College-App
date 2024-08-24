package com.example.tvzprojekt.Model;

public class Kljuc {
    private String jmbag;

    @Override
    public String toString() {
        return jmbag;
    }

    public String getJmbag() {
        return jmbag;
    }

    public Kljuc(String jmbag) {
        this.jmbag = jmbag;
    }
}
