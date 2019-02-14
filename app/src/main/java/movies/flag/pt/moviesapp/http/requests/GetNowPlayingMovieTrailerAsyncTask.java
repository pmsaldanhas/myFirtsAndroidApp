package movies.flag.pt.moviesapp.http.requests;

import android.content.Context;

import movies.flag.pt.moviesapp.http.entities.MovieTrailerResponse;
import movies.flag.pt.moviesapp.http.interfaces.RequestListener;


public class GetNowPlayingMovieTrailerAsyncTask extends ExecuteRequestAsyncTask<MovieTrailerResponse> {
    private static String finalPath;
    private static String movieId;
    private static final String PATH = "/movie/";
    private static final String PATH_FINAL = "/videos";

    public GetNowPlayingMovieTrailerAsyncTask(Context context, String sMovieId, RequestListener<MovieTrailerResponse> requestListener) {
        super(context, requestListener);
        this.movieId = sMovieId;
    }

    @Override
    protected String getPath() {
        StringBuilder sbPath = new StringBuilder(PATH);
        sbPath.append(movieId);
        sbPath.append(PATH_FINAL);

        finalPath = sbPath.toString();

        return finalPath;
    }

    @Override
    protected void addQueryParams(StringBuilder sb) {
    }

    @Override
    protected Class<MovieTrailerResponse> getResponseEntityClass() {
        return MovieTrailerResponse.class;
    }
}
