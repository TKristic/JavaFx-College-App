package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.ProfesorBuilder;
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

public non-sealed class ProfesoriController extends Transitions implements SearchBar {
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
    public TableView<ProfesorBuilder> profesoriTab;
    @FXML
    public TableColumn<ProfesorBuilder, String> jmbag;
    @FXML
    public TableColumn<ProfesorBuilder, String> ime;
    @FXML
    public TableColumn<ProfesorBuilder, String> prezime;
    @FXML
    public TableColumn<ProfesorBuilder, Date> datumRodenja;
    @FXML
    public TableColumn<ProfesorBuilder, String> smjer;
    @FXML
    public ChoiceBox kategorije;
    @FXML
    public TextField value;
    @FXML
    public Label korisnik;
    @FXML
    public Label status;

    public static ProfesorBuilder odabraniProfesorBuilder;

    public static ObservableList<ProfesorBuilder> profesoriList;

    @FXML
    public void initialize() {
        korisnik.setText("Korisnik : " + Main.getCurrentUser().getUsername());
        status.setText("Status : " + Main.getCurrentUser().getStatus().toString().toLowerCase());

        profesoriTab.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try(Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT * FROM PROFESORI";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            jmbag.setCellValueFactory(new PropertyValueFactory<>("jmbag"));
            ime.setCellValueFactory(new PropertyValueFactory<>("ime"));
            prezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));
            datumRodenja.setCellValueFactory(new PropertyValueFactory<>("datumRodenja"));
            smjer.setCellValueFactory(new PropertyValueFactory<>("smjer"));

            profesoriList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String jmbag = resultSet.getString("JMBAG");
                String ime = resultSet.getString("IME");
                String prezime = resultSet.getString("PREZIME");
                LocalDate datumRodenja = resultSet.getDate("DATUM").toLocalDate();
                String smjer = resultSet.getString("SMJER");

                ProfesorBuilder profesorBuilder = new ProfesorBuilder(jmbag, ime, prezime, datumRodenja, smjer);
                profesoriList.add(profesorBuilder);
            }

            profesoriTab.setItems(profesoriList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        profesoriList = FXCollections.observableArrayList();
        String kategorija = (String) kategorije.getValue();
        String query = switch (kategorija) {
            case "JMBAG" -> "SELECT * FROM PROFESORI WHERE JMBAG = ?";
            case "Ime" -> "SELECT * FROM PROFESORI WHERE IME = ?";
            case "Prezime" -> "SELECT * FROM PROFESORI WHERE PREZIME = ?";
            case "Smjer" -> "SELECT * FROM PROFESORI WHERE SMJER = ?";
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

            List<ProfesorBuilder> profesori = new ArrayList<>();
            while (resultSet.next()) {
                String jmbag = resultSet.getString("JMBAG");
                String ime = resultSet.getString("IME");
                String prezime = resultSet.getString("PREZIME");
                LocalDate datumRodenja = resultSet.getDate("DATUM").toLocalDate();
                String smjer = resultSet.getString("SMJER");

                ProfesorBuilder profesorBuilder = new ProfesorBuilder(jmbag, ime, prezime, datumRodenja, smjer);
                profesori.add(profesorBuilder);
            }

            profesoriList = profesori.stream()
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            profesoriTab.setItems(profesoriList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearValues() {
        kategorije.setValue(null);
        value.setText("");
    }

    public void showIODialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pitanjeProfesor.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Potvrda Profesori");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void showProfesorDialog(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("profesorDialog.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Unos Profesora");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void showModifyProfesorDialog(MouseEvent mouseEvent) throws IOException {

        if(!Main.isAdmin()) {
            Main.logger.error(Main.getCurrentUser().getUsername() + " nema permisiju za izvršavanje ove radnje");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Nemate permisiju za izvršavanje ove radnje");
            alert.showAndWait();
            return;
        }

        odabraniProfesorBuilder = profesoriTab.getSelectionModel().getSelectedItem();

        if (odabraniProfesorBuilder == null) {
            Main.logger.error("Nije odabran entitet za ovu akciju");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Prvo odaberite entitet iz liste za tu akciju");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("modifyProfesor.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Uređivanje profesora");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void showDeleteProfesorDialog(MouseEvent mouseEvent) throws IOException {

        if(!Main.isAdmin()) {
            Main.logger.error(Main.getCurrentUser().getUsername() + " nema permisiju za izvršavanje ove radnje");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Nemate permisiju za izvršavanje ove radnje");
            alert.showAndWait();
            return;
        }

        odabraniProfesorBuilder = profesoriTab.getSelectionModel().getSelectedItem();

        if (odabraniProfesorBuilder == null) {
            Main.logger.error("Nije odabran entitet za ovu akciju");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Prvo odaberite entitet iz liste za tu akciju");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("deleteProfesor.fxml"));
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
}
