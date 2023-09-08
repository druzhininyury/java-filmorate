package ru.yandex.practicum.filmorate.storage;

public class IncorrectUserIdException extends RuntimeException {
    public IncorrectUserIdException() {
        super();
    }

    public IncorrectUserIdException(String message) {
        super(message);
    }
}
