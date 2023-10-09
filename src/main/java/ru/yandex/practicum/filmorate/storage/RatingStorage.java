package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingStorage {

    Rating addRating(Rating rating);

    Optional<Rating> removeRatingById(int ratingId);

    Optional<Rating> getRatingById(int ratingId);

    List<Rating> getAllRatings();

}
