package movies.flag.pt.moviesapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import movies.flag.pt.moviesapp.R;

public class LaunchpadScreen extends Screen {

    private Button openMoviePosterListButton;
    private Button openPopularSeriesButton;
    private Button openSearchMoviesButton;
    private Button openSearchSeriesButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.launchpad_screen);

        findViews();
        addListeners();

    }

    private void findViews() {
        openMoviePosterListButton = findViewById(R.id.launchpad_screen_movies_button);
        openPopularSeriesButton = findViewById(R.id.launchpad_screen_series_button);
        openSearchMoviesButton = findViewById(R.id.launchpad_screen_search_movie_button);
        openSearchSeriesButton = findViewById(R.id.launchpad_screen_search_serie_button);
    }

    private void addListeners() {
        openMoviePosterListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaunchpadScreen.this, MoviesScreen.class);
                startActivity(intent);
            }
        });

        openPopularSeriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaunchpadScreen.this, SeriesScreen.class);
                startActivity(intent);
            }
        });

        openSearchMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaunchpadScreen.this, SearchMoviesScreen.class);
                startActivity(intent);
            }
        });

        openSearchSeriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaunchpadScreen.this, SearchSeriesScreen.class);
                startActivity(intent);
            }
        });
    }
}
