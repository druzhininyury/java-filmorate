package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ValidationException;
import ru.yandex.practicum.filmorate.storage.IncorrectFilmIdException;
import ru.yandex.practicum.filmorate.storage.IncorrectGenreIdException;
import ru.yandex.practicum.filmorate.storage.IncorrectRatingIdException;
import ru.yandex.practicum.filmorate.storage.IncorrectUserIdException;

import java.util.Map;

@RestControllerAdvice(basePackages = "ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationExceptionHandler(final ValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> incorrectUserIdExceptionHandler(final IncorrectUserIdException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> incorrectFilmIdExceptionHandler(final IncorrectFilmIdException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> incorrectGenreIdExceptionHandler(final IncorrectGenreIdException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> incorrectRatingIdExceptionHandler(final IncorrectRatingIdException e) {
        return Map.of("error", e.getMessage());
    }
}
