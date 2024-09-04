package com.example.tvzprojekt.Model;

import java.io.Serializable;
import java.time.LocalDate;

public class TransakcijaBuilder implements Serializable {

    private int ID;
    private double iznos;
    private String opisPlacanja;
    private LocalDate datumTransakcije;
    private String jmbag;
    private String tipKorisnika;

    public TransakcijaBuilder(int ID, double iznos, String opisPlacanja, LocalDate datumTransakcije, String jmbag, String tipKorisnika) {
        this.ID = ID;
        this.iznos = iznos;
        this.opisPlacanja = opisPlacanja;
        this.datumTransakcije = datumTransakcije;
        this.jmbag = jmbag;
        this.tipKorisnika = tipKorisnika;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public String getOpisPlacanja() {
        return opisPlacanja;
    }

    public void setOpisPlacanja(String opisPlacanja) {
        this.opisPlacanja = opisPlacanja;
    }

    public LocalDate getDatumTransakcije() {
        return datumTransakcije;
    }

    public void setDatumTransakcije(LocalDate datumTransakcije) {
        this.datumTransakcije = datumTransakcije;
    }

    public String getJmbag() {
        return jmbag;
    }

    public void setJmbag(String jmbag) {
        this.jmbag = jmbag;
    }

    public String getTipKorisnika() {
        return tipKorisnika;
    }

    public void setTipKorisnika(String tipKorisnika) {
        this.tipKorisnika = tipKorisnika;
    }
}
