package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.IspisPromjene;
import com.example.tvzprojekt.Model.StatusiPromjene;
import com.example.tvzprojekt.Model.StudentBuilder;
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
        labime.setText(StudentiController.odabraniStudentBuilder.getIme());
        labprezime.setText(StudentiController.odabraniStudentBuilder.getPrezime());
        labdatum.setText(StudentiController.odabraniStudentBuilder.getDatumRodenja().toString());
        labsmjer.setText(StudentiController.odabraniStudentBuilder.getSmjer());
        labgodina.setText(StudentiController.odabraniStudentBuilder.getGodina().toString());
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

        StudentiController.studentiList.remove(StudentiController.odabraniStudentBuilder);

        try(Connection connection = DatabaseConnector.getConnection()) {
            String query = "UPDATE STUDENTI SET IME=?, PREZIME=?, DATUM=?, SMJER=?, GODINA=? WHERE JMBAG=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, (newIme.equals("") ? StudentiController.odabraniStudentBuilder.getIme() : newIme));
            preparedStatement.setString(2, (newPrezime.equals("") ? StudentiController.odabraniStudentBuilder.getPrezime() : newPrezime));
            preparedStatement.setDate(3, (newDatum == null) ? Date.valueOf(StudentiController.odabraniStudentBuilder.getDatumRodenja()) : newDatum);
            preparedStatement.setString(4, (newSmjer.equals("") ? StudentiController.odabraniStudentBuilder.getSmjer() : newSmjer));
            preparedStatement.setInt(5, (newGodina == 0 ? StudentiController.odabraniStudentBuilder.getGodina() : newGodina));
            preparedStatement.setString(6, StudentiController.odabraniStudentBuilder.getJmbag());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        StudentBuilder studentBuilder = new StudentBuilder(StudentiController.odabraniStudentBuilder.getJmbag(),
                (newIme.equals("") ? StudentiController.odabraniStudentBuilder.getIme() : newIme),
                (newPrezime.equals("") ? StudentiController.odabraniStudentBuilder.getPrezime() : newPrezime),
                ((newDatum == null) ? Date.valueOf(StudentiController.odabraniStudentBuilder.getDatumRodenja()) : newDatum).toLocalDate(),
                (newSmjer.equals("") ? StudentiController.odabraniStudentBuilder.getSmjer() : newSmjer),
                (newGodina == 0 ? StudentiController.odabraniStudentBuilder.getGodina() : newGodina));

        StudentiController.studentiList.add(studentBuilder);

        Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.PROMJENI, studentBuilder.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));
        StudentiController.odabraniStudentBuilder = null;
        cancelDialog();
    }

    @Override
    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
