package com.example.tvzprojekt.Exceptions;

import com.example.tvzprojekt.Main;

public class JMBAGInputException extends RuntimeException {

    public JMBAGInputException(String message) {
        super(message);
        Main.logger.error("JMBAGInputException -> unesen krivi format JMBAG-a");
    }
}
