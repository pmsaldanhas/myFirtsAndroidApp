package movies.flag.pt.moviesapp.http.requests;

import android.content.Context;

import movies.flag.pt.moviesapp.http.entities.MoviesResponse;
import movies.flag.pt.moviesapp.http.interfaces.RequestListener;


public class GetNowPlayingMoviesAsyncTask extends ExecuteRequestAsyncTask<MoviesResponse> {

    private static final String PATH = "/movie/now_playing";
    //private static final String LANGUAGE_KEY = "language";
    //private static final String LANGUAGE_VALUE = "pt";

    private Context context;

    private static final String PAGE_KEY = "page";

    private String pageNumber;

    public GetNowPlayingMoviesAsyncTask(Context context, String sPageNumber, RequestListener<MoviesResponse> requestListener) {
        super(context, requestListener);
        this.pageNumber = sPageNumber;
        this.context = context;
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected void addQueryParams(StringBuilder sb) {
        addQueryParam(sb, PAGE_KEY, pageNumber);
    }

    @Override
    protected Class<MoviesResponse> getResponseEntityClass() {
        return MoviesResponse.class;
    }
}
