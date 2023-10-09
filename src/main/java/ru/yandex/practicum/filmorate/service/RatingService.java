package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingStorage ratingStorage;

    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public Rating addRating(Rating rating) {
        return ratingStorage.addRating(rating);
    }

    public Optional<Rating> removeRatingById(int ratingId) {
        return ratingStorage.removeRatingById(ratingId);
    }

    public Optional<Rating> getRatingById(int ratingId) {
        return ratingStorage.getRatingById(ratingId);
    }

    public List<Rating> getAllRatings() {
        return ratingStorage.getAllRatings();
    }

}
