package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Exceptions.PraznoPolje;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AddEventController implements DialogControls {

    @FXML
    public TextField naziv;
    @FXML
    public TextField cijena;
    @FXML
    public DatePicker datum;

    @FXML
    public TableView<Entitet> odsutni;
    @FXML
    public TableView<Entitet> prisutni;
    @FXML
    public TableColumn<Entitet, String> ime1;
    @FXML
    public TableColumn<Entitet, String> prezime1;
    @FXML
    public TableColumn<Entitet, String> smjer1;
    @FXML
    public TableColumn<Entitet, String> uloga1;
    @FXML
    public TableColumn<Entitet, String> ime2;
    @FXML
    public TableColumn<Entitet, String> prezime2;
    @FXML
    public TableColumn<Entitet, String> smjer2;
    @FXML
    public TableColumn<Entitet, String> uloga2;

    public static ObservableList<Entitet> odsutniList;
    public static ObservableList<Entitet> prisutniList;
    public static Entitet odabraniOdsutnik;
    public static Entitet odabraniPrisutnik;
    @FXML
    public Button submit;
    @FXML
    public Button cancel;

    @FXML
    public void initialize() {
        try(Connection connection = DatabaseConnector.getConnection()) {
            String queryStudenti = "SELECT * FROM STUDENTI";
            String queryProfesori = "SELECT * FROM PROFESORI";

            PreparedStatement studentiStatement = connection.prepareStatement(queryStudenti);
            PreparedStatement profesoriStatement = connection.prepareStatement(queryProfesori);


            ime1.setCellValueFactory(new PropertyValueFactory<>("ime"));
            prezime1.setCellValueFactory(new PropertyValueFactory<>("prezime"));
            smjer1.setCellValueFactory(new PropertyValueFactory<>("smjer"));
            uloga1.setCellValueFactory(new PropertyValueFactory<>("tip"));

            ime2.setCellValueFactory(new PropertyValueFactory<>("ime"));
            prezime2.setCellValueFactory(new PropertyValueFactory<>("prezime"));
            smjer2.setCellValueFactory(new PropertyValueFactory<>("smjer"));
            uloga2.setCellValueFactory(new PropertyValueFactory<>("tip"));

            odsutniList = FXCollections.observableArrayList();
            prisutniList = FXCollections.observableArrayList();

            ResultSet studentiSet = studentiStatement.executeQuery();
            ResultSet profesoriSet = profesoriStatement.executeQuery();

            while (studentiSet.next()) {
                odsutniList.add(new Student(studentiSet.getString(2),
                        studentiSet.getString(3),
                        studentiSet.getString(4),
                        studentiSet.getDate(5).toLocalDate(),
                        studentiSet.getString(6),
                        studentiSet.getInt(7)));
            }

            while (profesoriSet.next()) {
                odsutniList.add(new Profesor(profesoriSet.getString(2),
                        profesoriSet.getString(3),
                        profesoriSet.getString(4),
                        profesoriSet.getDate(5).toLocalDate(),
                        profesoriSet.getString(6)));
            }

            odsutni.setItems(odsutniList);
            prisutni.setItems(prisutniList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void acceptDialog() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "INSERT INTO EVENTI(NAZIV_EVENTA, POSJECENOST, CIJENA_KARTE, DATUM) VALUES(?, ?, ?, ?)";

            String nazivVal = naziv.getText();
            double cijenaVal = Double.parseDouble(cijena.getText());
            LocalDate datumVal = datum.getValue();

            provjeraStringova(nazivVal);
            provjeraNullVrijednosti(datumVal);

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nazivVal);
            preparedStatement.setInt(2, prisutniList.size());
            preparedStatement.setDouble(3, cijenaVal);
            preparedStatement.setDate(4, java.sql.Date.valueOf(datumVal));

            preparedStatement.executeUpdate();

            Event event = new Event(EventsController.eventiList.size() + 1, nazivVal, prisutniList.size(), cijenaVal, java.sql.Date.valueOf(datumVal));
            EventsController.eventiList.add(event);
            Main.zapisivanjePromjene(new IspisPromjene(StatusiPromjene.DODAJ, event.getClass().getSimpleName(), Main.getCurrentUser().getUsername(), LocalDateTime.now()));

            for (Entitet element : prisutniList) {
                String update = "INSERT INTO TRANSAKCIJE(IZNOS, OPIS, DATUM, JMBAG, TIP) VALUES(?, ?, ?, ?, ?)";

                PreparedStatement statement = connection.prepareStatement(update);
                statement.setDouble(1, cijenaVal);
                statement.setString(2, nazivVal);
                statement.setDate(3, Date.valueOf(datumVal));
                statement.setString(4, element.getJmbag());
                statement.setString(5, element.getTip());

                statement.executeUpdate();
            }

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
        }
    }

    private void provjeraNullVrijednosti(LocalDate date) throws PraznoPolje{
        if (date == null) throw new PraznoPolje("Nisu unesene vrijednosti");
    }

    private void provjeraStringova(String str1) throws PraznoPolje {
        if (str1.isEmpty()) throw new PraznoPolje("Nisu unesene vrijednosti");
    }

    public void dodajNaListu() {
        odabraniOdsutnik = odsutni.getSelectionModel().getSelectedItem();
        
        if (odabraniOdsutnik != null) {
            prisutniList.add(odabraniOdsutnik);
            //prisutni.setItems(prisutniList);
            odsutniList.remove(odabraniOdsutnik);
            //odsutni.setItems(odsutniList);
            odabraniOdsutnik = null;
        } else {
            Main.logger.error("UnpickedEntity -> nije odabran element iz liste");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Prvo odaberite entitet iz liste za tu akciju");
            alert.showAndWait();
        }
    }

    public void makniSListe() {
        odabraniPrisutnik = prisutni.getSelectionModel().getSelectedItem();

        if (odabraniPrisutnik != null) {
            odsutniList.add(odabraniPrisutnik);
            //odsutni.setItems(odsutniList);
            prisutniList.remove(odabraniPrisutnik);
            //prisutni.setItems(prisutniList);
            odabraniPrisutnik = null;
        } else {
            Main.logger.error("UnpickedEntity -> nije odabran element iz liste");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Prvo odaberite entitet iz liste za tu akciju");
            alert.showAndWait();
        }
    }
}
