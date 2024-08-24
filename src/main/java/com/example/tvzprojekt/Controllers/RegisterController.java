package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.DatabaseConnector;
import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.Profesor;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    @Override
    public void acceptDialog() {
        String inputUser = username.getText();
        String inputPass = password.getText();
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
            String line = String.join(",", inputUser, BCrypt.hashpw(inputPass, BCrypt.gensalt()), Boolean.toString(isAdmin));
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
}
