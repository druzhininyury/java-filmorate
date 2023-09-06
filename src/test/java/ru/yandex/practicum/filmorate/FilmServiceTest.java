package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {

    @Test
    public void filmValidationTest() {
        Film film;
        film = new Film("Name", "Description", LocalDate.of(1870, 1, 1), 90);
        assertFalse(FilmService.isFilmValid(film));
        film = new Film("Name", "Description", LocalDate.of(1895, 12, 28), 90);
        assertTrue(FilmService.isFilmValid(film));
    }
}
