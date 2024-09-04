package com.example.tvzprojekt.Model;

import java.io.Serializable;
import java.time.LocalDate;

public class ProfesorBuilder extends Entitet implements Serializable {

    public ProfesorBuilder() {
        super();
    }

    public ProfesorBuilder(String jmbag, String ime, String prezime, String smjer, String tip) {
        this.setJmbag(jmbag);
        this.setIme(ime);
        this.setPrezime(prezime);
        this.setSmjer(smjer);
        this.setTip(tip);
    }

    public ProfesorBuilder(String jmbag, String ime, String prezime, LocalDate datumRodenja, String smjer) {
        super(jmbag, ime, prezime, datumRodenja, smjer);
        this.setTip("Profesor");
    }
}
