package com.example.tvzprojekt.Exceptions;

import com.example.tvzprojekt.Main;

public class UnpickedEntity extends Exception {
    public UnpickedEntity(String message) {
        super(message);
        Main.logger.error("UnpickedEntity -> nije odabran element iz liste");
    }
}
