package com.learnjava.apiclient;

import com.learnjava.domain.movie.Movie;
import com.learnjava.domain.movie.MovieInfo;
import com.learnjava.domain.movie.Review;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MoviesClient {

    private final WebClient webClient;

    public MoviesClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Movie retrieveMovie(Long movieId) {
        // MovieInfo
        var movieInfo = invokeMovieInfoService(movieId); // blocking call

        // Reviews
        var reviews = invokeReviewService(movieId);

        return new Movie(movieInfo, reviews);
    }

    public List<Movie> retrieveMovies(List<Long> movieIds) {
        return movieIds.stream()
                .map(this::retrieveMovie)
                .collect(Collectors.toList());
    }

    public List<Movie> retrieveMoviesCompletableFuture(List<Long> movieIds) {
        var movieFutures = movieIds.stream()
                .map(this::retrieveMovieUsingCompletableFuture)
                .collect(Collectors.toList());

        return movieFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public CompletableFuture<Movie> retrieveMovieUsingCompletableFuture(Long movieId) {
        // MovieInfo
        var movieInfoCompletableFuture = CompletableFuture.supplyAsync(() -> invokeMovieInfoService(movieId));

        // Reviews
        var reviewsCompletableFuture = CompletableFuture.supplyAsync(() -> invokeReviewService(movieId));

        return movieInfoCompletableFuture
                .thenCombine(reviewsCompletableFuture, Movie::new);
    }

    private MovieInfo invokeMovieInfoService(Long movieId) {
        // uri path: /v1/movie_infos/{id}
        var movieInfoUriPath = "/v1/movie_infos/{movieInfoId}";

        // Use bodyToMono in case of a single element returned from the server.
        return webClient
                .get()
                .uri(movieInfoUriPath, movieId)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .block();
    }

    private List<Review> invokeReviewService(Long movieId) {
        // /v1/reviews?movieInfoId=1
        var uri = UriComponentsBuilder.fromUriString("/v1/reviews")
                .queryParam("movieInfoId", movieId)
                .buildAndExpand()
                .toUriString();

        // Use bodyToFlux in case of multiple elements returned from the server.
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .block();
    }
}
