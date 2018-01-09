package xyz.ratapp.munion.data.statistic.parsers;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import java.util.Locale;

import xyz.ratapp.munion.R;
import xyz.ratapp.munion.controllers.interfaces.DataCallback;
import xyz.ratapp.munion.ui.views.AuthWebView;

/**
 * Created by timtim on 04/01/2018.
 */

public class RucountryParser implements Runnable {

    private StatisticParser statisticParser;
    private Context context;
    private long id;
    private boolean wasLoad = false;
    private DataCallback<Float[]> callback;

    public RucountryParser(StatisticParser statisticParser, Context context,
                           long id, DataCallback<Float[]> callback) {
        this.statisticParser = statisticParser;
        this.context = context;
        this.id = id;
        this.callback = callback;
    }

    @Override
    public void run() {
        statisticParser.isWebViewMuted = true;
        String loginUrl = "http://spb.rucountry.ru/logon.html";
        String rucountryLogin = context.getString(R.string.rucountry_login);
        String rucountryPassword = context.getString(R.string.rucountry_password);
        String loginJsCode = String.format(Locale.getDefault(),
                "document.getElementById(\'auth-email\').value=\'%s\';\n" +
                        "document.getElementById(\'auth-password\').value=\'%s\';\n" +
                        "shared.Logon('/user/profile','form-auth-main');",
                rucountryLogin, rucountryPassword);
        String url = "http://spb.rucountry.ru/user/ads";
        //user-ad-filter-code
        String jsCode = String.format(Locale.getDefault(), "tmp.ListContent(\'myvtorichka\', false, {action: \'vtorichka\', pageid: 1, pagesize: 15, filter:\"{\\\"sd\\\":\\\"\\\",\\\"fd\\\":\\\"\\\",\\\"code\\\":\\\"%d\\\",\\\"status\\\":\\\"1\\\",\\\"type\\\":\\\"1\\\"}\", orderby:\'datecreated_desc\', url:\'\', route:\'user/ads\', uid:\'\'});",
                id);
        String jsResultCode = "document.getElementsByClassName(\'glyphicons no-js eye_open margin-bottom-5\')[0].textContent";

        AuthWebView wv = statisticParser.setWebView(true);

        wv.loginAndExecuteJs(loginUrl, loginJsCode, url, jsCode,
                jsResultCode, new AuthWebView.JSInterfaceCallback() {

                    @Override
                    @JavascriptInterface
                    public void sendResult(String result) {
                        if(!wasLoad) {
                            wasLoad = true;
                            if (result != null && !result.isEmpty()) {
                                int i = result.indexOf("Просмотры ");
                                if (i != -1) {
                                    result = result.substring(i + 10);
                                    float data = Float.parseFloat(result);
                                    callback.onSuccess(new Float[]{data});
                                    next();
                                } else {
                                    callback.onFailed(new Throwable("cant load rucountry"));
                                    next();
                                }
                            } else {
                                callback.onFailed(new Throwable("cant load rucountry"));
                                next();
                            }
                        }
                    }
                });
    }

    private void next() {
        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = () -> {
            statisticParser.isWebViewMuted = false;
            statisticParser.nextTask();
        };
        mainHandler.post(myRunnable);
    }

}
