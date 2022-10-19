package com.learnjava.apiclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.stopWatchReset;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MoviesClientTest {

    WebClient webClient = WebClient
            .builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    private final MoviesClient moviesClient = new MoviesClient(webClient);

    @BeforeEach
    void setUp() {
        stopWatchReset();
    }

    @RepeatedTest(10)
    void retrieveMovie() {
        long movieId = 2;

        startTimer();
        var movie = moviesClient.retrieveMovie(movieId);
        timeTaken();

        System.out.println(movie);

        assertNotNull(movie);
        assertEquals(1, movie.getReviewList().size());
        assertEquals("The Dark Knight", movie.getMovieInfo().getName());
    }

    @RepeatedTest(10)
    void retrieveMovieUsingCompletableFuture() {
        long movieId = 3;

        startTimer();
        var movie = moviesClient.retrieveMovieUsingCompletableFuture(movieId)
                .join();
        timeTaken();

        System.out.println(movie);

        assertNotNull(movie);
        assertEquals(1, movie.getReviewList().size());
        assertEquals("Dark Knight Rises", movie.getMovieInfo().getName());
    }

    @RepeatedTest(10)
    void retrieveMovies() {

        List<Long> movieIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

        startTimer();
        var movies = moviesClient.retrieveMovies(movieIds);
        timeTaken();

        assertNotNull(movies);
        assertEquals(7, movies.size());

    }

    @RepeatedTest(10)
    void retrieveMoviesCompletableFuture() {

        List<Long> movieIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

        startTimer();
        var movies = moviesClient.retrieveMoviesCompletableFuture(movieIds);
        timeTaken();

        assertNotNull(movies);
        assertEquals(7, movies.size());

    }

    @RepeatedTest(10)
    void retrieveMoviesCompletableFutureAllOf() {

        List<Long> movieIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

        startTimer();
        var movies = moviesClient.retrieveMoviesCompletableFutureAllOf(movieIds);
        timeTaken();

        assertNotNull(movies);
        assertEquals(7, movies.size());

    }
}