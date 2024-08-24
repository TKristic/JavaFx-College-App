package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Exceptions.JMBAGInputException;
import com.example.tvzprojekt.Exceptions.PostojeciKeyBaza;
import com.example.tvzprojekt.Exceptions.PraznoPolje;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.IspisPromjene;
import com.example.tvzprojekt.Model.Promjena;
import com.example.tvzprojekt.Model.StatusiPromjene;
import com.example.tvzprojekt.Model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StudentiDialogController implements DialogControls {

    @FXML
    public TextField jmbag;
    @FXML
    public TextField ime;
    @FXML
    public TextField prezime;
    @FXML
    public DatePicker datum;
    @FXML
    public ChoiceBox smjer;
    @FXML
    public TextField godina;
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
            String query = "INSERT INTO STUDENTI(JMBAG, IME, PREZIME, DATUM, SMJER, GODINA) VALUES(?, ? ,? ,?, ?, ?)";
            String update = "INSERT INTO ENTITETI(JMBAG, TIP) VALUES(?, ?)";

            String jmbagVal = jmbag.getText();
            String imeVal = ime.getText();
            String prezimeVal = prezime.getText();
            LocalDate datumVal = datum.getValue();
            String smjerVal = (String) smjer.getValue();
            int godinaVal = Integer.parseInt(godina.getText());

            provjeraInputJmbaga(jmbagVal);
            provjeraStringova(jmbagVal, imeVal, prezimeVal, smjerVal);
            provjeraNullVrijednosti(datumVal);
            provjeraBazeZaKljuceve(connection, jmbagVal);

            PreparedStatement updateStatement = connection.prepareStatement(update);
            updateStatement.setString(1, jmbagVal);
            updateStatement.setString(2, "Student");
            updateStatement.executeUpdate();

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, jmbagVal);
            preparedStatement.setString(2, imeVal);
            preparedStatement.setString(3, prezimeVal);
            preparedStatement.setDate(4, Date.valueOf(datumVal));
            preparedStatement.setString(5, smjerVal);
            preparedStatement.setInt(6, godinaVal);

            preparedStatement.executeUpdate();

            Student student = new Student(jmbagVal, imeVal, prezimeVal, datumVal, smjerVal, godinaVal);

            StudentiController.studentiList.add(student);
            Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.DODAJ, student.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Godina mora biti cijeli broj");
            alert.showAndWait();
        } catch (PraznoPolje | NullPointerException e2) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Unesite sve vrijednosti prije submita");
            alert.showAndWait();
        } catch (PostojeciKeyBaza e3) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Već postoji student s tim JMBAG-om");
            alert.showAndWait();
        } catch (JMBAGInputException e4) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Unos za JMBAG mora biti 10 brojčanih znakova");
            alert.showAndWait();
        }

    }

    private void provjeraStringova(String str1, String str2, String str3, String str4) throws PraznoPolje {
        if (str1.isEmpty() || str2.isEmpty() || str3.isEmpty() || str4.isEmpty()) throw new PraznoPolje("Nisu unesene vrijednosti");
    }

    private void provjeraNullVrijednosti(LocalDate date) throws PraznoPolje{
        if (date == null) throw new PraznoPolje("Nisu unesene vrijednosti");
    }

    private void provjeraBazeZaKljuceve(Connection connection, String key) throws PostojeciKeyBaza {
        String query = "SELECT JMBAG FROM ENTITETI";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                if (set.getString("JMBAG").equals(key)) throw new PostojeciKeyBaza("JMBAG vec postoji u bazi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void provjeraInputJmbaga(String unos) throws JMBAGInputException {
        String regex = "\\d{10}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(unos);

        if (!matcher.matches()) throw new JMBAGInputException("Unos ne odgovara JMBAG kriteriju");
    }

}
