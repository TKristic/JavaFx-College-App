package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.IspisPromjene;
import com.example.tvzprojekt.Model.StatusiPromjene;
import com.example.tvzprojekt.Model.TransakcijaBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ModifyTransakcijaController implements DialogControls {

    @FXML
    public Label labIznos;
    @FXML
    public Label labopis;
    @FXML
    public Label labdatum;
    @FXML
    public TextField valiznos;
    @FXML
    public TextField valopis;
    @FXML
    public DatePicker valdatum;
    @FXML
    public Button accept;
    @FXML
    public Button cancel;

    public void initialize() {
        labIznos.setText(String.valueOf(TransakcijeController.odabranaTransakcijaBuilder.getIznos()));
        labopis.setText(TransakcijeController.odabranaTransakcijaBuilder.getOpisPlacanja());
        labdatum.setText(TransakcijeController.odabranaTransakcijaBuilder.getDatumTransakcije().toString());
    }

    @Override
    public void acceptDialog() {
        double newiznos;
        try {
            newiznos = Double.parseDouble(valiznos.getText());
        } catch (NumberFormatException e) {
            if (valiznos.getText().equals("")) newiznos = 0;
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText(null);
                alert.setContentText("Iznos mora biti brojčana vrijednost");
                alert.showAndWait();
                return;
            }
        }

        String newOpis = valopis.getText();
        LocalDate newDatum;
        try {
            newDatum = Date.valueOf(valdatum.getValue()).toLocalDate();
        } catch (NullPointerException e) {
            newDatum = null;
        }

        TransakcijeController.transakcijeList.remove(TransakcijeController.odabranaTransakcijaBuilder);

        try(Connection connection = DatabaseConnector.getConnection()) {
            String query = "UPDATE TRANSAKCIJE SET IZNOS=?, OPIS=?, DATUM=? WHERE ID_TRANSAKCIJA=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setDouble(1, (newiznos == 0 ? TransakcijeController.odabranaTransakcijaBuilder.getIznos() : newiznos));
            preparedStatement.setString(2, (newOpis.equals("") ? TransakcijeController.odabranaTransakcijaBuilder.getOpisPlacanja() : newOpis));
            preparedStatement.setDate(3, (newDatum == null) ? Date.valueOf(TransakcijeController.odabranaTransakcijaBuilder.getDatumTransakcije()) : Date.valueOf(newDatum));
            preparedStatement.setInt(4, TransakcijeController.odabranaTransakcijaBuilder.getID());

            TransakcijaBuilder transakcijaBuilder = new TransakcijaBuilder(
                    TransakcijeController.odabranaTransakcijaBuilder.getID(),
                    (newiznos == 0 ? TransakcijeController.odabranaTransakcijaBuilder.getIznos() : newiznos),
                    (newOpis.equals("") ? TransakcijeController.odabranaTransakcijaBuilder.getOpisPlacanja() : newOpis),
                    ((newDatum == null) ? Date.valueOf(TransakcijeController.odabranaTransakcijaBuilder.getDatumTransakcije()) : Date.valueOf(newDatum)).toLocalDate(),
                    TransakcijeController.odabranaTransakcijaBuilder.getJmbag(),
                    TransakcijeController.odabranaTransakcijaBuilder.getTipKorisnika());

            TransakcijeController.transakcijeList.add(transakcijaBuilder);

            Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.PROMJENI, transakcijaBuilder.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));
            TransakcijeController.odabranaTransakcijaBuilder = null;

            preparedStatement.executeUpdate();

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
