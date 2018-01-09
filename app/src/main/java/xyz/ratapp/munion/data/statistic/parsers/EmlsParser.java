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

public class EmlsParser implements Runnable {

    private StatisticParser statisticParser;
    private Context context;
    private long id;
    private boolean fromCian;
    private DataCallback<Float[]> callback;
    private boolean wasLoad = false;

    public EmlsParser(StatisticParser statisticParser, Context context, long id,
                      boolean fromCian, DataCallback<Float[]> callback) {
        this.statisticParser = statisticParser;
        this.context = context;
        this.id = id;
        this.fromCian = fromCian;
        this.callback = callback;
    }

    @Override
    public void run() {
        statisticParser.isWebViewMuted = true;

        String loginUrl = "https://emls.ru/spb/term/index.php?pageID=21";
        String emlsLogin = context.getString(R.string.emls_login);
        String emlsPassword = context.getString(R.string.emls_password);
        String loginJsCode = String.format(Locale.getDefault(),
                "document.getElementsByName(\'login\')[0].value=\'%s\';\n" +
                        "document.getElementsByName(\'password\')[0].value=\'%s\';\n" +
                        "document.forms[\'auth\'].submit();",
                emlsLogin, emlsPassword);
        String url = String.format(Locale.getDefault(),
                "https://emls.ru/spb/term/index.php?module=38&q=0&p=0&id=%d&swm=1&d=1&a=3&action=stat", id);
        String jsResultCode = "'<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'";

        AuthWebView wv = statisticParser.setWebView(false, false);

        wv.loginAndExecuteJs(loginUrl, loginJsCode, url, "",
                jsResultCode, new AuthWebView.JSInterfaceCallback() {

                    @Override
                    @JavascriptInterface
                    public void sendResult(String result) {
                        if (!wasLoad) {
                            wasLoad = true;
                            if (result != null && !result.isEmpty()) {

                                int size = 0;
                                float cian = -1, emls = -1;
                                int i = result.indexOf("Всего просмотров на emls: ");
                                if (i != -1) {
                                    result = result.substring(i + 26);
                                    emls = Float.parseFloat(result.substring(0, result.indexOf(',')));
                                    size++;
                                }

                                i = result.indexOf("просмотров на cian.ru ");
                                if (i != -1) {
                                    result = result.substring(i + 22);
                                    cian = Float.parseFloat(result.substring(0, result.indexOf("<br>")));
                                    size++;
                                }

                                if (size == 2) {
                                    next();
                                    callback.onSuccess(new Float[]{emls, cian});
                                } else if (size == 1) {
                                    next();
                                    callback.onSuccess(new Float[]{emls});
                                } else {
                                    next();
                                    callback.onFailed(new Throwable("cant load emls"));
                                }

                            } else {
                                next();
                                callback.onFailed(new Throwable("cant load emls"));
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
