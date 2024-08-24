package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ModifyProfesorController implements DialogControls {

    @FXML
    public Label labime;
    @FXML
    public Label labprezime;
    @FXML
    public Label labdatum;
    @FXML
    public Label labsmjer;
    @FXML
    public TextField valime;
    @FXML
    public TextField valprezime;
    @FXML
    public DatePicker valdatum;
    @FXML
    public ChoiceBox valsmjer;
    @FXML
    public Button accept;
    @FXML
    public Button cancel;

    public void initialize() {
        labime.setText(ProfesoriController.odabraniProfesor.getIme());
        labprezime.setText(ProfesoriController.odabraniProfesor.getPrezime());
        labdatum.setText(ProfesoriController.odabraniProfesor.getDatumRodenja().toString());
        labsmjer.setText(ProfesoriController.odabraniProfesor.getSmjer());
    }

    @Override
    public void acceptDialog() {

        String newIme = valime.getText();
        String newPrezime = valprezime.getText();
        java.sql.Date newDatum;
        try {
            newDatum = Date.valueOf(valdatum.getValue());
        } catch (NullPointerException e) {
            newDatum = null;
        }

        String newSmjer = (String) valsmjer.getValue();
        if (newSmjer == null) newSmjer = "";

        ProfesoriController.profesoriList.remove(ProfesoriController.odabraniProfesor);



        try(Connection connection = DatabaseConnector.getConnection()) {
            String query = "UPDATE PROFESORI SET IME=?, PREZIME=?, DATUM=?, SMJER=? WHERE JMBAG=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, (newIme.equals("") ? ProfesoriController.odabraniProfesor.getIme() : newIme));
            preparedStatement.setString(2, (newPrezime.equals("") ? ProfesoriController.odabraniProfesor.getPrezime() : newPrezime));
            preparedStatement.setDate(3, (newDatum == null) ? Date.valueOf(ProfesoriController.odabraniProfesor.getDatumRodenja()) : newDatum);
            preparedStatement.setString(4, (newSmjer.equals("") ? ProfesoriController.odabraniProfesor.getSmjer() : newSmjer));
            preparedStatement.setString(5, ProfesoriController.odabraniProfesor.getJmbag());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Profesor profesor = new Profesor(ProfesoriController.odabraniProfesor.getJmbag(),
                (newIme.equals("") ? ProfesoriController.odabraniProfesor.getIme() : newIme),
                (newPrezime.equals("") ? ProfesoriController.odabraniProfesor.getPrezime() : newPrezime),
                ((newDatum == null) ? Date.valueOf(ProfesoriController.odabraniProfesor.getDatumRodenja()) : newDatum).toLocalDate(),
                (newSmjer.equals("") ? ProfesoriController.odabraniProfesor.getSmjer() : newSmjer));

        ProfesoriController.profesoriList.add(profesor);
        Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.PROMJENI, profesor.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));
        ProfesoriController.odabraniProfesor = null;

        cancelDialog();
    }

    @Override
    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    
}
