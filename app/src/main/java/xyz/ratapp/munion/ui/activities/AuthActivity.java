package xyz.ratapp.munion.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.util.List;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragment;
import xyz.ratapp.munion.controllers.auth.AuthController;

/**
 * Created by timtim on 24/12/2017.
 */

public class AuthActivity extends MaterialIntroActivity {

    public final static String ACTION_DO_AUTH =
            "xyz.ratapp.munion.ACTION_DO_AUTH";
    public static final String RESPONSE_EXTRA_CHAT_ENTITY_ID =
            "RESPONSE_EXTRA_CHAT_ENTITY_ID";
    public final static String EXTRA_PHONE_NUMBER = "phone_number";
    public final static String EXTRA_PASSWORD = "password";
    public final static int REQUEST_AUTH_CODE = 73;


    public static Intent getAuthIntent(String phone, String password) {
        Intent intent = new Intent(ACTION_DO_AUTH);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_PHONE_NUMBER, phone);
        extras.putString(EXTRA_PASSWORD, password);
        intent.putExtras(extras);

        return intent;
    }


    private AuthController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        controller = new AuthController(this);
        controller.onCreate();
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        controller.onStart();
    }

    public void setupFragments(List<SlideFragment> fragments) {
        if(fragments != null) {
            for (SlideFragment fragment : fragments) {
                addSlide(fragment);
            }
        }
        else {
            finish();
        }
    }

    public void showToast(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_SHORT).
                show();
    }

    public void showToast(@StringRes int message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_SHORT).
                show();
    }

    public void showLongToast(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).
                show();
    }

    public void showLongToast(@StringRes int message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).
                show();
    }

}
