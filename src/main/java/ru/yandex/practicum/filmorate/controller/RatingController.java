package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mpa")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/{ratingId}")
    public Optional<Rating> getRatingById(@PathVariable int ratingId) {
        return ratingService.getRatingById(ratingId);
    }

    @GetMapping
    public List<Rating> getAllGenres() {
        return ratingService.getAllRatings();
    }

}
