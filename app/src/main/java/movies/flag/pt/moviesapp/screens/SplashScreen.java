package movies.flag.pt.moviesapp.screens;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.constants.PreferenceIds;
import movies.flag.pt.moviesapp.helpers.SharedPreferencesHelper;

public class SplashScreen extends Screen {

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        findViews();
        addListeners();
    }

    private void findViews() {
        imageView = findViewById(R.id.splash_screen_image);
    }

    private void addListeners() {
        imageView.setScaleX(0);
        imageView.setScaleY(0);
        imageView.animate().scaleX(1).scaleY(1).rotationXBy(360).rotationYBy(360)
                .setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startFirstScreen();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        }).setDuration(2000).start();
    }

    private void startFirstScreen() {
        boolean isFirstUse = SharedPreferencesHelper.
                getBooleanPreference(PreferenceIds.IS_FIRST_USE, true);

        if (isFirstUse) {
            SharedPreferencesHelper.savePreference(PreferenceIds.IS_FIRST_USE, false);
            startActivity(new Intent(SplashScreen.this, FirstLaunchScreen.class));
            finish();
        } else {
            startActivity(new Intent(SplashScreen.this, LaunchpadScreen.class));
            finish();
        }
    }

}