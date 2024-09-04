package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.Institucija;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegisterController implements DialogControls{

    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public CheckBox admin;
    @FXML
    public Button accept;
    @FXML
    public Button cancel;
    @FXML
    public ChoiceBox<Institucija> ustanova;
    private static final String kljuc = "sdfghjklgzfesrdgthjkiugzfdtrgzhujiokpjiugfdtrzgu";
    private static final String link = "https://fujcisto.com//api.php";
    public void initialize() throws IOException{
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

                List<Institucija> institucije = new ArrayList<>();

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

    @Override
    public void acceptDialog() {
        String inputUser = username.getText();
        String inputPass = password.getText();
        Institucija odabranaUstanova = (Institucija) ustanova.getValue();
        boolean isAdmin = admin.isSelected();

        if (inputUser.equals("") || inputPass.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Oba polja moraju biti popunjena");
            alert.showAndWait();
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("korisnici.txt", true))) {
            String line = String.join(",", inputUser, BCrypt.hashpw(inputPass, BCrypt.gensalt()), Boolean.toString(isAdmin), odabranaUstanova.toString());
            writer.write(line);
            writer.newLine();
            Main.logger.info(inputUser + " je registriran");
        } catch (IOException e) {
            e.printStackTrace();
        }


        cancelDialog();
    }

    @Override
    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
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
