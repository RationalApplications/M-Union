package xyz.ratapp.munion.data.statistic.parsers;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import xyz.ratapp.munion.R;
import xyz.ratapp.munion.controllers.interfaces.DataCallback;
import xyz.ratapp.munion.data.statistic.parsers.CianParser;
import xyz.ratapp.munion.data.statistic.parsers.EmlsParser;
import xyz.ratapp.munion.data.statistic.parsers.RucountryParser;
import xyz.ratapp.munion.data.statistic.parsers.YandexParser;
import xyz.ratapp.munion.ui.views.AuthWebView;

/**
 * Created by timtim on 02/01/2018.
 */

public class StatisticParser {

    private Context context;
    AuthWebView wv = null;
    boolean isWebViewMuted = false;
    private Queue<Runnable> pool = new ArrayDeque<>();

    public StatisticParser(Context context) {
        this.context = context;
    }

    public void parse(String url, DataCallback<Float> callback) {

        if (wv == null) {
            wv = new AuthWebView(context, true, false);
        }

        if (url.contains("www.")) {
            url = url.replaceFirst("www\\.", "");
        }

        if (url.startsWith("http://emls.ru/")) {
            url = url.replaceAll("http://emls\\.ru/fullinfo/1/", "");
            url = url.replaceAll("\\.html", "");
            long id = url.endsWith("/") ?
                    Long.parseLong(url.substring(0, url.length() - 1))
                    :
                    Long.parseLong(url);

            //parseEmls(context, id, false, callback);
            addTask(new EmlsParser(this, context, id, false, callback));
        } else if (url.startsWith("https://spb.cian.ru/")) {
            //parseCian(context, url, callback);
            addTask(new CianParser(this, context, url, callback));
        } else if (url.startsWith("https://spb.sterium.com/")) {
            parseSterium(url, callback);
        } else if (url.startsWith("http://mirkvartir.ru/")) {
            url = url.replaceAll("http://mirkvartir\\.ru/", "");
            long id = url.endsWith("/") ?
                    Long.parseLong(url.substring(0, url.length() - 1))
                    :
                    Long.parseLong(url);

            parseMirkvartir(id, callback);
        } else if (url.startsWith("http://restate.ru/")) {
            parseRestate(url, callback);
        } else if (url.startsWith("http://spb.rucountry.ru/")) {
            long id = Long.parseLong(url.replaceAll("http://spb\\.rucountry\\.ru/vtorichka/", "").
                    replace(".html", ""));

            //parseRucountry(context, id, callback);
            addTask(new RucountryParser(this, context, id, callback));
        } else if (url.startsWith("https://realty.yandex.ru/")) {
            //parseYandex(context, url, callback);
            addTask(new YandexParser(this, context, url, callback));
        } else {
            callback.onSuccess(0f);
        }

    }

    void addTask(Runnable task) {
        if (!isWebViewMuted) {
            task.run();
        } else {
            pool.add(task);
        }
    }

    void nextTask() {
        Runnable runnable = pool.poll();

        if (runnable != null) {
            runnable.run();
        }
    }

    private void parseSterium(String url, DataCallback<Float> callback) {
        new Thread(() -> {
            try {
                Document html = Jsoup.connect(url).
                        timeout(10000).
                        validateTLSCertificates(false).get();

                String looks = html.body().
                        getElementsByClass("looks").
                        last().text();
                callback.onSuccess(Float.parseFloat(looks.substring(0, looks.indexOf(' '))));
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFailed(new Throwable("cant load restate"));
            }
        }).start();
    }

    private void parseRestate(String url, DataCallback<Float> callback) {
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

    private void parseMirkvartir(long id, DataCallback<Float> callback) {
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

}
