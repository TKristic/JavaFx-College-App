package com.example.tvzprojekt.Model;

public class Institucija {
    private String naziv;
    private String skracenica;
    private String adresa;
    private String webStranica;

    public Institucija(String naziv, String skracenica, String adresa, String webStranica) {
        this.naziv = naziv;
        this.skracenica = skracenica;
        this.adresa = adresa;
        this.webStranica = webStranica;
    }

    @Override
    public String toString() {
        return naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getSkracenica() {
        return skracenica;
    }

    public void setSkracenica(String skracenica) {
        this.skracenica = skracenica;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getWebStranica() {
        return webStranica;
    }

    public void setWebStranica(String webStranica) {
        this.webStranica = webStranica;
    }
}
