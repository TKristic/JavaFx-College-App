package com.example.tvzprojekt.Exceptions;

import com.example.tvzprojekt.Main;

public class PraznoPolje extends Exception {
    public PraznoPolje(String message) {
        super(message);
        Main.logger.error("PraznoPolje -> nisu ispunjena sva input polja");
    }
}
