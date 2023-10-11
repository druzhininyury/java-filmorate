package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film removeFilm(int filmId);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(int filmId);

    public void addLike(int filmId, int userId);

    public void removeLike(int filmId, int userId);

    public void checkIfFilmExists(int filmId);
}
