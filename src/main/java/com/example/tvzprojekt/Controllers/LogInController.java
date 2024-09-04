package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.Institucija;
import com.example.tvzprojekt.Model.Privilegije;
import com.example.tvzprojekt.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LogInController {

    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public Button accept;
    @FXML
    public Button register;
    @FXML
    public ChoiceBox<Institucija> ustanova;
    private static final String kljuc = "sdfghjklgzfesrdgthjkiugzfdtrgzhujiokpjiugfdtrzgu";
    private static final String link = "https://fujcisto.com//api.php";

    public void initialize() throws IOException {
        try {
            URL url = new URL(link + "?api_key=" + kljuc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                // Parsiraj JSON koristeći JSON Simple
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(response.toString());

                JSONArray institucijeArray = (JSONArray) jsonObject.get("institucije");

                Set<Institucija> institucije = new HashSet<>();

                for (Object obj : institucijeArray) {
                    JSONObject institucijaJson = (JSONObject) obj;

                    String naziv = (String) institucijaJson.get("naziv");
                    String skracenica = (String) institucijaJson.get("skraćenica");
                    String adresa = (String) institucijaJson.get("adresa");
                    String webStranica = (String) institucijaJson.get("web_stranica");

                    Institucija institucija = new Institucija(naziv, skracenica, adresa, webStranica);
                    institucije.add(institucija);
                }
                // Napuni ChoiceBox s institucijama
                ustanova.getItems().addAll(institucije);

            } else {
                System.out.println("GET request failed.");
            }
        } catch (Exception e) {
            noInternet();
        }
    }


    public void acceptDialog() {
        if (username.getText().equals("") || password.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Username i/ili password polja su prazna");
            alert.showAndWait();
            return;
        }

        String unesenUser = username.getText();
        String unesenPass = password.getText();
        Institucija odabranaUstanova = (Institucija) ustanova.getValue();
        Set<User> users = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("korisnici.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length == 4) {
                    String username = userDetails[0];
                    String passwordHash = userDetails[1];
                    boolean isAdmin = Boolean.parseBoolean(userDetails[2]);
                    String fakultet = userDetails[3];

                    User user = new User(username, passwordHash, isAdmin ? Privilegije.ADMINISTAROR : Privilegije.USER,  fakultet);
                    users.add(user);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean isLogged = false;

        for(User user : users) {
            if (BCrypt.checkpw(unesenPass, user.getHash())) {
                if (unesenUser.equals(user.getUsername())) {

                    if(odabranaUstanova.toString().equals(user.getUstanova())) {
                        Main.initUser(unesenUser, unesenPass, user.getStatus(), odabranaUstanova.toString());
                        try {
                            isLogged = true;
                            Main.logger.info(Main.getCurrentUser().getUsername() + " se ulogirao");
                            Main.loggedIn(Main.getPrimaryStage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }
        }

        if (!isLogged) {
            Main.logger.error("Neuspješna prijava");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Neuspješna prijava, probajte ponovno");
            alert.showAndWait();
        }
    }

    public void registerDialog() throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("registerDialog.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Registracija");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public void noInternet() throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("noInternet.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Nema Interneta");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }


}
