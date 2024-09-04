package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Exceptions.JMBAGInputException;
import com.example.tvzprojekt.Exceptions.PraznoPolje;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.IspisPromjene;
import com.example.tvzprojekt.Model.StatusiPromjene;
import com.example.tvzprojekt.Model.TransakcijaBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTransakcijaController implements DialogControls{

    @FXML
    public TextField iznos;
    @FXML
    public TextField opis;
    @FXML
    public DatePicker datum;
    @FXML
    public TextField jmbag;

    @FXML
    public Button submit;
    @FXML
    public Button cancel;

    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void acceptDialog() {
        try(Connection connection = DatabaseConnector.getConnection()) {
            String query = "INSERT INTO TRANSAKCIJE(IZNOS, OPIS, DATUM, JMBAG, TIP) VALUES(?, ?, ?, ?, ?)";

            double iznosVal = Double.parseDouble(iznos.getText());
            String opisVal = opis.getText();
            LocalDate datumVal = datum.getValue();
            String jmbagVal = jmbag.getText();
            String tip;

            provjeraInputJmbaga(jmbagVal);
            provjeraStringova(opisVal);
            provjeraNullVrijednosti(datumVal);



            PreparedStatement preparedStatement = connection.prepareStatement(query);
            PreparedStatement queryStatement = connection.prepareStatement("SELECT JMBAG, GODINA\n" +
                    "FROM STUDENTI\n" +
                    "WHERE JMBAG = '" +  jmbagVal + "'\n" +
                    "UNION\n" +
                    "SELECT JMBAG, SMJER\n" +
                    "FROM PROFESORI\n" +
                    "WHERE JMBAG = '" +  jmbagVal + "';\n");

            preparedStatement.setDouble(1, iznosVal);
            preparedStatement.setString(2, opisVal);
            preparedStatement.setDate(3, java.sql.Date.valueOf(datumVal));
            preparedStatement.setString(4, jmbagVal);

            ResultSet resultSet = queryStatement.executeQuery();

            if(resultSet.next()) {
                try {
                    Integer.parseInt(resultSet.getString(2));
                    preparedStatement.setString(5, "Student");
                    tip = "Student";
                } catch (NumberFormatException e) {
                    preparedStatement.setString(5, "Profesor");
                    tip = "Profesor";
                }
            } else {
                Main.logger.error("Navedeni JMBAG ne postoji u bazi");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText(null);
                alert.setContentText("Navedeni JMBAG ne postoji u bazi");
                alert.showAndWait();
                return;
            }


            TransakcijaBuilder transakcijaBuilder = new TransakcijaBuilder(TransakcijeController.transakcijeList.size() + 1,
                    iznosVal, opisVal, datumVal, jmbagVal, tip);
            TransakcijeController.transakcijeList.add(transakcijaBuilder);

            preparedStatement.executeUpdate();
            Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.DODAJ, transakcijaBuilder.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e1) {
            Main.logger.error("Unesen krivi format (treba biti broj)");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Iznos mora biti broj");
            alert.showAndWait();
        } catch (PraznoPolje | NullPointerException e2) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Unesite sve vrijednosti prije submita");
            alert.showAndWait();
        } catch (JMBAGInputException e4) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Unos za JMBAG mora biti 10 brojčanih znakova");
            alert.showAndWait();
        }

    }

    private void provjeraStringova(String str1) throws PraznoPolje {
        if (str1.isEmpty()) throw new PraznoPolje("Nisu unesene vrijednosti");
    }

    private void provjeraNullVrijednosti(LocalDate date) throws PraznoPolje{
        if (date == null) throw new PraznoPolje("Nisu unesene vrijednosti");
    }

    private void provjeraInputJmbaga(String unos) throws JMBAGInputException {
        String regex = "\\d{10}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(unos);

        if (!matcher.matches()) throw new JMBAGInputException("Unos ne odgovara JMBAG kriteriju");
    }

}
