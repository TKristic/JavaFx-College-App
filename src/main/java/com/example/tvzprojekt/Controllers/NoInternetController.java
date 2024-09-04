package com.example.tvzprojekt.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NoInternetController implements DialogControls{
    @FXML
    public Button accept;

    @Override
    public void acceptDialog() {
        System.exit(0);
    }

    @Override
    public void cancelDialog() {

    }
}
