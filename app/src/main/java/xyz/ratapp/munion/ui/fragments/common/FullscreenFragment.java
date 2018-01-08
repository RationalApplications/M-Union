package xyz.ratapp.munion.ui.fragments.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import xyz.ratapp.munion.ui.activities.MainActivity;

/**
 * <p>Date: 07.11.17</p>
 *
 * @author Simon
 */

public abstract class FullscreenFragment extends BaseFragment implements Runnable {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen();
        registerSystemUiVisibility();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterSystemUiVisibility();
        exitFullscreen(getActivity());
    }

    @Override
    public void run() {
        setFullscreen();
    }

    public void setFullscreen() {
        setFullscreen(getActivity());
    }

    public void setFullscreen(Activity activity) {
        ((MainActivity)activity).getNavigation().hideBottomNavigation(true);
        ((AppCompatActivity)activity).getSupportActionBar().hide();
    }

    public void exitFullscreen(Activity activity) {
        ((MainActivity)activity).getNavigation().restoreBottomNavigation(true);
        ((AppCompatActivity)activity).getSupportActionBar().show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void registerSystemUiVisibility() {
        final View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                setFullscreen();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void unregisterSystemUiVisibility() {
        final View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(null);
    }
}