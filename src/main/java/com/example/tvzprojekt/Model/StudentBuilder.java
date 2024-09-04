package com.example.tvzprojekt.Model;

import java.io.Serializable;
import java.time.LocalDate;

public class StudentBuilder extends Entitet implements Serializable {

    private Integer godina;

    public StudentBuilder() {
        super();
    }

    public StudentBuilder(String jmbag, String ime, String prezime, LocalDate datumRodenja, String smjer, Integer godina) {
        super(jmbag, ime, prezime, datumRodenja, smjer);
        this.godina = godina;
        this.setTip("Student");
    }

    public StudentBuilder(String jmbag, String ime, String prezime, String smjer, String tip) {
        this.setJmbag(jmbag);
        this.setIme(ime);
        this.setPrezime(prezime);
        this.setSmjer(smjer);
        this.setTip(tip);
    }

    public Integer getGodina() {
        return godina;
    }

    public void setGodina(Integer godina) {
        this.godina = godina;
    }
}
