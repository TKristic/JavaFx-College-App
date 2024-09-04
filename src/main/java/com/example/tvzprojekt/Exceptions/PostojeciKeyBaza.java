package com.example.tvzprojekt.Exceptions;

import com.example.tvzprojekt.Main;

public class PostojeciKeyBaza extends RuntimeException {
    public PostojeciKeyBaza(String message) {
        super(message);
        Main.logger.error("PostojeciKeyBaza -> Unesen je key duplikat iz baze");
    }
}
