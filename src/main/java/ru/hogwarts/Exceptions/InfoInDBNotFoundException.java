package ru.hogwarts.Exceptions;

public class InfoInDBNotFoundException extends RuntimeException{
    public InfoInDBNotFoundException() {
    }

    public InfoInDBNotFoundException(String message) {
        super(message);
    }

    public InfoInDBNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfoInDBNotFoundException(Throwable cause) {
        super(cause);
    }

    public InfoInDBNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
