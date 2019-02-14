package movies.flag.pt.moviesapp.http.requests;

import android.content.Context;

import movies.flag.pt.moviesapp.http.entities.SeriesResponse;
import movies.flag.pt.moviesapp.http.interfaces.RequestListener;


public class GetNowPlayingSearchSeriesAsyncTask extends ExecuteRequestAsyncTask<SeriesResponse> {

    private static final String PATH = "/search/tv";

    private static final String QUERY_SEARCH_KEY = "query";
    private static final String PAGE_KEY = "page";

    private String querySearchValue;

    private String pageNumber;


    public GetNowPlayingSearchSeriesAsyncTask(Context context, String sPageNumber, String sQuerySearchValue, RequestListener<SeriesResponse> requestListener) {
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
    protected Class<SeriesResponse> getResponseEntityClass() {
        return SeriesResponse.class;
    }
}
