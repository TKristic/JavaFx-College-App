package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.StudentBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public non-sealed class StudentiController extends Transitions implements SearchBar {
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
    public TableView<StudentBuilder> studentiTab;
    @FXML
    public TableColumn<StudentBuilder, String> jmbag;
    @FXML
    public TableColumn<StudentBuilder, String> ime;
    @FXML
    public TableColumn<StudentBuilder, String> prezime;
    @FXML
    public TableColumn<StudentBuilder, Date> datumRodenja;
    @FXML
    public TableColumn<StudentBuilder, String> smjer;
    @FXML
    public TableColumn<StudentBuilder, Integer> godina;
    @FXML
    public ChoiceBox kategorije;
    @FXML
    public TextField value;
    @FXML
    public Label korisnik;
    @FXML
    public Label status;

    public static StudentBuilder odabraniStudentBuilder;

    public static ObservableList<StudentBuilder> studentiList;

    @FXML
    public void initialize() {
        korisnik.setText("Korisnik : " + Main.getCurrentUser().getUsername());
        status.setText("Status : " + Main.getCurrentUser().getStatus().toString().toLowerCase());

        studentiTab.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try(Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT * FROM STUDENTI";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            jmbag.setCellValueFactory(new PropertyValueFactory<>("jmbag"));
            ime.setCellValueFactory(new PropertyValueFactory<>("ime"));
            prezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));
            datumRodenja.setCellValueFactory(new PropertyValueFactory<>("datumRodenja"));
            smjer.setCellValueFactory(new PropertyValueFactory<>("smjer"));
            godina.setCellValueFactory(new PropertyValueFactory<>("godina"));

            studentiList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String jmbag = resultSet.getString("JMBAG");
                String ime = resultSet.getString("IME");
                String prezime = resultSet.getString("PREZIME");
                LocalDate datumRodenja = resultSet.getDate("DATUM").toLocalDate();
                String smjer = resultSet.getString("SMJER");
                Integer godina = Integer.valueOf(resultSet.getInt("GODINA"));


                StudentBuilder studentBuilder = new StudentBuilder(jmbag, ime, prezime, datumRodenja, smjer, godina);
                studentiList.add(studentBuilder);
            }

            studentiTab.setItems(studentiList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showStudentDialog(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("studentDialog.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Unos Studenta");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void showModifyStudentDialog(MouseEvent mouseEvent) throws IOException {

        if(!Main.isAdmin()) {
            Main.logger.error(Main.getCurrentUser().getUsername() + " nema permisiju za izvršavanje ove radnje");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Nemate permisiju za izvršavanje ove radnje");
            alert.showAndWait();
            return;
        }

        odabraniStudentBuilder = studentiTab.getSelectionModel().getSelectedItem();

        if (odabraniStudentBuilder == null) {
            Main.logger.error("Nije odabran entitet za ovu akciju");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Prvo odaberite entitet iz liste za tu akciju");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("modifyStudent.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Uređivanje studenta");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }


    public void showIODialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pitanjeStudent.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Potvrda Studenti");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void showDeleteStudentDialog(MouseEvent mouseEvent) throws IOException {

        if(!Main.isAdmin()) {
            Main.logger.error(Main.getCurrentUser().getUsername() + " nema permisiju za izvršavanje ove radnje");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Nemate permisiju za izvršavanje ove radnje");
            alert.showAndWait();
            return;
        }

        odabraniStudentBuilder = studentiTab.getSelectionModel().getSelectedItem();

        if (odabraniStudentBuilder == null) {
            Main.logger.error("Nije odabran entitet za ovu akciju");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Prvo odaberite entitet iz liste za tu akciju");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("deleteStudent.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Brisanje studenta");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void clearValues() {
        kategorije.setValue(null);
        value.setText("");
    }

    public void searchForValue() {
        if (kategorije.getValue() == null || value.getText().equals("")) {
            Main.logger.error("Oba polja moraju biti popunjena");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Oba polja moraju biti popunjena");
            alert.showAndWait();
            return;
        }

        studentiList = null;
        String kategorija = (String) kategorije.getValue();
        String query = switch (kategorija) {
            case "JMBAG" -> "SELECT * FROM STUDENTI WHERE JMBAG = ?";
            case "Ime" -> "SELECT * FROM STUDENTI WHERE IME = ?";
            case "Prezime" -> "SELECT * FROM STUDENTI WHERE PREZIME = ?";
            case "Smjer" -> "SELECT * FROM STUDENTI WHERE SMJER = ?";
            case "Godina" -> "SELECT * FROM STUDENTI WHERE GODINA = ?";
            default -> throw new IllegalArgumentException("Nepoznata kategorija: " + kategorija);
        };

        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, value.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            jmbag.setCellValueFactory(new PropertyValueFactory<>("jmbag"));
            ime.setCellValueFactory(new PropertyValueFactory<>("ime"));
            prezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));
            datumRodenja.setCellValueFactory(new PropertyValueFactory<>("datumRodenja"));
            smjer.setCellValueFactory(new PropertyValueFactory<>("smjer"));
            godina.setCellValueFactory(new PropertyValueFactory<>("godina"));

            List<StudentBuilder> studentBuilders = new ArrayList<>();
            while (resultSet.next()) {
                String jmbag = resultSet.getString("JMBAG");
                String ime = resultSet.getString("IME");
                String prezime = resultSet.getString("PREZIME");
                LocalDate datumRodenja = resultSet.getDate("DATUM").toLocalDate();
                String smjer = resultSet.getString("SMJER");
                Integer godina = resultSet.getInt("GODINA");

                StudentBuilder studentBuilder = new StudentBuilder(jmbag, ime, prezime, datumRodenja, smjer, godina);
                studentBuilders.add(studentBuilder);
            }

            studentiList = studentBuilders.stream()
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            studentiTab.setItems(studentiList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
