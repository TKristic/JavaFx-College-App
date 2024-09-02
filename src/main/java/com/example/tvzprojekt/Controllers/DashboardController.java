package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.DashboardVariables;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class DashboardController extends Transitions {

    @FXML
    public Label brojStudenata;
    @FXML
    public Label najGodiste;
    @FXML
    public Label NajSmjer;
    @FXML
    public Label brucosi;
    @FXML
    public Label diplomirani;
    @FXML
    public Label bilanca;
    @FXML
    public Label prihodi;
    @FXML
    public Label rashodi;
    @FXML
    public Label brojTransakcija;
    @FXML
    public Label brojProfesora;
    @FXML
    public Label najProfesija;
    @FXML
    public Label defProfesija;
    @FXML
    public Label studPoProf;
    @FXML
    public Label brojEvenata;
    @FXML
    public Label zadnjiEvent;
    @FXML
    public Label posjecenost;
    @FXML
    public HBox DashBtn;
    @FXML
    public HBox studentiBtn;
    @FXML
    public HBox profesoriBtn;
    @FXML
    public HBox transBtn;
    @FXML
    public HBox eventBtn;
    @FXML
    public HBox logBtn;

    @FXML
    public Label korisnik;
    @FXML
    public Label status;


    @FXML
    public void initialize() {
        DashboardVariables.initialize();
        brojStudenata.setText(brojStudenata.getText() + " " + DashboardVariables.brojStudenata);
        najGodiste.setText(najGodiste.getText() + " " + DashboardVariables.najGodiste);
        NajSmjer.setText(NajSmjer.getText() + " " + DashboardVariables.najSmjer);
        brucosi.setText(brucosi.getText() + " " + DashboardVariables.brucosi);
        diplomirani.setText(diplomirani.getText() + " " + DashboardVariables.diplomirani);
        bilanca.setText(bilanca.getText() + " " + DashboardVariables.bilanca);
        prihodi.setText(prihodi.getText() + " " + DashboardVariables.prihodi);
        rashodi.setText(rashodi.getText() + " " + DashboardVariables.rashodi);
        brojTransakcija.setText(brojTransakcija.getText() + " " + DashboardVariables.brojTransakcija);
        brojProfesora.setText(brojProfesora.getText() + " " + DashboardVariables.brojProfesora);
        najProfesija.setText(najProfesija.getText() + " " + DashboardVariables.najpokrivenijiSmjer);
        defProfesija.setText(defProfesija.getText() + " " + DashboardVariables.deficitarniSmjer);
        studPoProf.setText(studPoProf.getText() + " " + DashboardVariables.studentiPoProfesoru);
        brojEvenata.setText(brojEvenata.getText() + " " + DashboardVariables.brojEvenata);
        zadnjiEvent.setText(zadnjiEvent.getText() + " " + DashboardVariables.zadnjiEvent);
        posjecenost.setText(posjecenost.getText() + " " + DashboardVariables.ukupnaPosjecenost);

        korisnik.setText("Korisnik : " + Main.getCurrentUser().getUsername());
        status.setText("Status : " + Main.getCurrentUser().getStatus().toString().toLowerCase());
    }
}
