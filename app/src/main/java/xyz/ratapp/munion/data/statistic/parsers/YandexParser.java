package xyz.ratapp.munion.data.statistic.parsers;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import xyz.ratapp.munion.controllers.interfaces.DataCallback;
import xyz.ratapp.munion.ui.views.AuthWebView;

/**
 * Created by timtim on 04/01/2018.
 */

public class YandexParser implements Runnable {

    private StatisticParser statisticParser;
    private Context context;
    private String url;
    private boolean wasLoad = false;
    private DataCallback<Float> callback;

    public YandexParser(StatisticParser statisticParser, Context context,
                        String url, DataCallback<Float> callback) {
        this.statisticParser = statisticParser;
        this.context = context;
        this.url = url;
        this.callback = callback;
    }

    @Override
    public void run() {
        statisticParser.isWebViewMuted = true;
        statisticParser.wv = new AuthWebView(context, false, true);
        statisticParser.wv.executeJsAfterLoadingPage(url, "",
                "document.getElementsByTagName('html')[0].innerHTML",
                new AuthWebView.JSInterfaceCallback() {

                    @Override
                    @JavascriptInterface
                    public void sendResult(String result) {
                        if(!wasLoad) {
                            wasLoad = true;
                            if (result != null && !result.isEmpty()) {
                                int i = result.indexOf("Просмотры: ");
                                if (i != -1) {
                                    result = result.substring(i + 11);
                                    float data = Float.parseFloat(result.substring(0, result.indexOf("</div>")));
                                    callback.onSuccess(data);
                                    next();
                                } else {
                                    callback.onFailed(new Throwable("cant load yandex"));
                                    next();
                                }
                            } else {
                                callback.onFailed(new Throwable("cant load yandex"));
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
