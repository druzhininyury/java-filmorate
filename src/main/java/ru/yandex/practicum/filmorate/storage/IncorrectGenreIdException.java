package ru.yandex.practicum.filmorate.storage;

public class IncorrectGenreIdException extends RuntimeException {
    public IncorrectGenreIdException() {
        super();
    }

    public IncorrectGenreIdException(String message) {
        super(message);
    }
}
