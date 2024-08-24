package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.Event;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public non-sealed class EventsController extends Transitions implements SearchBar {
    @FXML
    public HBox DashBtn;
    @FXML
    public HBox eventiBtn;
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
    public ChoiceBox kategorije;
    @FXML
    public TextField value;
    @FXML
    public TableView<Event> eventiTab;
    @FXML
    public TableColumn<Event, Integer> id;
    @FXML
    public TableColumn<Event, String> naziv;
    @FXML
    public TableColumn<Event, Integer> posjecenost;
    @FXML
    public TableColumn<Event, Double> cijena;
    @FXML
    public TableColumn<Event, Date> datum;

    public static ObservableList<Event> eventiList;
    public static Event odabraniEvent;

    @FXML
    public void initialize() {
        korisnik.setText("Korisnik : " + Main.getCurrentUser().getUsername());
        status.setText("Status : " + Main.getCurrentUser().getStatus().toString().toLowerCase());

        eventiTab.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try(Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT * FROM EVENTI";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            id.setCellValueFactory(new PropertyValueFactory<>("ID"));
            naziv.setCellValueFactory(new PropertyValueFactory<>("nazivEventa"));
            posjecenost.setCellValueFactory(new PropertyValueFactory<>("posjecenost"));
            cijena.setCellValueFactory(new PropertyValueFactory<>("cijenaKarte"));
            datum.setCellValueFactory(new PropertyValueFactory<>("datumEventa"));

            eventiList = FXCollections.observableArrayList();



            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String naziv = resultSet.getString(2);
                int posjecenost = resultSet.getInt(3);
                double cijena = resultSet.getDouble(4);
                Date datum = resultSet.getDate(5);



                Event event = new Event(id, naziv, posjecenost, cijena, datum);
                eventiList.add(event);
            }

            eventiTab.setItems(eventiList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showEventDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("eventDialog.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Unos eventa");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void showModifyEventDialog() throws IOException {

        if(!Main.isAdmin()) {
            Main.logger.error(Main.getCurrentUser().getUsername() + " nema permisiju za izvršavanje ove radnje");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Nemate permisiju za izvršavanje ove radnje");
            alert.showAndWait();
            return;
        }

        odabraniEvent = eventiTab.getSelectionModel().getSelectedItem();

        if (odabraniEvent == null) {
            Main.logger.error("Nije odabran entitet za ovu akciju");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Prvo odaberite entitet iz liste za tu akciju");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("modifyEvent.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Uređivanje eventa");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void showDeleteEventDialog() throws IOException {

        if(!Main.isAdmin()) {
            Main.logger.error(Main.getCurrentUser().getUsername() + " nema permisiju za izvršavanje ove radnje");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Nemate permisiju za izvršavanje ove radnje");
            alert.showAndWait();
            return;
        }

        odabraniEvent = eventiTab.getSelectionModel().getSelectedItem();

        if (odabraniEvent == null) {
            Main.logger.error("Nije odabran entitet za ovu akciju");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Prvo odaberite entitet iz liste za tu akciju");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("deleteEvent.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Brisanje eventa");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
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

        try {
            Double.parseDouble(value.getText());
        } catch (NumberFormatException e) {
            try {
                Integer.parseInt(value.getText());
            } catch (NumberFormatException e2) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText(null);
                alert.setContentText("Vrijednost mora biti brojčana");
                alert.showAndWait();
                return;
            }
        }


        String kategorija = (String) kategorije.getValue();
        String query = switch (kategorija) {
            case "ID" -> "SELECT * FROM EVENTI WHERE ID = ?";
            case "Posjećenost veća od" -> "SELECT * FROM EVENTI WHERE POSJECENOST > ?";
            case "Posjećenost manja od" -> "SELECT * FROM EVENTI WHERE POSJECENOST < ?";
            case "Cijena veća od" -> "SELECT * FROM EVENTI WHERE CIJENA_KARTE > ?";
            case "Cijena manja od" -> "SELECT * FROM EVENTI WHERE CIJENA_KARTE < ?";
            default -> throw new IllegalArgumentException("Nepoznata kategorija: " + kategorija);
        };

        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, value.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            id.setCellValueFactory(new PropertyValueFactory<>("ID"));
            naziv.setCellValueFactory(new PropertyValueFactory<>("nazivEventa"));
            posjecenost.setCellValueFactory(new PropertyValueFactory<>("posjecenost"));
            cijena.setCellValueFactory(new PropertyValueFactory<>("cijenaKarte"));
            datum.setCellValueFactory(new PropertyValueFactory<>("datumEventa"));

            List<Event> events = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String naziv = resultSet.getString(2);
                int posjecenost = resultSet.getInt(3);
                double cijena = resultSet.getDouble(4);
                Date datum = resultSet.getDate(5);

                Event event = new Event(id, naziv, posjecenost, cijena, datum);
                events.add(event);
            }

            eventiList = events.stream()
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            eventiTab.setItems(eventiList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showIODialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pitanjeEvent.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Potvrda Eventi");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }
    public void clearValues() {
        kategorije.setValue(null);
        value.setText("");
    }
}
