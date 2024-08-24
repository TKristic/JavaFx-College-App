package com.example.tvzprojekt.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Student extends Entitet implements Serializable {

    private Integer godina;

    public Student() {
        super();
    }

    public Student(String jmbag, String ime, String prezime, LocalDate datumRodenja, String smjer, Integer godina) {
        super(jmbag, ime, prezime, datumRodenja, smjer);
        this.godina = godina;
        this.setTip("Student");
    }

    public Integer getGodina() {
        return godina;
    }

    public void setGodina(Integer godina) {
        this.godina = godina;
    }
}
