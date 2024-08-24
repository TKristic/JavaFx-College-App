package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.IspisPromjene;
import com.example.tvzprojekt.Model.Promjena;
import com.example.tvzprojekt.Model.StatusiPromjene;
import com.example.tvzprojekt.Model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ModifyStudentController implements DialogControls{

    @FXML
    public Label labime;
    @FXML
    public Label labprezime;
    @FXML
    public Label labdatum;
    @FXML
    public Label labsmjer;
    @FXML
    public Label labgodina;
    @FXML
    public TextField valime;
    @FXML
    public TextField valprezime;
    @FXML
    public DatePicker valdatum;
    @FXML
    public ChoiceBox valsmjer;
    @FXML
    public TextField valgodina;
    @FXML
    public Button accept;
    @FXML
    public Button cancel;

    public void initialize() {
        labime.setText(StudentiController.odabraniStudent.getIme());
        labprezime.setText(StudentiController.odabraniStudent.getPrezime());
        labdatum.setText(StudentiController.odabraniStudent.getDatumRodenja().toString());
        labsmjer.setText(StudentiController.odabraniStudent.getSmjer());
        labgodina.setText(StudentiController.odabraniStudent.getGodina().toString());
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
        int newGodina;
        try {
            newGodina = Integer.parseInt(valgodina.getText());
        } catch (NumberFormatException e) {
            if (valgodina.getText().equals("")) newGodina = 0;
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText(null);
                alert.setContentText("Godina mora biti brojčana vrijednost");
                alert.showAndWait();
                return;
            }
        }

        StudentiController.studentiList.remove(StudentiController.odabraniStudent);

        try(Connection connection = DatabaseConnector.getConnection()) {
            String query = "UPDATE STUDENTI SET IME=?, PREZIME=?, DATUM=?, SMJER=?, GODINA=? WHERE JMBAG=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, (newIme.equals("") ? StudentiController.odabraniStudent.getIme() : newIme));
            preparedStatement.setString(2, (newPrezime.equals("") ? StudentiController.odabraniStudent.getPrezime() : newPrezime));
            preparedStatement.setDate(3, (newDatum == null) ? Date.valueOf(StudentiController.odabraniStudent.getDatumRodenja()) : newDatum);
            preparedStatement.setString(4, (newSmjer.equals("") ? StudentiController.odabraniStudent.getSmjer() : newSmjer));
            preparedStatement.setInt(5, (newGodina == 0 ? StudentiController.odabraniStudent.getGodina() : newGodina));
            preparedStatement.setString(6, StudentiController.odabraniStudent.getJmbag());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Student student = new Student(StudentiController.odabraniStudent.getJmbag(),
                (newIme.equals("") ? StudentiController.odabraniStudent.getIme() : newIme),
                (newPrezime.equals("") ? StudentiController.odabraniStudent.getPrezime() : newPrezime),
                ((newDatum == null) ? Date.valueOf(StudentiController.odabraniStudent.getDatumRodenja()) : newDatum).toLocalDate(),
                (newSmjer.equals("") ? StudentiController.odabraniStudent.getSmjer() : newSmjer),
                (newGodina == 0 ? StudentiController.odabraniStudent.getGodina() : newGodina));

        StudentiController.studentiList.add(student);

        Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.PROMJENI, student.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));
        StudentiController.odabraniStudent = null;
        cancelDialog();
    }

    @Override
    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
