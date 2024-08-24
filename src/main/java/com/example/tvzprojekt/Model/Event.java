package com.example.tvzprojekt.Model;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {

    private int ID;
    private String nazivEventa;
    private int posjecenost;
    private double cijenaKarte;
    private Date datumEventa;

    public Event(int ID, String nazivEventa, int posjecenost, double cijenaKarte, Date datumEventa) {
        this.ID = ID;
        this.nazivEventa = nazivEventa;
        this.posjecenost = posjecenost;
        this.cijenaKarte = cijenaKarte;
        this.datumEventa = datumEventa;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNazivEventa() {
        return nazivEventa;
    }

    public void setNazivEventa(String nazivEventa) {
        this.nazivEventa = nazivEventa;
    }

    public int getPosjecenost() {
        return posjecenost;
    }

    public void setPosjecenost(int posjecenost) {
        this.posjecenost = posjecenost;
    }

    public double getCijenaKarte() {
        return cijenaKarte;
    }

    public void setCijenaKarte(double cijenaKarte) {
        this.cijenaKarte = cijenaKarte;
    }

    public Date getDatumEventa() {
        return datumEventa;
    }

    public void setDatumEventa(Date datumEventa) {
        this.datumEventa = datumEventa;
    }
}
