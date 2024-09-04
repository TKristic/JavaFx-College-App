package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.EventBuilder;
import com.example.tvzprojekt.Model.IspisPromjene;
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
        labnaziv.setText(String.valueOf(EventsController.odabraniEventBuilder.getNazivEventa()));
        labposjecenost.setText(String.valueOf(EventsController.odabraniEventBuilder.getPosjecenost()));
        labcijena.setText(String.valueOf(EventsController.odabraniEventBuilder.getCijenaKarte()));
        labdatum.setText(EventsController.odabraniEventBuilder.getDatumEventa().toString());
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
            String query = "UPDATE EVENTI SET NAZIV_EVENTA=?, POSJECENOST=?, CIJENA_KARTE=?, DATUM=? WHERE ID=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            EventsController.eventiList.remove(EventsController.odabraniEventBuilder);

            preparedStatement.setString(1, (newnaziv.equals("") ? EventsController.odabraniEventBuilder.getNazivEventa() : newnaziv));
            preparedStatement.setInt(2, (newposjecenost == 0 ? EventsController.odabraniEventBuilder.getPosjecenost() : newposjecenost));
            preparedStatement.setDouble(3, (newcijena == 0 ? EventsController.odabraniEventBuilder.getCijenaKarte() : newcijena));
            preparedStatement.setDate(4, (newDatum == null) ? (java.sql.Date) EventsController.odabraniEventBuilder.getDatumEventa() : newDatum);
            preparedStatement.setInt(5, EventsController.odabraniEventBuilder.getID());

            preparedStatement.executeUpdate();

            EventBuilder eventBuilder = new EventBuilder(EventsController.odabraniEventBuilder.getID(),
                    (newnaziv.equals("") ? EventsController.odabraniEventBuilder.getNazivEventa() : newnaziv),
                    (newposjecenost == 0 ? EventsController.odabraniEventBuilder.getPosjecenost() : newposjecenost),
                    (newcijena == 0 ? EventsController.odabraniEventBuilder.getCijenaKarte() : newcijena),
                    (newDatum == null) ? (java.sql.Date) EventsController.odabraniEventBuilder.getDatumEventa() : newDatum);

            EventsController.eventiList.add(eventBuilder);
            Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.PROMJENI, eventBuilder.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));
            EventsController.odabraniEventBuilder = null;

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
