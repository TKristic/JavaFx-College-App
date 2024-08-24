package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.Event;
import com.example.tvzprojekt.Model.IspisPromjene;
import com.example.tvzprojekt.Model.Promjena;
import com.example.tvzprojekt.Model.StatusiPromjene;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ModifyEventController implements DialogControls {

    @FXML
    public Label labnaziv;
    @FXML
    public Label labposjecenost;
    @FXML
    public Label labcijena;
    @FXML
    public Label labdatum;
    @FXML
    public TextField valnaziv;
    @FXML
    public TextField valposjecenost;
    @FXML
    public TextField valcijena;
    @FXML
    public DatePicker valdatum;
    @FXML
    public Button accept;
    @FXML
    public Button cancel;

    public void initialize() {
        labnaziv.setText(String.valueOf(EventsController.odabraniEvent.getNazivEventa()));
        labposjecenost.setText(String.valueOf(EventsController.odabraniEvent.getPosjecenost()));
        labcijena.setText(String.valueOf(EventsController.odabraniEvent.getCijenaKarte()));
        labdatum.setText(EventsController.odabraniEvent.getDatumEventa().toString());
    }

    @Override
    public void acceptDialog() {
        String newnaziv = valnaziv.getText();
        int newposjecenost;
        try {
            newposjecenost = Integer.parseInt(valposjecenost.getText());
        } catch (NumberFormatException e) {
            if (valposjecenost.getText().equals("")) newposjecenost = 0;
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText(null);
                alert.setContentText("Iznos mora biti brojčana vrijednost");
                alert.showAndWait();
                return;
            }
        }
        double newcijena;
        try {
            newcijena = Double.parseDouble(valcijena.getText());
        } catch (NumberFormatException e) {
            if (valcijena.getText().equals("")) newcijena = 0;
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText(null);
                alert.setContentText("Iznos mora biti brojčana vrijednost");
                alert.showAndWait();
                return;
            }
        }
        java.sql.Date newDatum;
        try {
            newDatum = Date.valueOf(valdatum.getValue());
        } catch (NullPointerException e) {
            newDatum = null;
        }

        try(Connection connection = DatabaseConnector.getConnection()) {
            String query = "UPDATE EVENTI SET NAZIV_EVENTA=?, POSJECENOST=?, CIJENA_KARTE=?, DATUM=? WHERE ID_EVENT=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            EventsController.eventiList.remove(EventsController.odabraniEvent);

            preparedStatement.setString(1, (newnaziv.equals("") ? EventsController.odabraniEvent.getNazivEventa() : newnaziv));
            preparedStatement.setInt(2, (newposjecenost == 0 ? EventsController.odabraniEvent.getPosjecenost() : newposjecenost));
            preparedStatement.setDouble(3, (newcijena == 0 ? EventsController.odabraniEvent.getCijenaKarte() : newcijena));
            preparedStatement.setDate(4, (newDatum == null) ? (java.sql.Date) EventsController.odabraniEvent.getDatumEventa() : newDatum);
            preparedStatement.setInt(5, EventsController.odabraniEvent.getID());

            preparedStatement.executeUpdate();

            Event event = new Event(EventsController.odabraniEvent.getID(),
                    (newnaziv.equals("") ? EventsController.odabraniEvent.getNazivEventa() : newnaziv),
                    (newposjecenost == 0 ? EventsController.odabraniEvent.getPosjecenost() : newposjecenost),
                    (newcijena == 0 ? EventsController.odabraniEvent.getCijenaKarte() : newcijena),
                    (newDatum == null) ? (java.sql.Date) EventsController.odabraniEvent.getDatumEventa() : newDatum);

            EventsController.eventiList.add(event);
            Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.PROMJENI, event.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));
            EventsController.odabraniEvent = null;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        cancelDialog();
    }

    @Override
    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
