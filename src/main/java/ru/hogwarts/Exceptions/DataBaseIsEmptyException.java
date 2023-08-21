package ru.hogwarts.Exceptions;

public class DataBaseIsEmptyException extends RuntimeException{
    public DataBaseIsEmptyException() {
    }

    public DataBaseIsEmptyException(String message) {
        super(message);
    }

    public DataBaseIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataBaseIsEmptyException(Throwable cause) {
        super(cause);
    }

    public DataBaseIsEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
