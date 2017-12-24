package xyz.ratapp.munion.ui.activities.auth;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.util.concurrent.TimeUnit;

import xyz.ratapp.munion.ui.activities.SplashActivity;

/**
 * Created by timtim on 24/12/2017.
 */

public class AuthTask extends AsyncTask<Void, Void, Void> {

    private ProgressBar pb;
    private AuthActivity activity;

    public AuthTask(ProgressBar pb,
                    AuthActivity activity) {
        this.pb = pb;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            TimeUnit.SECONDS.sleep(3);
            //TODO: Здесь проверяем наличие в базе клиента
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        pb.setVisibility(View.GONE);
        activity.startPhoneNumberVerification();
        showCodeDialog(activity);
    }

    static void showCodeDialog(Activity activity) {
        VerificationDialog dialog = new VerificationDialog();
        dialog.show(activity.getFragmentManager(), "dialog");
    }

}