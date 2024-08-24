package com.example.tvzprojekt.Model;

import java.time.LocalDate;
import java.util.Date;

public class Entitet {

    private String jmbag;
    private String ime;
    private String prezime;
    private LocalDate datumRodenja;
    private String smjer;
    private String tip;

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Entitet() {

    }

    public Entitet(String jmbag, String ime, String prezime, LocalDate datumRodenja, String smjer) {
        this.jmbag = jmbag;
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodenja = datumRodenja;
        this.smjer = smjer;
    }

    public String getJmbag() {
        return jmbag;
    }

    public void setJmbag(String jmbag) {
        this.jmbag = jmbag;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public LocalDate getDatumRodenja() {
        return datumRodenja;
    }

    public void setDatumRodenja(LocalDate datumRodenja) {
        this.datumRodenja = datumRodenja;
    }

    public String getSmjer() {
        return smjer;
    }

    public void setSmjer(String smjer) {
        this.smjer = smjer;
    }

    @Override
    public String toString() {
        return jmbag;
    }
}
