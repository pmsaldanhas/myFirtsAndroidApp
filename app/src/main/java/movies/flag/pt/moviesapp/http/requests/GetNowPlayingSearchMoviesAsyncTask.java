package movies.flag.pt.moviesapp.http.requests;

import android.content.Context;

import movies.flag.pt.moviesapp.http.entities.MoviesResponse;
import movies.flag.pt.moviesapp.http.interfaces.RequestListener;


public class GetNowPlayingSearchMoviesAsyncTask extends ExecuteRequestAsyncTask<MoviesResponse> {

    private static final String PATH = "/search/movie";

    private static final String QUERY_SEARCH_KEY = "query";
    private static final String PAGE_KEY = "page";

    private String querySearchValue;

    private String pageNumber;


    public GetNowPlayingSearchMoviesAsyncTask(Context context, String sPageNumber, String sQuerySearchValue, RequestListener<MoviesResponse> requestListener) {
        super(context, requestListener);
        this.querySearchValue = sQuerySearchValue;
        this.pageNumber = sPageNumber;
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected void addQueryParams(StringBuilder sb) {
        addQueryParam(sb, QUERY_SEARCH_KEY, querySearchValue);
        addQueryParam(sb, PAGE_KEY, pageNumber);
    }

    @Override
    protected Class<MoviesResponse> getResponseEntityClass() {
        return MoviesResponse.class;
    }
}
