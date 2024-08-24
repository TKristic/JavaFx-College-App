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

public class DeleteEventController implements DialogControls {
    @FXML
    public Label label;
    @FXML
    public Button accept;
    @FXML
    public Button cancel;

    @FXML
    public void initialize() {
        label.setText("Jeste li sigurni da želite obrisati event (" + EventsController.odabraniEvent.getID() +
                " " + EventsController.odabraniEvent.getNazivEventa() + " " +
                EventsController.odabraniEvent.getPosjecenost() + ")");
    }

    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void acceptDialog() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "DELETE FROM EVENTI WHERE ID_EVENT = " + EventsController.odabraniEvent.getID();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();

            EventsController.eventiList.remove(EventsController.odabraniEvent);
            Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.OBRISI, EventsController.odabraniEvent.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));
            EventsController.odabraniEvent = null;

            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
