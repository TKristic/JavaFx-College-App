package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.IspisPromjene;
import com.example.tvzprojekt.Model.Promjena;
import com.example.tvzprojekt.Model.StatusiPromjene;
import com.example.tvzprojekt.Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PromjeneController extends Transitions {

    @FXML
    public TableView<IspisPromjene> tab;
    @FXML
    public TableColumn<IspisPromjene, StatusiPromjene> statusTab;
    @FXML
    public TableColumn<IspisPromjene, String> objekt;
    @FXML
    public TableColumn<IspisPromjene, String> korisnikTab;
    @FXML
    public TableColumn<IspisPromjene, LocalDateTime> vrijeme;
    @FXML
    public Label korisnik;
    @FXML
    public Label status;

    public static ObservableList<IspisPromjene> lista;

    public void initialize() {

        korisnik.setText("Korisnik : " + Main.getCurrentUser().getUsername());
        status.setText("Status : " + Main.getCurrentUser().getStatus().toString().toLowerCase());

        lista = readPromjeneFromFile();

        statusTab.setCellValueFactory(new PropertyValueFactory<>("status"));
        objekt.setCellValueFactory(new PropertyValueFactory<>("klasa"));
        korisnikTab.setCellValueFactory(new PropertyValueFactory<>("username"));
        vrijeme.setCellValueFactory(new PropertyValueFactory<>("vrijemePromjene"));

        tab.setItems(lista);
    }

    public static ObservableList<IspisPromjene> readPromjeneFromFile() {
        String datoteka = "promjene.bat";
        ObservableList<IspisPromjene> promjene = FXCollections.observableArrayList();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(datoteka))) {
            while (true) {
                try {
                    // Deserijalizacija objekta
                    IspisPromjene promjena = (IspisPromjene) ois.readObject();
                    // Dodavanje u ObservableList
                    promjene.add(promjena);
                } catch (EOFException eof) {
                    break; // Kraj datoteke
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return promjene;
    }
}
