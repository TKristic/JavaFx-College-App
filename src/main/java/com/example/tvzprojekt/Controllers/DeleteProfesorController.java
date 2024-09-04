package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.IspisPromjene;
import com.example.tvzprojekt.Model.StatusiPromjene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DeleteProfesorController implements DialogControls{

    @FXML
    public Label label;
    @FXML
    public Button accept;
    @FXML
    public Button cancel;

    @FXML
    public void initialize() {
        label.setText("Jeste li sigurni da želite obrisati profesora (" + ProfesoriController.odabraniProfesorBuilder.getIme() +
                " " + ProfesoriController.odabraniProfesorBuilder.getPrezime() + " " +
                ProfesoriController.odabraniProfesorBuilder.getJmbag() + ")");
    }

    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void acceptDialog() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "DELETE FROM PROFESORI WHERE JMBAG = " + ProfesoriController.odabraniProfesorBuilder.getJmbag();
            String delete = "DELETE FROM ENTITETI WHERE JMBAG = " + ProfesoriController.odabraniProfesorBuilder.getJmbag();
            String deleteTransakcije = "DELETE FROM TRANSAKCIJE WHERE JMBAG = " + ProfesoriController.odabraniProfesorBuilder.getJmbag();

            PreparedStatement transakcijeStatement = connection.prepareStatement(deleteTransakcije);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            PreparedStatement deleteStatement = connection.prepareStatement(delete);

            transakcijeStatement.executeUpdate();
            preparedStatement.executeUpdate();
            deleteStatement.executeUpdate();

            ProfesoriController.profesoriList.remove(ProfesoriController.odabraniProfesorBuilder);
            Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.OBRISI, ProfesoriController.odabraniProfesorBuilder.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));

            ProfesoriController.odabraniProfesorBuilder = null;

            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
