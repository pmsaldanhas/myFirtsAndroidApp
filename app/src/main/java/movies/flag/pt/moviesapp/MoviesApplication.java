package movies.flag.pt.moviesapp;

import android.app.Application;
import android.util.Log;

import com.orm.SugarContext;

import movies.flag.pt.moviesapp.helpers.SharedPreferencesHelper;


public class MoviesApplication extends Application {

    private static final String TAG = MoviesApplication.class.getSimpleName();

    private static MoviesApplication instance;

    public static MoviesApplication getInstance() {
        return instance;
    }

    private int numberOfResumedScreens;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        instance = this;
        SharedPreferencesHelper.init(this);
        SugarContext.init(this);
    }

    public void addOnResumeScreen() {
        numberOfResumedScreens++;
    }

    public void removeOnResumeScreen() {
        numberOfResumedScreens--;
    }

    public boolean applicationIsInBackground() {
        return numberOfResumedScreens == 0;
    }

}
