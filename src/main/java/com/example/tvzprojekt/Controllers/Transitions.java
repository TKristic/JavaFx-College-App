package com.example.tvzprojekt.Controllers;

import com.example.tvzprojekt.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public abstract class Transitions {

    public void showStudentScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("studenti.fxml"));

        Scene scene = null;

        try{
            scene = new Scene(fxmlLoader.load());
        }catch(IOException e){
            e.printStackTrace();
        }

        Main.getStage().setScene(scene);
        Main.getStage().show();
    }

    public void showProfesoriScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("profesori.fxml"));

        Scene scene = null;

        try{
            scene = new Scene(fxmlLoader.load());
        }catch(IOException e){
            e.printStackTrace();
        }

        Main.getStage().setScene(scene);
        Main.getStage().show();
    }

    public void showDashboardScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));

        Scene scene = null;

        try{
            scene = new Scene(fxmlLoader.load());
        }catch(IOException e){
            e.printStackTrace();
        }

        Main.getStage().setScene(scene);
        Main.getStage().show();
    }

    public void showEventiScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("eventi.fxml"));

        Scene scene = null;

        try{
            scene = new Scene(fxmlLoader.load());
        }catch(IOException e){
            e.printStackTrace();
        }

        Main.getStage().setScene(scene);
        Main.getStage().show();
    }

    public void showTransakcijeScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("transakcije.fxml"));

        Scene scene = null;

        try{
            scene = new Scene(fxmlLoader.load());
        }catch(IOException e){
            e.printStackTrace();
        }

        Main.getStage().setScene(scene);
        Main.getStage().show();
    }

    public void showPromjeneScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("promjene.fxml"));

        Scene scene = null;

        try{
            scene = new Scene(fxmlLoader.load());
        }catch(IOException e){
            e.printStackTrace();
        }

        Main.getStage().setScene(scene);
        Main.getStage().show();
    }

    public void showTecajScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("tecajnaLista.fxml"));
        Scene scene = null;

        try{
            scene = new Scene(fxmlLoader.load()); // tu se lomi program
        }catch(IOException e){
            e.printStackTrace();
        }

        Main.getStage().setScene(scene);
        Main.getStage().show();
    }


    public void Odjava() {
        System.exit(0);
    }
}
