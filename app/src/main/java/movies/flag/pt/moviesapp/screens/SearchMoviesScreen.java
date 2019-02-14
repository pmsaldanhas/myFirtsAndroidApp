package movies.flag.pt.moviesapp.screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.adapters.MoviesAdapter;
import movies.flag.pt.moviesapp.constants.PreferenceIds;
import movies.flag.pt.moviesapp.helpers.CheckNetworkHelper;
import movies.flag.pt.moviesapp.helpers.SharedPreferencesHelper;
import movies.flag.pt.moviesapp.http.entities.Movie;
import movies.flag.pt.moviesapp.http.entities.MoviesResponse;
import movies.flag.pt.moviesapp.http.interfaces.RequestListener;
import movies.flag.pt.moviesapp.http.requests.GetNowPlayingSearchMoviesAsyncTask;
import movies.flag.pt.moviesapp.utils.DLog;

public class SearchMoviesScreen extends Screen{

    private ListView searchMoviesView;
    private EditText inputSearchMovieText;
    private Button searchMoviesButton;
    private SearchMoviesScreen.NetworkChangeBroadcastReceiver networkChangeBroadcastReceiver;

    private int pageNr;

    private MoviesAdapter adapter;

    private SwipeRefreshLayout mySwipeRefreshLayout;
    private Button btIncrease;
    private TextView headerDateHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_movies_screen);

        findViews();
        addFooterView();
        addHeaderView();
        addListeners();
        saveReceivers();

        String sPageNr = getString(R.string.pageNumberOneSearchMovies);
        pageNr = Integer.parseInt(sPageNr);
        executeRequestSearchMovie(sPageNr);
    }

    private void executeRequestSearchMovie(final String pageNumber) {

        String searchedWord = inputSearchMovieText.getText().toString();

        // Example to request get now playing movies
        new GetNowPlayingSearchMoviesAsyncTask(this, pageNumber, searchedWord, new RequestListener<MoviesResponse>() {

            @Override
            public void onResponseSuccess(MoviesResponse responseEntity) {
                DLog.d(tag, "onResponseSuccess");

                SharedPreferencesHelper.savePreference(PreferenceIds.CUR_DATE_TIME_SEARCH_MOVIES, System.currentTimeMillis());
                long getDateTimeLongFormat = SharedPreferencesHelper.
                        getLongPreference(PreferenceIds.CUR_DATE_TIME_SEARCH_MOVIES, 0);
                Date dateTime = new Date(getDateTimeLongFormat);
                SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                //Show date and time on Header
                headerDateHour.setText(format.format(dateTime));

                //If net available
                ArrayList<Movie> movies = (ArrayList<Movie>) responseEntity.getMovies();

                //Stop SwipeRefreshLayout when Movies are loaded
                mySwipeRefreshLayout.setRefreshing(false);

                //Set btIncrease disable
                if ( movies.isEmpty() || Integer.parseInt(pageNumber) == responseEntity.getTotalPages() ) {
                    btIncrease.setVisibility(View.GONE);
                }

                //Show the complete list of movies loaded
                if(pageNumber.equals(getString(R.string.pageNumberOneSearchMovies))) {
                    adapter = new MoviesAdapter(SearchMoviesScreen.this, movies);
                    searchMoviesView.setAdapter(adapter);
                }
                else{
                    adapter.addAll(movies);
                }

            }

            @Override
            public void onNetworkError() {
                DLog.d(tag, "onNetworkError");
            }
        }).execute();
    }

    private void findViews() {
        mySwipeRefreshLayout = findViewById(R.id.search_movies_screen_swiperefresh);
        searchMoviesView = findViewById(R.id.search_movies_screen_list_view);
        inputSearchMovieText = findViewById(R.id.search_movies_screen_input_text);
        searchMoviesButton = findViewById(R.id.search_movies_screen_search_button);
    }

    private void addFooterView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View footerMovie = inflater.inflate(R.layout.footer_search_movies_item, null, false);
        btIncrease = footerMovie.findViewById(R.id.footer_search_movies_item_btincrease);
        searchMoviesView.addFooterView(footerMovie);
    }

    private void addHeaderView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View headerMovie = inflater.inflate(R.layout.header_search_movies_item, null, false);
        headerDateHour = headerMovie.findViewById(R.id.header_search_movies_item_datehour_label);
        searchMoviesView.addHeaderView(headerMovie);
    }


    private void saveReceivers() {
        networkChangeBroadcastReceiver = new SearchMoviesScreen.NetworkChangeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeBroadcastReceiver, intentFilter);
    }

    public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {

        private final String tag = MoviesScreen.NetworkChangeBroadcastReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            if ( CheckNetworkHelper.networkAvailable(context) ) {
                Log.d(tag, getString(R.string.wifiAvailable));

                Toast.makeText(context, getString(R.string.wifiAvailable), Toast.LENGTH_SHORT).show();

                long longDateVerify = SharedPreferencesHelper.
                        getLongPreference(PreferenceIds.CUR_DATE_TIME_SEARCH_MOVIES, 0);

                //condition -> last update over 5 min at the moment when net is available
                if( longDateVerify < ( System.currentTimeMillis() - (5*60*1000) ) ) {
                    String sPageNr = getString(R.string.pageNumberOneSearchMovies);
                    pageNr = Integer.parseInt(sPageNr);
                    executeRequestSearchMovie(sPageNr);
                }
            } else {
                Log.d(tag, getString(R.string.wifiNoAvailable));
                Toast.makeText(context, getString(R.string.wifiNoAvailable), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addListeners () {

        searchMoviesButton.setEnabled(false);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(tag, "onRefresh called from SwipeRefreshLayout");
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        String sPageNr = String.valueOf(pageNr);
                        executeRequestSearchMovie(sPageNr);
                    }
                }
        );

        btIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sPageNr = String.valueOf(++pageNr);
                executeRequestSearchMovie(sPageNr);
            }
        });

        inputSearchMovieText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchMoviesButton.setEnabled(s.length() > 0);

                searchMoviesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sPageNr = getString(R.string.pageNumberOneSearchMovies);
                        pageNr = Integer.parseInt(sPageNr);
                        executeRequestSearchMovie(sPageNr);
                    }
                });
            }
        });


    }
}
