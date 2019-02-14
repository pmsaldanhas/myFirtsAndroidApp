package movies.flag.pt.moviesapp.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.http.entities.MovieTrailer;
import movies.flag.pt.moviesapp.http.entities.MovieTrailerResponse;
import movies.flag.pt.moviesapp.http.interfaces.RequestListener;
import movies.flag.pt.moviesapp.http.requests.GetNowPlayingMovieTrailerAsyncTask;
import movies.flag.pt.moviesapp.utils.DLog;

public class DetailPageMovie extends Screen{

    private TextView detailPageTitle;
    private TextView detailPageDescription;
    private TextView detailPageRanking;
    private ImageView detailPageImage;
    private Button seeTrailerBt;
    private String idMovieSelected;
    private String sTrailerKey;
    private Context context;
    public final static String DETAILS_TITLE_MOVIE = "DETAILS_TITLE_MOVIE_KEY";
    public final static String DETAILS_DESCRIPTION_MOVIE = "DETAILS_DESCRIPTION_MOVIE_KEY";
    public final static String DETAILS_RANKING_MOVIE = "DETAILS_RANKING_MOVIE_KEY";
    public final static String DETAILS_IMAGE_MOVIE = "DETAILS_IMAGE_MOVIE_KEY";
    public final static String DETAILS_ID_MOVIE = "DETAILS_ID_MOVIE_KEY";
    public final static String DETAILS_KEY_MOVIE_TRAILER = "DETAILS_KEY_MOVIE_TRAILER_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page_movie);

        findViews();
        addListeners();

        Intent intent = getIntent();
        String titleMovieReceived = intent.getStringExtra(DETAILS_TITLE_MOVIE);
        String descriptionMovieReceived = intent.getStringExtra(DETAILS_DESCRIPTION_MOVIE);
        String rankingMovieReceived = intent.getStringExtra(DETAILS_RANKING_MOVIE);
        String imageMovieReceived = intent.getStringExtra(DETAILS_IMAGE_MOVIE);
        idMovieSelected = intent.getStringExtra(DETAILS_ID_MOVIE);
        detailPageTitle.setText(titleMovieReceived);
        detailPageDescription.setText(descriptionMovieReceived);
        detailPageRanking.setText(rankingMovieReceived);

        String urlLoadImg = new StringBuilder()
                .append(getString(R.string.detail_page_endpoint_image))
                .append(imageMovieReceived)
                .toString();

        Glide.with(this)
                .load(urlLoadImg)
                .placeholder(R.drawable.ic_warning_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(detailPageImage);
    }

    private void findViews() {
        detailPageTitle = findViewById(R.id.detail_page_movie_title_label);
        detailPageDescription = findViewById(R.id.detail_page_movie_description_label);
        detailPageRanking = findViewById(R.id.detail_page_movie_ranking_label);
        detailPageImage = findViewById(R.id.detail_page_movie_image);
        seeTrailerBt = findViewById(R.id.detail_page_movie_trailer_button);
    }

    private void addListeners () {
        seeTrailerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeRequestMovieTrailer();
            }
        });
    }

    private void executeRequestMovieTrailer() {
        new GetNowPlayingMovieTrailerAsyncTask(this, idMovieSelected, new RequestListener<MovieTrailerResponse>() {
            @Override
            public void onResponseSuccess(MovieTrailerResponse responseEntity) {
                DLog.d(tag, "onResponseSuccess");
                //net
                responseEntity.getKeys();
                ArrayList<MovieTrailer> movieTrailerKeys = (ArrayList<MovieTrailer>) responseEntity.getKeys();
                sTrailerKey = movieTrailerKeys.get(0).getKey();

                Intent intent = new Intent(DetailPageMovie.this, DetailPageMovieTrailer.class);
                intent.putExtra(DetailPageMovie.DETAILS_KEY_MOVIE_TRAILER, sTrailerKey);
                startActivity(intent);
            }
            @Override
            public void onNetworkError() {
                DLog.d(tag, "onNetworkError");
                //!net
                Toast.makeText(context, getString(R.string.errorMovieTrailer), Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }
}
