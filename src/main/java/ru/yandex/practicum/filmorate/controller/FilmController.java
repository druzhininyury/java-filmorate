package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private static int nextId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getId() != 0) {
            log.warn("Film addition failed: the film has already ID.");
            throw new ValidationException("The film has already ID.");
        }
        if (!isFilmValid(film)) {
            log.warn("Film addition failed: film parameters are incorrect");
            throw new ValidationException("Film parameters are incorrect.");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Film added: " + film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getId() <= 0) {
            log.warn("Film update failed: the film has no ID.");
            throw new ValidationException("The film has no ID.");
        }
        if (!isFilmValid(film)) {
            log.warn("Film update failed: film parameters are incorrect");
            throw new ValidationException("Film parameters are incorrect");
        }
        if (!films.containsKey(film.getId())) {
            log.warn("Film update failed: the film has unknown ID.");
            throw new ValidationException("The film has unknown ID.");
        }
        films.put(film.getId(), film);
        log.info("Film added: " + film);
        return film;
    }

    public static boolean isFilmValid(Film film) {
        LocalDate releaseDate = film.getReleaseDate();
        if (releaseDate != null && !releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            return true;
        } else {
            return false;
        }
    }

    private int getNextId() {
        return nextId++;
    }

}
