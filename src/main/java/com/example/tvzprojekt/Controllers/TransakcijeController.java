package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.TransakcijaBuilder;
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

public non-sealed class TransakcijeController extends Transitions implements SearchBar {
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
    public Label korisnik;
    @FXML
    public Label status;
    @FXML
    public ChoiceBox kategorije;
    @FXML
    public TextField value;
    @FXML
    public TableView<TransakcijaBuilder> transakcijeTab;
    @FXML
    public TableColumn<TransakcijaBuilder, Integer> id;
    @FXML
    public TableColumn<TransakcijaBuilder, Double> iznos;
    @FXML
    public TableColumn<TransakcijaBuilder, String> opis;
    @FXML
    public TableColumn<TransakcijaBuilder, Date> datum;
    @FXML
    public TableColumn<TransakcijaBuilder, String> jmbag;
    @FXML
    public TableColumn<TransakcijaBuilder, String> tip;
    public static ObservableList<TransakcijaBuilder> transakcijeList;
    public static TransakcijaBuilder odabranaTransakcijaBuilder;

    @FXML
    public void initialize() {
        korisnik.setText("Korisnik : " + Main.getCurrentUser().getUsername());
        status.setText("Status : " + Main.getCurrentUser().getStatus().toString().toLowerCase());

        transakcijeTab.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try(Connection connection = DatabaseConnector.getConnection()) {

            String query = "SELECT * FROM TRANSAKCIJE";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            id.setCellValueFactory(new PropertyValueFactory<>("ID"));
            iznos.setCellValueFactory(new PropertyValueFactory<>("iznos"));
            opis.setCellValueFactory(new PropertyValueFactory<>("opisPlacanja"));
            datum.setCellValueFactory(new PropertyValueFactory<>("datumTransakcije"));
            jmbag.setCellValueFactory(new PropertyValueFactory<>("jmbag"));
            tip.setCellValueFactory(new PropertyValueFactory<>("tipKorisnika"));

            transakcijeList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID_TRANSAKCIJA");
                double iznos = resultSet.getDouble("IZNOS");
                String opis = resultSet.getString("OPIS");
                LocalDate datum = resultSet.getDate("DATUM").toLocalDate();
                String jmbag = resultSet.getString("JMBAG");
                String korisnik = resultSet.getString("TIP");

                TransakcijaBuilder transakcijaBuilder = new TransakcijaBuilder(id, iznos, opis, datum, jmbag, korisnik);
                transakcijeList.add(transakcijaBuilder);
            }

            transakcijeTab.setItems(transakcijeList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showIODialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("pitanjeTransakcija.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Potvrda Transakcije");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void showTransakcijaDialog(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("transakcijaDialog.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Unos transakcije");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void showModifyTransakcijaDialog(MouseEvent mouseEvent) throws IOException {

        if(!Main.isAdmin()) {
            Main.logger.error(Main.getCurrentUser().getUsername() + " nema permisiju za izvršavanje ove radnje");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Nemate permisiju za izvršavanje ove radnje");
            alert.showAndWait();
            return;
        }

        odabranaTransakcijaBuilder = transakcijeTab.getSelectionModel().getSelectedItem();

        if (odabranaTransakcijaBuilder == null) {
            Main.logger.error("Nije odabran entitet za ovu akciju");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Prvo odaberite entitet iz liste za tu akciju");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("modifyTransakcija.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Uređivanje transakcije");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void showDeleteTransakcijaDialog(MouseEvent mouseEvent) throws IOException {

        if(!Main.isAdmin()) {
            Main.logger.error(Main.getCurrentUser().getUsername() + " nema permisiju za izvršavanje ove radnje");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Nemate permisiju za izvršavanje ove radnje");
            alert.showAndWait();
            return;
        }

        odabranaTransakcijaBuilder = transakcijeTab.getSelectionModel().getSelectedItem();

        if (odabranaTransakcijaBuilder == null) {
            Main.logger.error("Nije odabran entitet za ovu akciju");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Prvo odaberite entitet iz liste za tu akciju");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("deleteTransakcija.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Brisanje transakcije");
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
                Main.logger.error("Vrijednost mora biti brojčana");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText(null);
                alert.setContentText("Vrijednost mora biti brojčana");
                alert.showAndWait();
                return;
            }
        }

        transakcijeList = FXCollections.observableArrayList();
        String kategorija = (String) kategorije.getValue();
        String query = switch (kategorija) {
            case "ID" -> "SELECT * FROM TRANSAKCIJE WHERE ID = ?";
            case "Veće od" -> "SELECT * FROM TRANSAKCIJE WHERE IZNOS > ?";
            case "Manje od" -> "SELECT * FROM TRANSAKCIJE WHERE IZNOS < ?";
            default -> throw new IllegalArgumentException("Nepoznata kategorija: " + kategorija);
        };

        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, value.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            id.setCellValueFactory(new PropertyValueFactory<>("ID"));
            iznos.setCellValueFactory(new PropertyValueFactory<>("iznos"));
            opis.setCellValueFactory(new PropertyValueFactory<>("opisPlacanja"));
            datum.setCellValueFactory(new PropertyValueFactory<>("datumTransakcije"));
            jmbag.setCellValueFactory(new PropertyValueFactory<>("jmbag"));
            tip.setCellValueFactory(new PropertyValueFactory<>("tipKorisnika"));

            List<TransakcijaBuilder> transactions = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                double iznos = resultSet.getDouble("IZNOS");
                String opis = resultSet.getString("OPIS_PLACANJA");
                LocalDate datum = resultSet.getDate("DATUM").toLocalDate();
                String jmbag = resultSet.getString("JMBAG");
                String korisnik = resultSet.getString("TIP_KORISNIKA");

                TransakcijaBuilder transakcijaBuilder = new TransakcijaBuilder(id, iznos, opis, datum, jmbag, korisnik);
                transactions.add(transakcijaBuilder);
            }

            transakcijeList = transactions.stream()
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            transakcijeTab.setItems(transakcijeList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void clearValues() {
        kategorije.setValue(null);
        value.setText("");
    }
}
