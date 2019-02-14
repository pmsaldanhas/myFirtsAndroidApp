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
import movies.flag.pt.moviesapp.http.entities.SerieTrailer;
import movies.flag.pt.moviesapp.http.entities.SerieTrailerResponse;
import movies.flag.pt.moviesapp.http.interfaces.RequestListener;
import movies.flag.pt.moviesapp.http.requests.GetNowPlayingSerieTrailerAsyncTask;
import movies.flag.pt.moviesapp.utils.DLog;

public class DetailPageSerie extends Screen{

    private TextView detailPageTitle;
    private TextView detailPageDescription;
    private TextView detailPageRanking;
    private ImageView detailPageImage;
    private Button seeTrailerBt;
    private String idSerieSelected;
    private String sTrailerKey;
    private Context context;

    public final static String DETAILS_TITLE_SERIE = "DETAILS_TITLE_SERIE_KEY";
    public final static String DETAILS_DESCRIPTION_SERIE = "DETAILS_DESCRIPTION_SERIE_KEY";
    public final static String DETAILS_RANKING_SERIE = "DETAILS_RANKING_SERIE_KEY";
    public final static String DETAILS_IMAGE_SERIE = "DETAILS_IMAGE_SERIE_KEY";
    public final static String DETAILS_ID_SERIE = "DETAILS_ID_SERIE_KEY";
    public final static String DETAILS_KEY_SERIE_TRAILER = "DETAILS_KEY_SERIE_TRAILER_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page_serie);

        findViews();
        addListeners();

        Intent intent = getIntent();
        String titleSerieReceived = intent.getStringExtra(DETAILS_TITLE_SERIE);
        String descriptionSerieReceived = intent.getStringExtra(DETAILS_DESCRIPTION_SERIE);
        String rankingSerieReceived = intent.getStringExtra(DETAILS_RANKING_SERIE);
        String imageSerieReceived = intent.getStringExtra(DETAILS_IMAGE_SERIE);
        idSerieSelected = intent.getStringExtra(DETAILS_ID_SERIE);
        detailPageTitle.setText(titleSerieReceived);
        detailPageDescription.setText(descriptionSerieReceived);
        detailPageRanking.setText(rankingSerieReceived);

        String urlLoadImg = new StringBuilder()
                .append(getString(R.string.detail_page_endpoint_image))
                .append(imageSerieReceived)
                .toString();

        Glide.with(this)
                .load(urlLoadImg)
                .placeholder(R.drawable.ic_warning_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(detailPageImage);
    }

    private void findViews() {
        detailPageTitle = findViewById(R.id.detail_page_serie_title_label);
        detailPageDescription = findViewById(R.id.detail_page_serie_description_label);
        detailPageRanking = findViewById(R.id.detail_page_serie_ranking_label);
        detailPageImage = findViewById(R.id.detail_page_serie_image);
        seeTrailerBt = findViewById(R.id.detail_page_serie_trailer_button);
    }

    private void addListeners () {
        seeTrailerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeRequestSerieTrailer();
            }
        });
    }

    private void executeRequestSerieTrailer() {
        new GetNowPlayingSerieTrailerAsyncTask(this, idSerieSelected, new RequestListener<SerieTrailerResponse>() {
            @Override
            public void onResponseSuccess(SerieTrailerResponse responseEntity) {
                DLog.d(tag, "onResponseSuccess");
                //net
                responseEntity.getKeys();
                ArrayList<SerieTrailer> serieTrailerKeys = (ArrayList<SerieTrailer>) responseEntity.getKeys();
                sTrailerKey = serieTrailerKeys.get(0).getKey();

                Intent intent = new Intent(DetailPageSerie.this, DetailPageSerieTrailer.class);
                intent.putExtra(DetailPageSerie.DETAILS_KEY_SERIE_TRAILER, sTrailerKey);
                startActivity(intent);
            }
            @Override
            public void onNetworkError() {
                DLog.d(tag, "onNetworkError");
                //!net
                Toast.makeText(context, getString(R.string.errorSerieTrailer), Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }
}
