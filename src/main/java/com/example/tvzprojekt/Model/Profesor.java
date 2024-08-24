package com.example.tvzprojekt.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Profesor extends Entitet implements Serializable {

    public Profesor() {
        super();
    }

    public Profesor(String jmbag, String ime, String prezime, LocalDate datumRodenja, String smjer) {
        super(jmbag, ime, prezime, datumRodenja, smjer);
        this.setTip("Profesor");
    }
}
