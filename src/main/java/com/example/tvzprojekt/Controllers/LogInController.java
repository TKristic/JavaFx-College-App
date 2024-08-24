package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.Privilegije;
import com.example.tvzprojekt.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
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
        Set<User> users = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("korisnici.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length == 3) {
                    String username = userDetails[0];
                    String passwordHash = userDetails[1];
                    boolean isAdmin = Boolean.parseBoolean(userDetails[2]);

                    User user = new User(username, passwordHash, isAdmin ? Privilegije.ADMINISTAROR : Privilegije.USER);
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
                    Main.initUser(unesenUser, unesenPass, user.getStatus());
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


}
