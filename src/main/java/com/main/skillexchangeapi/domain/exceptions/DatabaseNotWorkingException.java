package com.main.skillexchangeapi.domain.exceptions;

public class DatabaseNotWorkingException extends Exception {
    public DatabaseNotWorkingException(String message) {
        super(message);
    }
}
