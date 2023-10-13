package ru.yandex.practicum.filmorate.storage;

public class IncorrectRatingIdException extends RuntimeException {
    public IncorrectRatingIdException() {
        super();
    }

    public IncorrectRatingIdException(String message) {
        super(message);
    }
}
