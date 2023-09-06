package ru.yandex.practicum.filmorate.storage;

public class IncorrectFilmIdException extends RuntimeException {

    public IncorrectFilmIdException() {
        super();
    }

    public IncorrectFilmIdException(String message) {
        super(message);
    }

}
