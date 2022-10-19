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

        // Here, we are performing join on each Completable Future one-by-one and if any of the CompletableFuture not yet completed it blocks the thread and waits.
        // We have an alternative to this, we can use allOf(), which make sure that code execution moves ahead only after
        // all completable Futures are completely executed.
        return movieFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public List<Movie> retrieveMoviesCompletableFutureAllOf(List<Long> movieIds) {
        var movieFutures = movieIds.stream()
                .map(this::retrieveMovieUsingCompletableFuture)
                .collect(Collectors.toList());

        // This function only completes after all the completableFutures passed to it completes.
        var completableFutureAllOf = CompletableFuture.allOf(movieFutures.toArray(new CompletableFuture[0]));

        return completableFutureAllOf
                .thenApply(v -> movieFutures
                        .stream()
                        // This join completes immediately since this code execution will only happen,
                        // after all the completableFutures executes completely.
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .join();

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
