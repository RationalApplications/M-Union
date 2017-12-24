package xyz.ratapp.munion.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * <p>Date: 03.12.17</p>
 *
 * @author Simon
 */

public class SplashActivity extends AppCompatActivity {

    public final static String EXTRA_SHOW_SPLASH = "show_splash";

    public static Intent getSplashIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        Bundle extras = new Bundle();
        extras.putBoolean(EXTRA_SHOW_SPLASH, true);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null &&
                savedInstanceState.containsKey(EXTRA_SHOW_SPLASH) &&
                savedInstanceState.getBoolean(EXTRA_SHOW_SPLASH)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
