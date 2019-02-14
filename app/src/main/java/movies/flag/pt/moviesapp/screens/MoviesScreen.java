package movies.flag.pt.moviesapp.screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.adapters.MoviesAdapter;
import movies.flag.pt.moviesapp.constants.PreferenceIds;
import movies.flag.pt.moviesapp.db.MovieDataBase;
import movies.flag.pt.moviesapp.helpers.CheckNetworkHelper;
import movies.flag.pt.moviesapp.helpers.MoviesHelper;
import movies.flag.pt.moviesapp.helpers.SharedPreferencesHelper;
import movies.flag.pt.moviesapp.http.entities.Movie;
import movies.flag.pt.moviesapp.http.entities.MoviesResponse;
import movies.flag.pt.moviesapp.http.interfaces.RequestListener;
import movies.flag.pt.moviesapp.http.requests.GetNowPlayingMoviesAsyncTask;
import movies.flag.pt.moviesapp.utils.DLog;

public class MoviesScreen extends Screen{

    private ListView moviesView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private Button btIncrease;
    private TextView headerDateHour;
    private MoviesScreen.NetworkChangeBroadcastReceiver networkChangeBroadcastReceiver;
    private int pageNr;
    private MoviesAdapter adapter;
    private ArrayList<MovieDataBase> moviesToDB;
    private ArrayList<Movie> moviesfromdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_screen);

        findViews();
        addFooterView();
        addHeaderView();
        addListeners();

        saveReceivers();
        String sPageNr = getString(R.string.pageNumberOneMovies);
        pageNr = Integer.parseInt(sPageNr);
        executeRequestMoviesScreen(sPageNr);
    }

    private void executeRequestMoviesScreen (final String pageNumber) {
        // Example to request get now playing movies
        new GetNowPlayingMoviesAsyncTask(this, pageNumber, new RequestListener<MoviesResponse>() {
            @Override
            public void onResponseSuccess(MoviesResponse responseEntity) {
                DLog.d(tag, "onResponseSuccess");


                SharedPreferencesHelper.savePreference(PreferenceIds.CUR_DATE_TIME_MOVIES, System.currentTimeMillis());
                long getDateTimeLongFormat = SharedPreferencesHelper.
                        getLongPreference(PreferenceIds.CUR_DATE_TIME_MOVIES, 0);
                Date dateTime = new Date(getDateTimeLongFormat);
                SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                //Show date and time on Header
                headerDateHour.setText(format.format(dateTime));

                //Reset DB
                SugarRecord.deleteAll(MovieDataBase.class);

                //If net available
                ArrayList<Movie> movies = (ArrayList<Movie>) responseEntity.getMovies();

                //Save on DB
                moviesToDB = MoviesHelper.createMoviesDb((ArrayList<Movie>) responseEntity.getMovies());
                SugarRecord.saveInTx(moviesToDB);

                //Stop SwipeRefreshLayout when Movies are loaded
                mySwipeRefreshLayout.setRefreshing(false);

                //Set btIncrease disable
                if ( movies.isEmpty() || Integer.parseInt(pageNumber) == responseEntity.getTotalPages() ) {
                    btIncrease.setVisibility(View.GONE);
                }

                //Show the complete list of movies loaded
                if(pageNumber.equals(getString(R.string.pageNumberOneMovies))) {
                    adapter = new MoviesAdapter(MoviesScreen.this, movies);
                    moviesView.setAdapter(adapter);
                }
                else{
                    adapter.addAll(movies);
                }
            }

            @Override
            public void onNetworkError() {
                DLog.d(tag, "onNetworkError");

                //Stop SwipeRefreshLayout when Movies are loaded from DB and turn button disabled
                mySwipeRefreshLayout.setRefreshing(false);
                mySwipeRefreshLayout.setEnabled(false);
                btIncrease.setVisibility(View.GONE);

                long getDateTimeLongFormat = SharedPreferencesHelper.
                        getLongPreference(PreferenceIds.CUR_DATE_TIME_MOVIES, 0);
                Date dateTime = new Date(getDateTimeLongFormat);
                SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                //Show date and time on Header
                headerDateHour.setText(format.format(dateTime));

                //If net disabled
                moviesfromdb = MoviesHelper.fromDb((ArrayList<MovieDataBase>) SugarRecord.listAll(MovieDataBase.class));
                adapter = new MoviesAdapter(MoviesScreen.this, moviesfromdb);
                moviesView.setAdapter(adapter);
            }
        }).execute();
    }

    private void findViews() {
        moviesView = findViewById(R.id.movies_screen_list_view);
        mySwipeRefreshLayout = findViewById(R.id.movies_screen_swiperefresh);
        headerDateHour = findViewById(R.id.header_movies_item_datehour_label);
        //btIncrease = findViewById(R.id.footer_movies_item_btincrease);
    }

    private void addFooterView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View footerMovie = inflater.inflate(R.layout.footer_movies_item, null, false);
        btIncrease = footerMovie.findViewById(R.id.footer_movies_item_btincrease);
        moviesView.addFooterView(footerMovie);
    }

    private void addHeaderView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View headerMovie = inflater.inflate(R.layout.header_movies_item, null, false);
        headerDateHour = headerMovie.findViewById(R.id.header_movies_item_datehour_label);
        moviesView.addHeaderView(headerMovie);
    }

    private void saveReceivers() {
        networkChangeBroadcastReceiver = new MoviesScreen.NetworkChangeBroadcastReceiver();
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
                        getLongPreference(PreferenceIds.CUR_DATE_TIME_MOVIES, 0);

                //condition -> last update over 5 min at the moment when net is available
                if( longDateVerify < ( System.currentTimeMillis() - (5*60*1000) ) ) {
                    String sPageNr = getString(R.string.pageNumberOneMovies);
                    pageNr = Integer.parseInt(sPageNr);
                    executeRequestMoviesScreen(sPageNr);
                }
            } else {
                Log.d(tag, getString(R.string.wifiNoAvailable));
                Toast.makeText(context, getString(R.string.wifiNoAvailable), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addListeners() {
        mySwipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i(tag, "onRefresh called from SwipeRefreshLayout");
                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                    String sPageNr = String.valueOf(pageNr);
                    executeRequestMoviesScreen(sPageNr);
                }
            }
        );

        btIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sPageNr = String.valueOf(++pageNr);
                executeRequestMoviesScreen(sPageNr);
            }
        });
    }
}
