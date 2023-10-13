package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static int nextId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    private static int getNextId() {
        return nextId++;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        return films.put(film.getId(), film);
    }

    @Override
    public Film removeFilm(int filmId) {
        if (!films.containsKey(filmId)) {
            log.warn("No film found, the film has unknown ID: " + filmId);
            throw new IncorrectFilmIdException("Unknown film id: " + filmId);
        }
        return films.remove(filmId);
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.warn("Film update failed, the film has unknown ID: " + film.getId());
            throw new IncorrectFilmIdException("Unknown film id: " + film.getId());
        }
        return films.put(film.getId(),film);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int filmId) {
        if (!films.containsKey(filmId)) {
            log.warn("No film found, the film has unknown ID: " + filmId);
            throw new IncorrectFilmIdException("Unknown film id: " + filmId);
        }
        return films.get(filmId);
    }

    @Override
    public void addLike(int filmId, int userId) {
        if (films.containsKey(filmId)) {
            films.get(filmId).addLike(userId);
        }
    }

    @Override
    public void removeLike(int filmId, int userId) {
        if (films.containsKey(filmId)) {
            films.get(filmId).removeLike(userId);
        }
    }

    @Override
    public void checkIfFilmExists(int filmId) {
        if (!films.containsKey(filmId)) {
            throw new IncorrectFilmIdException("No film with id=" + filmId);
        }
    }
}
