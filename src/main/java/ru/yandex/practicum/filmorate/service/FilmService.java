package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public static boolean isFilmValid(Film film) {
        LocalDate releaseDate = film.getReleaseDate();
        return releaseDate != null && !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }

    public Film addFilm(Film film) throws ValidationException {
        if (!isFilmValid(film)) {
            log.warn("Film addition failed: film parameters are incorrect");
            throw new ValidationException("Film parameters are incorrect.");
        }
        filmStorage.addFilm(film);
        log.info("Film added: " + film);
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        if (!isFilmValid(film)) {
            log.warn("Film update failed: film parameters are incorrect");
            throw new ValidationException("Film parameters are incorrect");
        }
        filmStorage.updateFilm(film);
        log.info("Film updated: " + film);
        return filmStorage.getFilmById(film.getId());
    }

    public Film removeFilm(int filmId) {
        return filmStorage.removeFilm(filmId);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Film addFilmLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film removeFilmLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getFilmsTop(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }
}
