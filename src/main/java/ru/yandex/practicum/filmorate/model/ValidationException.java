package ru.yandex.practicum.filmorate.model;

public class ValidationException extends Exception {

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }
}
