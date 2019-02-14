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
import movies.flag.pt.moviesapp.adapters.SeriesAdapter;
import movies.flag.pt.moviesapp.constants.PreferenceIds;
import movies.flag.pt.moviesapp.helpers.CheckNetworkHelper;
import movies.flag.pt.moviesapp.helpers.SharedPreferencesHelper;
import movies.flag.pt.moviesapp.http.entities.Serie;
import movies.flag.pt.moviesapp.http.entities.SeriesResponse;
import movies.flag.pt.moviesapp.http.interfaces.RequestListener;
import movies.flag.pt.moviesapp.http.requests.GetNowPlayingSearchSeriesAsyncTask;
import movies.flag.pt.moviesapp.utils.DLog;

public class SearchSeriesScreen extends Screen{

    private ListView searchSeriesView;
    private EditText inputSearchSeriesText;
    private Button searchSeriesButton;
    private SearchSeriesScreen.NetworkChangeBroadcastReceiver networkChangeBroadcastReceiver;

    private int pageNr;

    private SeriesAdapter adapter;

    private SwipeRefreshLayout mySwipeRefreshLayout;
    private Button btIncrease;
    private TextView headerDateHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_series_screen);

        findViews();
        addFooterView();
        addHeaderView();
        addListeners();
        saveReceivers();

        String sPageNr = getString(R.string.pageNumberOneSearchSeries);
        pageNr = Integer.parseInt(sPageNr);
        executeRequestSearchSerie(sPageNr);
    }

    private void executeRequestSearchSerie(final String pageNumber) {

        String searchedWord = inputSearchSeriesText.getText().toString();

        // Example to request get now playing series
        new GetNowPlayingSearchSeriesAsyncTask(this, pageNumber, searchedWord, new RequestListener<SeriesResponse>() {

            @Override
            public void onResponseSuccess(SeriesResponse responseEntity) {
                DLog.d(tag, "onResponseSuccess");

                SharedPreferencesHelper.savePreference(PreferenceIds.CUR_DATE_TIME_SEARCH_SERIES, System.currentTimeMillis());
                long getDateTimeLongFormat = SharedPreferencesHelper.
                        getLongPreference(PreferenceIds.CUR_DATE_TIME_SEARCH_SERIES, 0);
                Date dateTime = new Date(getDateTimeLongFormat);
                SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                //Show date and time on Header
                headerDateHour.setText(format.format(dateTime));

                //If net available
                ArrayList<Serie> series = (ArrayList<Serie>) responseEntity.getSeries();

                //Stop SwipeRefreshLayout when Series are loaded
                mySwipeRefreshLayout.setRefreshing(false);

                //Set btIncrease disable
                if ( series.isEmpty() || Integer.parseInt(pageNumber) == responseEntity.getTotalPages() ) {
                    btIncrease.setVisibility(View.GONE);
                }

                //Show the complete list of series loaded
                if(pageNumber.equals(getString(R.string.pageNumberOneSearchSeries))) {
                    adapter = new SeriesAdapter(SearchSeriesScreen.this, series);
                    searchSeriesView.setAdapter(adapter);
                }
                else{
                    adapter.addAll(series);
                }
            }

            @Override
            public void onNetworkError() {
                DLog.d(tag, "onNetworkError");
            }
        }).execute();
    }

    private void findViews() {
        mySwipeRefreshLayout = findViewById(R.id.search_series_screen_swiperefresh);
        searchSeriesView = findViewById(R.id.search_series_screen_list_view);
        inputSearchSeriesText = findViewById(R.id.search_series_screen_input_text);
        searchSeriesButton = findViewById(R.id.search_series_screen_search_button);
    }

    private void addFooterView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View footerSerie = inflater.inflate(R.layout.footer_search_series_item, null, false);
        btIncrease = footerSerie.findViewById(R.id.footer_search_series_item_btincrease);
        searchSeriesView.addFooterView(footerSerie);
    }

    private void addHeaderView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View headerSerie = inflater.inflate(R.layout.header_search_series_item, null, false);
        headerDateHour = headerSerie.findViewById(R.id.header_search_series_item_datehour_label);
        searchSeriesView.addHeaderView(headerSerie);
    }


    private void saveReceivers() {
        networkChangeBroadcastReceiver = new SearchSeriesScreen.NetworkChangeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeBroadcastReceiver, intentFilter);
    }

    public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {

        private final String tag = SeriesScreen.NetworkChangeBroadcastReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            if ( CheckNetworkHelper.networkAvailable(context) ) {
                Log.d(tag, getString(R.string.wifiAvailable));

                Toast.makeText(context, getString(R.string.wifiAvailable), Toast.LENGTH_SHORT).show();

                long longDateVerify = SharedPreferencesHelper.
                        getLongPreference(PreferenceIds.CUR_DATE_TIME_SEARCH_SERIES, 0);

                //condition -> last update over 5 min at the moment when net is available
                if( longDateVerify < ( System.currentTimeMillis() - (5*60*1000) ) ) {
                    String sPageNr = getString(R.string.pageNumberOneSearchSeries);
                    pageNr = Integer.parseInt(sPageNr);
                    executeRequestSearchSerie(sPageNr);
                }
            } else {
                Log.d(tag, getString(R.string.wifiNoAvailable));
                Toast.makeText(context, getString(R.string.wifiNoAvailable), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addListeners () {

        searchSeriesButton.setEnabled(false);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(tag, "onRefresh called from SwipeRefreshLayout");
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        String sPageNr = String.valueOf(pageNr);
                        executeRequestSearchSerie(sPageNr);
                    }
                }
        );

        btIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sPageNr = String.valueOf(++pageNr);
                executeRequestSearchSerie(sPageNr);
            }
        });

        inputSearchSeriesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchSeriesButton.setEnabled(s.length() > 0);

                searchSeriesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sPageNr = getString(R.string.pageNumberOneSearchSeries);
                        pageNr = Integer.parseInt(sPageNr);
                        executeRequestSearchSerie(sPageNr);
                    }
                });
            }
        });
    }
}
