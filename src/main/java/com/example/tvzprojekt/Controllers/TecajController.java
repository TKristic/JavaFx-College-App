package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.Transakcija;
import com.example.tvzprojekt.Model.Valuta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TecajController extends Transitions {
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
    public TableView<Valuta> tecajTab;
    @FXML
    public TableColumn<Valuta, Integer> sifra;
    @FXML
    public TableColumn<Valuta, Double> drzava;
    @FXML
    public TableColumn<Valuta, String> kupovni;
    @FXML
    public TableColumn<Valuta, Date> prodajni;
    @FXML
    public TableColumn<Valuta, String> srednji;
    @FXML
    public TableColumn<Valuta, String> datum;
    public static ObservableList<Valuta> tecajList;

    @FXML
    public void initialize() {
        korisnik.setText("Korisnik : " + Main.getCurrentUser().getUsername());
        status.setText("Status : " + Main.getCurrentUser().getStatus().toString().toLowerCase());

        tecajTab.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        sifra.setCellValueFactory(new PropertyValueFactory<>("sifraValute"));
        drzava.setCellValueFactory(new PropertyValueFactory<>("drzava"));
        kupovni.setCellValueFactory(new PropertyValueFactory<>("kupovniTecaj"));
        prodajni.setCellValueFactory(new PropertyValueFactory<>("prodajniTecaj"));
        srednji.setCellValueFactory(new PropertyValueFactory<>("srednjiTecaj"));
        datum.setCellValueFactory(new PropertyValueFactory<>("datumPrimjene"));

        String url = "https://api.hnb.hr/tecajn-eur/v3"; // URL za HNB API
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());

                JSONParser parser = new JSONParser();

                try {
                    JSONArray jsonArray = (JSONArray) parser.parse(jsonResponse);
                    List<Valuta> valute = new ArrayList<>();

                    for (Object obj : jsonArray) {
                        JSONObject jsonObject = (JSONObject) obj;

                        String datumPrimjene = (String) jsonObject.get("datum_primjene");
                        String drzava = (String) jsonObject.get("drzava");
                        double kupovniTecaj = Double.parseDouble(((String) jsonObject.get("kupovni_tecaj")).replace(",", "."));
                        double prodajniTecaj = Double.parseDouble(((String) jsonObject.get("prodajni_tecaj")).replace(",", "."));
                        String sifraValute = (String) jsonObject.get("sifra_valute");
                        double srednjiTecaj = Double.parseDouble(((String) jsonObject.get("srednji_tecaj")).replace(",", "."));

                        Valuta valutaObj = new Valuta(datumPrimjene, drzava, kupovniTecaj, prodajniTecaj, sifraValute, srednjiTecaj);
                        tecajList.add(valutaObj);
                    }

                    tecajTab.setItems(tecajList);

                } catch (org.json.simple.parser.ParseException e) {
                    throw new RuntimeException(e);
                }


            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
