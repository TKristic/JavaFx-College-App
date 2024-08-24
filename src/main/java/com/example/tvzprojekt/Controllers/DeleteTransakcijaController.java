package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.IspisPromjene;
import com.example.tvzprojekt.Model.Promjena;
import com.example.tvzprojekt.Model.StatusiPromjene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DeleteTransakcijaController {

    @FXML
    public Label label;
    @FXML
    public Button accept;
    @FXML
    public Button cancel;

    @FXML
    public void initialize() {
        label.setText("Jeste li sigurni da želite obrisati transakciju (" + TransakcijeController.odabranaTransakcija.getID() +
                " " + TransakcijeController.odabranaTransakcija.getOpisPlacanja() + " " +
                TransakcijeController.odabranaTransakcija.getIznos() + ")");
    }

    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void acceptDialog() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "DELETE FROM TRANSAKCIJE WHERE ID_TRANSAKCIJA = " + TransakcijeController.odabranaTransakcija.getID();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();

            TransakcijeController.transakcijeList.remove(TransakcijeController.odabranaTransakcija);
            Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.OBRISI, TransakcijeController.odabranaTransakcija.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));
            TransakcijeController.odabranaTransakcija = null;

            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
