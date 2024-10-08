package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.Main;
import com.example.tvzprojekt.Model.StudentBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PitanjeStudent {

    @FXML
    public Button cancel;

    public void cancelDialog() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void input() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("inputStudenti.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(Main.getStage());
        dialogStage.setTitle("Input Studenata");
        dialogStage.getIcons().add(new Image("/icon.png"));

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
        cancelDialog();
    }

    public void output() throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Pisanje u tijeku");
        alert.setHeaderText(null);
        alert.setContentText("Pisanje u tijeku");

        String fileName = "studenti_" + System.currentTimeMillis() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (StudentBuilder studentBuilder : StudentiController.studentiList) {
                String line = String.join(",", studentBuilder.getJmbag(), studentBuilder.getIme(), studentBuilder.getPrezime(),
                        studentBuilder.getDatumRodenja().toString(), studentBuilder.getSmjer(), studentBuilder.getGodina().toString());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        alert.close();
        cancelDialog();
    }


}
