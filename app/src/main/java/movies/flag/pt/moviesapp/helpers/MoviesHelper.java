package movies.flag.pt.moviesapp.helpers;

import java.util.ArrayList;

import movies.flag.pt.moviesapp.db.MovieDataBase;
import movies.flag.pt.moviesapp.http.entities.Movie;

public final class MoviesHelper {

    public static ArrayList<MovieDataBase> createMoviesDb (ArrayList<Movie> movies) {
        //moviestodb

        ArrayList<MovieDataBase> moviesDB = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {

            Movie movie = movies.get(i);

            MovieDataBase movieDB = new MovieDataBase(
                    movie.getPosterPath(),
                    movie.getOverview(),
                    movie.getReleaseDate(),
                    movie.getOriginalTitle(),
                    movie.getOriginalLanguage(),
                    movie.getTitle(),
                    movie.getBackdropPath(),
                    movie.getPopularity(),
                    movie.getVoteCount(),
                    movie.getVoteAverage(),
                    movie.getMovieId()
            );

            moviesDB.add(movieDB);
        }
        return moviesDB;
    }

    public static ArrayList<Movie> fromDb (ArrayList<MovieDataBase> moviesDb) {
        //moviesDBTomovies

        ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesDb.size(); i++) {

            MovieDataBase movieDB = moviesDb.get(i);

            Movie movie = new Movie(
                    movieDB.getPosterPath(),
                    movieDB.getOverview(),
                    movieDB.getReleaseDate(),
                    movieDB.getOriginalTitle(),
                    movieDB.getOriginalLanguage(),
                    movieDB.getTitle(),
                    movieDB.getBackdropPath(),
                    movieDB.getPopularity(),
                    movieDB.getVoteCount(),
                    movieDB.getVoteAverage(),
                    movieDB.getMovieId()
            );

            movies.add(movie);
        }
        return movies;
    }
}