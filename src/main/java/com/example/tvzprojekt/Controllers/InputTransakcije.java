package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Model.TransakcijaBuilder;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.example.tvzprojekt.Main.getStage;

public class InputTransakcije implements DialogControls {
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
                TransakcijaBuilder transakcijaBuilder = parseTransakcija(line);
                if (transakcijaBuilder != null) {
                    TransakcijeController.transakcijeList.add(transakcijaBuilder);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static TransakcijaBuilder parseTransakcija(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            Integer ID = Integer.valueOf(parts[0]);
            Double iznos = Double.valueOf(parts[1]);
            String opis = parts[2];
            String datumString = parts[3];
            String jmbag = parts[4];
            String tip = parts[5];

            LocalDate datumRodjenja = LocalDate.parse(datumString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return new TransakcijaBuilder(ID, iznos, opis, datumRodjenja, jmbag, tip);

        }
        return null;
    }

    private static void addToDatabase() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sqlCheckExistence = "SELECT 1 FROM transakcije WHERE ID = ?";
            String sqlInsert = "INSERT INTO TRANSAKCIJE(IZNOS, OPIS_PLACANJA, DATUM, JMBAG, TIP_KORISNIKA) VALUES(?, ?, ?, ?, ?)";

            try (PreparedStatement checkExistence = connection.prepareStatement(sqlCheckExistence);
                 PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {

                for (TransakcijaBuilder transakcijaBuilder : TransakcijeController.transakcijeList) {
                    checkExistence.setInt(1, transakcijaBuilder.getID());
                    ResultSet resultSet = checkExistence.executeQuery();
                    if (resultSet.next()) {
                        continue;
                    }

                    preparedStatement.setDouble(1, transakcijaBuilder.getIznos());
                    preparedStatement.setString(2, transakcijaBuilder.getOpisPlacanja());
                    preparedStatement.setDate(3, Date.valueOf(transakcijaBuilder.getDatumTransakcije()));
                    preparedStatement.setString(4, transakcijaBuilder.getJmbag());
                    preparedStatement.setString(5, transakcijaBuilder.getTipKorisnika());

                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
