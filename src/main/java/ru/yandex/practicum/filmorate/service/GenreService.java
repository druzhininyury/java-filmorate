package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre addGenre(Genre genre) {
        return genreStorage.addGenre(genre);
    }

    public Optional<Genre> removeGenreById(int genreId) {
        return genreStorage.removeGenreById(genreId);
    }

    public Optional<Genre> getGenreById(int genreId) {
        return genreStorage.getGenreById(genreId);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }
}
