package com.example.tvzprojekt.Model;

public class Valuta {
    public int brojTecajnice;
    public String datumPrimjene;
    public String drzava;

    @Override
    public String toString() {
        return "Valuta{" +
                "brojTecajnice=" + brojTecajnice +
                ", datumPrimjene='" + datumPrimjene + '\'' +
                ", drzava='" + drzava + '\'' +
                ", drzavaIso='" + drzavaIso + '\'' +
                ", kupovniTecaj=" + kupovniTecaj +
                ", prodajniTecaj=" + prodajniTecaj +
                ", sifraValute='" + sifraValute + '\'' +
                ", srednjiTecaj=" + srednjiTecaj +
                ", valuta='" + valuta + '\'' +
                '}';
    }

    public String drzavaIso;
    public double kupovniTecaj;
    public double prodajniTecaj;
    public String sifraValute;
    public double srednjiTecaj;
    public String valuta;

    public Valuta(int brojTecajnice, String datumPrimjene, String drzava, String drzavaIso, double kupovniTecaj, double prodajniTecaj, String sifraValute, double srednjiTecaj, String valuta) {
        this.brojTecajnice = brojTecajnice;
        this.datumPrimjene = datumPrimjene;
        this.drzava = drzava;
        this.drzavaIso = drzavaIso;
        this.kupovniTecaj = kupovniTecaj;
        this.prodajniTecaj = prodajniTecaj;
        this.sifraValute = sifraValute;
        this.srednjiTecaj = srednjiTecaj;
        this.valuta = valuta;
    }

    public Valuta(String datumPrimjene, String drzava, double kupovniTecaj, double prodajniTecaj, String sifraValute, double srednjiTecaj) {
        this.datumPrimjene = datumPrimjene;
        this.drzava = drzava;
        this.kupovniTecaj = kupovniTecaj;
        this.prodajniTecaj = prodajniTecaj;
        this.sifraValute = sifraValute;
        this.srednjiTecaj = srednjiTecaj;
    }

    public int getBrojTecajnice() {
        return brojTecajnice;
    }

    public void setBrojTecajnice(int brojTecajnice) {
        this.brojTecajnice = brojTecajnice;
    }

    public String getDatumPrimjene() {
        return datumPrimjene;
    }

    public void setDatumPrimjene(String datumPrimjene) {
        this.datumPrimjene = datumPrimjene;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getDrzavaIso() {
        return drzavaIso;
    }

    public void setDrzavaIso(String drzavaIso) {
        this.drzavaIso = drzavaIso;
    }

    public double getKupovniTecaj() {
        return kupovniTecaj;
    }

    public void setKupovniTecaj(double kupovniTecaj) {
        this.kupovniTecaj = kupovniTecaj;
    }

    public double getProdajniTecaj() {
        return prodajniTecaj;
    }

    public void setProdajniTecaj(double prodajniTecaj) {
        this.prodajniTecaj = prodajniTecaj;
    }

    public String getSifraValute() {
        return sifraValute;
    }

    public void setSifraValute(String sifraValute) {
        this.sifraValute = sifraValute;
    }

    public double getSrednjiTecaj() {
        return srednjiTecaj;
    }

    public void setSrednjiTecaj(double srednjiTecaj) {
        this.srednjiTecaj = srednjiTecaj;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }
}
