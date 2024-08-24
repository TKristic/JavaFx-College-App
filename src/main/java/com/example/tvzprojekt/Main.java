package com.example.tvzprojekt;

import com.example.tvzprojekt.Controllers.PromjeneController;
import com.example.tvzprojekt.Model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class Main extends Application {

    private static Stage primaryStage;
    private static User currentUser;
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage stage) throws IOException {


        setPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("logIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 300);

        getPrimaryStage().setScene(scene);
        getPrimaryStage().setTitle("Prijava");
        getPrimaryStage().setResizable(false);
        getPrimaryStage().getIcons().add(new Image("/icon.png"));
        getPrimaryStage().show();
    }

    public static void main(String[] args) {
        launch();
    }

    public synchronized static Stage getStage(){
        return getPrimaryStage();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Main.primaryStage = primaryStage;
    }

    public static void loggedIn(Stage stage) throws IOException {
        setPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

        getPrimaryStage().setScene(scene);
        getPrimaryStage().setTitle("TVZ Projekt");
        getPrimaryStage().setResizable(false);
        getPrimaryStage().getIcons().add(new Image("/icon.png"));
        getPrimaryStage().show();
    }

    public static void initUser(String username, String password, Privilegije privilegija) {
        currentUser = new User(username, password, privilegija);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isAdmin() {
        return  (currentUser.getStatus() == Privilegije.ADMINISTAROR) ? true : false;
    }

    public static void zapisivanjePromjene(IspisPromjene promjena) {

        String datoteka = "promjene.bat";

        Thread thread = new Thread(() -> {
            if (new File(datoteka).exists()) {
                ObservableList<IspisPromjene> list = PromjeneController.readPromjeneFromFile();
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(datoteka))) {

                    for (IspisPromjene ispis : list) {
                        oos.writeObject(ispis);
                    }

                    oos.writeObject(promjena);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(datoteka))) {

                    oos.writeObject(promjena);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        thread.start();

    }
}