package com.api.holidays.exception;

public class CountryValidationException extends RuntimeException{

    public CountryValidationException(String message) {
        super(message);
    }
}
