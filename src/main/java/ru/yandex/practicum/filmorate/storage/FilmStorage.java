package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film removeFilm(int filmId);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(int filmId);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    void checkIfFilmExists(int filmId);
}
