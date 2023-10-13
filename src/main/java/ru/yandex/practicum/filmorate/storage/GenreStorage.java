package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    Genre addGenre(Genre genre);

    Optional<Genre> removeGenreById(int genreId);

    Optional<Genre> getGenreById(int genreId);

    List<Genre> getAllGenres();

}
