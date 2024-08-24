package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.example.tvzprojekt.Main.getStage;

public class InputStudenti implements DialogControls {

    @FXML
    public Label filename;
    @FXML
    public Button accept;
    @FXML
    public Button cancel;

    private File file = null;

    public void fileHandle() {
        file = null;
        filename.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Odaberi datoteku");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tekstualne datoteke", "*.txt"));


        file = fileChooser.showOpenDialog(getStage());
        if (file != null) filename.setText(file.getName());
    }

    @Override
    public void acceptDialog() {
        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Prvo odaberite datoteku za čitanje");
            alert.showAndWait();
            return;
        }

        Thread readTxtThread = new Thread(() -> {
            readTxtFile(file.getAbsolutePath());
            addToDatabase();
        });
        readTxtThread.start();

        cancelDialog();
    }

    @Override
    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    private static void readTxtFile(String fileName) {

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student student = parseStudent(line);
                if (student != null) {
                    StudentiController.studentiList.add(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Student parseStudent(String line) {
        String[] parts = line.split(",");
        if (parts.length == 6) {
            String jmbag = parts[0];
            String ime = parts[1];
            String prezime = parts[2];
            String datumString = parts[3];
            String smjer = parts[4];
            Integer godina = Integer.parseInt(parts[5]);

            LocalDate datumRodjenja = LocalDate.parse(datumString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return new Student(jmbag, ime, prezime, datumRodjenja, smjer, godina);

        }
        return null;
    }

    private static void addToDatabase() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sqlCheckExistence = "SELECT 1 FROM studenti WHERE jmbag = ?";
            String updateEntiteti = "INSERT INTO entiteti (jmbag, tip) VALUES (?, ?)";
            String sqlInsert = "INSERT INTO studenti (jmbag, ime, prezime, datum, smjer, godina) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement checkExistence = connection.prepareStatement(sqlCheckExistence);
                 PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
                 PreparedStatement updateStatement = connection.prepareStatement(updateEntiteti)) {

                for (Student student : StudentiController.studentiList) {
                    checkExistence.setString(1, student.getJmbag());
                    ResultSet resultSet = checkExistence.executeQuery();
                    if (resultSet.next()) {
                        continue;
                    }

                    updateStatement.setString(1, student.getJmbag());
                    updateStatement.setString(2, "Student");

                    updateStatement.executeUpdate();

                    preparedStatement.setString(1, student.getJmbag());
                    preparedStatement.setString(2, student.getIme());
                    preparedStatement.setString(3, student.getPrezime());
                    preparedStatement.setDate(4, Date.valueOf(student.getDatumRodenja()));
                    preparedStatement.setString(5, student.getSmjer());
                    preparedStatement.setInt(6, student.getGodina());

                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
