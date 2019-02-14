package movies.flag.pt.moviesapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import movies.flag.pt.moviesapp.R;

public class FirstLaunchScreen extends Screen {

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_launch_screen);

        button = findViewById(R.id.first_launch_screen_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstLaunchScreen.this, LaunchpadScreen.class));
                finish();
            }
        });
    }
}
