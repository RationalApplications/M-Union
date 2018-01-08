package xyz.ratapp.munion.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import xyz.ratapp.munion.helpers.ChatSDKHelper;
import xyz.ratapp.munion.helpers.PreferencesHelper;

/**
 * <p>Date: 03.12.17</p>
 *
 * @author Simon
 */

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            boolean isAuthed = PreferencesHelper.getInstance(this).isAuthed();
            String chatThread = PreferencesHelper.getInstance(this).getChatThreadEntityId();

            if (isAuthed && !chatThread.isEmpty()) {
                ChatSDKHelper.authWithUser(this, currentUser);
            }
        }
    }
}
