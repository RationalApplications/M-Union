package xyz.ratapp.munion.data.statistic;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import xyz.ratapp.munion.R;
import xyz.ratapp.munion.controllers.interfaces.DataCallback;
import xyz.ratapp.munion.ui.views.AuthWebView;

/**
 * Created by timtim on 02/01/2018.
 */

public class StatisticParser {

    static void parse(Context context, String url,
                       DataCallback<Float> callback) {

        if(url.startsWith("http://www.emls.ru/")) {
            url = url.replaceAll("http://www\\.emls\\.ru/", "");
            long id = url.endsWith("/") ?
                    Long.parseLong(url.substring(0, url.length() - 1))
                    :
                    Long.parseLong(url);

            parseEmls(context, id, callback);
        }
        else if(url.startsWith("http://www.mirkvartir.ru/")) {
            url = url.replaceAll("http://www\\.mirkvartir\\.ru/", "");
            long id = url.endsWith("/") ?
                    Long.parseLong(url.substring(0, url.length() - 1))
                    :
                    Long.parseLong(url);

            parseMirkvartir(id, callback);
        }
        else if(url.startsWith("http://www.restate.ru/")) {
            parseRestate(url, callback);
        }
        else if(url.startsWith("http://spb.rucountry.ru/")) {
            long id = Long.parseLong(url.replaceAll("http://spb\\.rucountry\\.ru/vtorichka/", "").
                    replace(".html", ""));

            parseRucountry(context, id, callback);
        }
        else if(url.startsWith("https://realty.yandex.ru/")) {
            parseYandex(context, url, callback);
        }
        else {
            callback.onSuccess(0f);
        }

    }

    private static void parseRestate(String url, DataCallback<Float> callback) {
        new Thread(() -> {
            try {
                Document html = Jsoup.connect(url).
                        timeout(10000).
                        validateTLSCertificates(false).get();

                callback.onSuccess(Float.parseFloat(html.body().
                        getElementsByClass("rbobj").
                        last().getElementsByIndexEquals(3).text()));
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFailed(new Throwable("cant load restate"));
            }
        }).start();
    }

    private static void parseRucountry(Context context, long id,
                                        DataCallback<Float> callback) {
        String loginUrl = "http://spb.rucountry.ru/";
        String rucountryLogin = context.getString(R.string.rucountry_login);
        String rucountryPassword = context.getString(R.string.rucountry_password);
        String loginJsCode = String.format(Locale.getDefault(),
                "tmp.auth('/user/profile');" +
                        "document.getElementById(\'auth-email\').value=\'%s\';\n" +
                        "document.getElementById(\'auth-password\').value=\'%s\';\n" +
                        "document.getElementById[\'form-auth\'].submit();",
                rucountryLogin, rucountryPassword);
        String url = String.format(Locale.getDefault(),
                "http://spb.rucountry.ru/vtorichka/21537323.html?type=stat&id=%d", id);
        String jsCode = "'<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'";

        AuthWebView wv = new AuthWebView(context);

        wv.loginAndExecuteJs(loginUrl, loginJsCode,
                url, jsCode, new AuthWebView.JSInterfaceCallback() {
                    @Override
                    public void sendResult(String result) {
                        if(result != null && !result.isEmpty()) {
                            callback.onSuccess(0f);
                        }
                        else {
                            callback.onFailed(new Throwable("cant load rucountry"));
                        }
                    }
                } );

    }

    private static void parseYandex(Context context, String url,
                                     DataCallback<Float> callback) {
        //сделать через WebView
        AuthWebView wv = new AuthWebView(context);
        wv.executeJsAfterLoadingPage(url,
                "document.getElementsByClassName('offer-card__views-count')[0]",
                new AuthWebView.JSInterfaceCallback() {
                    @Override
                    public void sendResult(String result) {
                        if(result != null && !result.isEmpty()) {
                            callback.onSuccess(0f);
                        }
                        else {
                            callback.onFailed(new Throwable("cant load yandex"));
                        }
                    }
                });

    }

    private static void parseMirkvartir(long id, DataCallback<Float> callback) {
        new Thread(() -> {
            try {
                String url = "http://www.mirkvartir.ru/handlers/getEstateViewCount.ashx?estateId=" + id;

                Gson gson = new Gson();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new URL(url).
                                openConnection().getInputStream()));

                callback.onSuccess(gson.fromJson(reader, JsonObject.class).
                        get("EventCount").getAsFloat());
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFailed(new Throwable("cant load Mirkvartir"));
            }
        }).start();
    }

    private static void parseEmls(Context context, long id,
                                   DataCallback<Float> callback) {
        String loginUrl = "http://emls.ru/spb/term/index.php?pageID=21";
        String emlsLogin = context.getString(R.string.emls_login);
        String emlsPassword = context.getString(R.string.emls_password);
        String loginJsCode = String.format(Locale.getDefault(),
                "document.getElementsByName(\'login\')[0].value=\'%s\';\n" +
                "document.getElementsByName(\'password\')[0].value=\'%s\';\n" +
                "document.forms[\'auth\'].submit();",
                emlsLogin, emlsPassword);
        String url = String.format(Locale.getDefault(),
                "https://emls.ru/spb/term/index.php?module=38&q=0&p=0&id=%d&swm=1&d=1&a=3&action=stat", id);
        String jsCode = "'<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'";

        AuthWebView wv = new AuthWebView(context);

        wv.loginAndExecuteJs(loginUrl, loginJsCode,
                url, jsCode, new AuthWebView.JSInterfaceCallback() {
                    @Override
                    public void sendResult(String result) {
                        if(result != null && !result.isEmpty()) {
                            callback.onSuccess(0f);
                        }
                        else {
                            callback.onFailed(new Throwable("cant load emls"));
                        }
                    }
                } );

    }

}
