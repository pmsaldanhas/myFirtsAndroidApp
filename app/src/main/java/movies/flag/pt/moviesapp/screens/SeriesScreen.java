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
import movies.flag.pt.moviesapp.adapters.SeriesAdapter;
import movies.flag.pt.moviesapp.constants.PreferenceIds;
import movies.flag.pt.moviesapp.db.SerieDataBase;
import movies.flag.pt.moviesapp.helpers.CheckNetworkHelper;
import movies.flag.pt.moviesapp.helpers.SeriesHelper;
import movies.flag.pt.moviesapp.helpers.SharedPreferencesHelper;
import movies.flag.pt.moviesapp.http.entities.Serie;
import movies.flag.pt.moviesapp.http.entities.SeriesResponse;
import movies.flag.pt.moviesapp.http.interfaces.RequestListener;
import movies.flag.pt.moviesapp.http.requests.GetNowPlayingSeriesAsyncTask;
import movies.flag.pt.moviesapp.utils.DLog;

public class SeriesScreen extends Screen{

    private ListView seriesView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private Button btIncrease;
    private TextView headerDateHour;
    private SeriesScreen.NetworkChangeBroadcastReceiver networkChangeBroadcastReceiver;
    private int pageNr;
    private SeriesAdapter adapter;
    private ArrayList<SerieDataBase> seriesToDB;
    private ArrayList<Serie> seriesfromdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.series_screen);

        findViews();
        addFooterView();
        addHeaderView();
        addListeners();

        saveReceivers();
        String sPageNr = getString(R.string.pageNumberOneSeries);
        pageNr = Integer.parseInt(sPageNr);
        executeRequestSeriesScreen(sPageNr);
    }

    private void executeRequestSeriesScreen (final String pageNumber) {
        // Example to request get now playing series
        new GetNowPlayingSeriesAsyncTask(this, pageNumber, new RequestListener<SeriesResponse>() {
            @Override
            public void onResponseSuccess(SeriesResponse responseEntity) {
                DLog.d(tag, "onResponseSuccess");


                SharedPreferencesHelper.savePreference(PreferenceIds.CUR_DATE_TIME_SERIES, System.currentTimeMillis());
                long getDateTimeLongFormat = SharedPreferencesHelper.
                        getLongPreference(PreferenceIds.CUR_DATE_TIME_SERIES, 0);
                Date dateTime = new Date(getDateTimeLongFormat);
                SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                //Show date and time on Header
                headerDateHour.setText(format.format(dateTime));

                //Reset DB
                SugarRecord.deleteAll(SerieDataBase.class);

                //If net available
                ArrayList<Serie> series = (ArrayList<Serie>) responseEntity.getSeries();

                //Save on DB
                seriesToDB = SeriesHelper.createSeriesDb((ArrayList<Serie>) responseEntity.getSeries());
                SugarRecord.saveInTx(seriesToDB);

                //Stop SwipeRefreshLayout when Series are loaded
                mySwipeRefreshLayout.setRefreshing(false);

                //Set btIncrease disable
                if ( series.isEmpty() || Integer.parseInt(pageNumber) == responseEntity.getTotalPages() ) {
                    btIncrease.setVisibility(View.GONE);
                }

                //Show the complete list of series loaded
                if(pageNumber.equals(getString(R.string.pageNumberOneSeries))) {
                    adapter = new SeriesAdapter(SeriesScreen.this, series);
                    seriesView.setAdapter(adapter);
                }
                else{
                    adapter.addAll(series);
                }
            }

            @Override
            public void onNetworkError() {
                DLog.d(tag, "onNetworkError");

                //Stop SwipeRefreshLayout when Series are loaded from DB and turn button disabled
                mySwipeRefreshLayout.setRefreshing(false);
                mySwipeRefreshLayout.setEnabled(false);
                btIncrease.setVisibility(View.GONE);

                long getDateTimeLongFormat = SharedPreferencesHelper.
                        getLongPreference(PreferenceIds.CUR_DATE_TIME_SERIES, 0);
                Date dateTime = new Date(getDateTimeLongFormat);
                SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                //Show date and time on Header
                headerDateHour.setText(format.format(dateTime));

                //If net disabled
                seriesfromdb = SeriesHelper.fromDb((ArrayList<SerieDataBase>) SugarRecord.listAll(SerieDataBase.class));
                adapter = new SeriesAdapter(SeriesScreen.this, seriesfromdb);
                seriesView.setAdapter(adapter);
            }
        }).execute();
    }

    private void findViews() {
        seriesView = findViewById(R.id.series_screen_list_view);
        mySwipeRefreshLayout = findViewById(R.id.series_screen_swiperefresh);
        headerDateHour = findViewById(R.id.header_series_item_datehour_label);
        //btIncrease = findViewById(R.id.footer_series_item_btincrease);
    }

    private void addFooterView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View footerSerie = inflater.inflate(R.layout.footer_series_item, null, false);
        btIncrease = footerSerie.findViewById(R.id.footer_series_item_btincrease);
        seriesView.addFooterView(footerSerie);
    }

    private void addHeaderView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View headerSerie = inflater.inflate(R.layout.header_series_item, null, false);
        headerDateHour = headerSerie.findViewById(R.id.header_series_item_datehour_label);
        seriesView.addHeaderView(headerSerie);
    }

    private void saveReceivers() {
        networkChangeBroadcastReceiver = new SeriesScreen.NetworkChangeBroadcastReceiver();
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
                        getLongPreference(PreferenceIds.CUR_DATE_TIME_SERIES, 0);

                //condition -> last update over 5 min at the moment when net is available
                if( longDateVerify < ( System.currentTimeMillis() - (5*60*1000) ) ) {
                    String sPageNr = getString(R.string.pageNumberOneSeries);
                    pageNr = Integer.parseInt(sPageNr);
                    executeRequestSeriesScreen(sPageNr);
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
                    executeRequestSeriesScreen(sPageNr);
                }
            }
        );

        btIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sPageNr = String.valueOf(++pageNr);
                executeRequestSeriesScreen(sPageNr);
            }
        });
    }
}
