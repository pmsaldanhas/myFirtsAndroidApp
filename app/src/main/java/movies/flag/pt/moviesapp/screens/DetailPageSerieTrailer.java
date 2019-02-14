package movies.flag.pt.moviesapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import movies.flag.pt.moviesapp.R;

public class DetailPageSerieTrailer extends Screen{

    private WebView trailerWebView;
    private String keySerieSelected;
    private String sEndpointYoutube;
    public final static String DETAILS_KEY_SERIE_TRAILER = "DETAILS_KEY_SERIE_TRAILER_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page_serie_trailer);

        findViews();
        addListeners();

        Intent intent = getIntent();
        keySerieSelected = intent.getStringExtra(DETAILS_KEY_SERIE_TRAILER);
        sEndpointYoutube = getString(R.string.youtube_endpoint);

        String sb = new StringBuilder()
                .append(sEndpointYoutube)
                .append(keySerieSelected)
                .toString();

        trailerWebView.loadUrl(sb);
    }

    private void findViews() {
        trailerWebView = findViewById(R.id.detail_page_serie_trailer_video_trailer);
    }

    private void addListeners () {}
}
