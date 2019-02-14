package movies.flag.pt.moviesapp.http.requests;

import android.content.Context;

import movies.flag.pt.moviesapp.http.entities.SerieTrailerResponse;
import movies.flag.pt.moviesapp.http.interfaces.RequestListener;


public class GetNowPlayingSerieTrailerAsyncTask extends ExecuteRequestAsyncTask<SerieTrailerResponse> {
    private static String finalPath;
    private static String serieId;
    private static final String PATH = "/tv/";
    private static final String PATH_FINAL = "/videos";

    public GetNowPlayingSerieTrailerAsyncTask(Context context, String sSerieId, RequestListener<SerieTrailerResponse> requestListener) {
        super(context, requestListener);
        this.serieId = sSerieId;
    }

    @Override
    protected String getPath() {
        StringBuilder sbPath = new StringBuilder(PATH);
        sbPath.append(serieId);
        sbPath.append(PATH_FINAL);

        finalPath = sbPath.toString();

        return finalPath;
    }

    @Override
    protected void addQueryParams(StringBuilder sb) {
    }

    @Override
    protected Class<SerieTrailerResponse> getResponseEntityClass() {
        return SerieTrailerResponse.class;
    }
}
