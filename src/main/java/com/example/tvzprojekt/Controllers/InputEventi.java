package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Model.Event;
import com.example.tvzprojekt.Model.Kljucevi;
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

import static com.example.tvzprojekt.Main.getStage;

public class InputEventi implements DialogControls{
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
                Event event = parseEvent(line);
                if (event != null) {
                    EventsController.eventiList.add(event);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Event parseEvent(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            Integer ID = Integer.valueOf(parts[0]);
            String naziv = parts[1];
            Integer posjecenost = Integer.valueOf(parts[2]);
            Double cijena = Double.valueOf(parts[3]);
            String datumString = parts[4];

            try {
                Date datumRodjenja = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(datumString).getTime());
                return new Event(ID, naziv, posjecenost, cijena, datumRodjenja);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void addToDatabase() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sqlCheckExistence = "SELECT 1 FROM eventi WHERE ID = ?";
            String sqlInsert = "INSERT INTO EVENTI(ID, NAZIV_EVENTA, POSJECENOST, CIJENA_KARTE, DATUM) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement checkExistence = connection.prepareStatement(sqlCheckExistence);
                 PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {

                for (Event event : EventsController.eventiList) {
                    checkExistence.setInt(1, event.getID());
                    ResultSet resultSet = checkExistence.executeQuery();
                    if (resultSet.next()) {
                        continue;
                    }

                    preparedStatement.setInt(1, event.getID());
                    preparedStatement.setString(2, event.getNazivEventa());
                    preparedStatement.setInt(3, event.getPosjecenost());
                    preparedStatement.setDouble(4, event.getCijenaKarte());
                    preparedStatement.setDate(5, (Date) event.getDatumEventa());

                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
