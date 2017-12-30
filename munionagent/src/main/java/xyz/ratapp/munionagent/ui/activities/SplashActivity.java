package xyz.ratapp.munionagent.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.chatsdk.ui.main.MainActivity;
import xyz.ratapp.munionagent.helpers.PreferencesHelper;

/**
 * Created by timtim on 28/12/2017.
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
            Intent intent = new Intent(this,
                    PreferencesHelper.getInstance(this).isAuthed() ?
                            MainActivity.class
                            :
                            LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}