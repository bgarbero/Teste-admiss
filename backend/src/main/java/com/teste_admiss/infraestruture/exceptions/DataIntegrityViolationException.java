package com.teste_admiss.infraestruture.exceptions;

public class DataIntegrityViolationException extends RuntimeException {

    public DataIntegrityViolationException(String message) {
        super(message);
    }

}
