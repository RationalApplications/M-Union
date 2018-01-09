package xyz.ratapp.munion.data.statistic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import xyz.ratapp.munion.R;
import xyz.ratapp.munion.controllers.interfaces.DataCallback;
import xyz.ratapp.munion.controllers.interfaces.ListCallback;
import xyz.ratapp.munion.data.statistic.audio.BitrixOAuthAudiosTask;
import xyz.ratapp.munion.data.pojo.Lead;
import xyz.ratapp.munion.data.pojo.Statistics;
import xyz.ratapp.munion.data.statistic.parsers.StatisticParser;
import xyz.ratapp.munion.ui.views.BitrixAuthWebView;

/**
 * Created by timtim on 03/01/2018.
 */

public class StatisticLoader implements Runnable {

    private static final String AUTH_URL_MASK =
            "https://m-union.bitrix24.ru/oauth/authorize/?client_id=%s&response_type=%d&redirect_uri=%s";
    private static final int RESPONSE_TYPE = 13;
    public static final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);


    private Context context;
    private final AlertDialog dialog;
    private String objectName;
    private List<String> dataUrls;
    private List<Lead.Record> records;
    private int callsCount;
    private int looksCount;
    private boolean loadRecords;
    private DataCallback<Statistics> callback;
    private Statistics result;


    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scopes;


    private volatile boolean wasTalksLoad = false;
    private volatile boolean wasStatisticLoad = false;
    //ключ = номер попытки, значение = количество загруженных элементов
    private volatile Map<Integer, Integer> counter = new HashMap<>();


    public StatisticLoader(AlertDialog dialog, String objectName,
                           List<String> dataUrls, List<Lead.Record> records,
                           int callsCount, int looksCount,
                           boolean loadRecords, DataCallback<Statistics> callback) {
        this.dialog = dialog;
        this.context = dialog.getContext();
        initData(context);
        this.objectName = objectName;
        this.dataUrls = dataUrls;
        this.records = records;
        this.callsCount = callsCount;
        this.looksCount = looksCount;
        this.loadRecords = loadRecords;
        this.callback = callback;
    }

    private void initData(Context context) {
        if(clientId == null || clientSecret == null ||
                redirectUri == null || scopes == null) {
            clientId = context.getString(R.string.client_id);
            clientSecret = context.getString(R.string.client_secret);
            redirectUri = context.getString(R.string.redirect_uri);
            scopes = context.getString(R.string.scopes);
        }
    }

    @Override
    public void run() {
        result = new Statistics(this);
        result.setCallsCount(callsCount);
        result.setLooksCount(looksCount);
        result.setObjectName(objectName);

        loadStatistic(0);

        if(loadRecords) {
            loadTalks(0);
        }
    }

    private void loadStatistic(int attempt) {
        if(attempt < 2) {
            StatisticParser parser = new StatisticParser(dialog);

            counter.put(attempt, 0);
            Map<String, Float> data = new HashMap<>();

            for (String url : dataUrls) {
                if(url.contains("cian.ru")) {
                    continue;
                }

                parser.parse(url, new DataCallback<Float[]>() {
                    @Override
                    public void onSuccess(Float result[]) {
                        Integer cnt = counter.get(attempt);
                        counter.put(attempt, cnt + 1);
                        data.put(url, result[0]);

                        if(result.length > 1) {
                            cnt = counter.get(attempt);
                            counter.put(attempt, cnt + 1);
                            String cianUrl = "";

                            for (String dataUrl : dataUrls) {
                                if(dataUrl.contains("cian.ru")) {
                                    cianUrl = url;
                                    break;
                                }
                            }

                            data.put(cianUrl, result[1]);
                        }

                        if (cnt + 1 == dataUrls.size()) {
                            float views = 0;
                            for (Map.Entry<String, Float> entry : data.entrySet()) {
                                views += entry.getValue();
                            }

                            StatisticLoader.this.result.setViewsCount((int) views);
                            StatisticLoader.this.result.setData(data);
                            wasStatisticLoad = true;
                            sendResult();
                        }
                    }

                    @Override
                    public void onFailed(Throwable thr) {
                        Handler mainHandler = new Handler(context.getMainLooper());

                        Runnable myRunnable = () ->
                                loadStatistic(attempt + 1);
                        mainHandler.post(myRunnable);
                    }
                });
            }
        }
        else {
            callback.onFailed(new Throwable("cant load statistics"));
        }
    }

    private void loadTalks(int attempt) {
        if(attempt < 2) {
            loadTalksWithCallback(new ListCallback<String>() {
                @Override
                public void onSuccess(List<String> data) {
                    result.setTalksUrls(data);
                    wasTalksLoad = true;
                    sendResult();
                }

                @Override
                public void onFailed(Throwable thr) {
                    loadTalks(attempt + 1);
                }
            }, records);
        }
        else {
            callback.onFailed(new Throwable("Can't load talks"));
        }
    }

    private void sendResult() {
        if(wasStatisticLoad &&
                (wasTalksLoad || !loadRecords) &&
                resultIsReady()) {
            callback.onSuccess(result);
        }
    }

    private boolean resultIsReady() {
        return result.getData() != null &&
                result.getData().size() > 0 &&
                result.getTalksUrls() != null &&
                result.getTalksUrls().size() == records.size();
    }

    private void loadTalksWithCallback(ListCallback<String> callback,
                           List<Lead.Record> talksRecords) {
        String email = context.getString(R.string.user_name);
        String password = context.getString(R.string.user_password);

        BitrixAuthWebView wv = new BitrixAuthWebView(context);
        wv.setVisibility(View.GONE);
        dialog.addContentView(wv, params);
        String url = String.format(Locale.getDefault(), AUTH_URL_MASK,
                clientId, RESPONSE_TYPE, redirectUri);
        wv.login(url, email, password, new BitrixAuthWebView.
                BitrixAuthCallback() {

            @Override
            public void onComplete(String code) {
                List<String> data =
                        authAndTakeAudiosUrls(talksRecords, code);

                if(data == null) {
                    callback.onFailed(new Throwable());
                }

                callback.onSuccess(data);
            }

            @Override
            public void onFailed() {
                callback.onFailed(new Throwable());
            }
        });
    }

    private List<String> authAndTakeAudiosUrls(List<Lead.Record> talksRecords, String code) {
        try {
            BitrixOAuthAudiosTask task = new BitrixOAuthAudiosTask();
            task.setTalksRecords(talksRecords);
            return task.execute(clientId,
                    clientSecret, redirectUri,
                    code, scopes, getAppFolder()).get();
        } catch (Exception e) {
            return null;
        }
    }

    private String getAppFolder() throws PackageManager.NameNotFoundException {
        //detect app folder
        PackageManager m = context.getPackageManager();
        String t = context.getPackageName();
        PackageInfo p = m.getPackageInfo(t, 0);
        return p.applicationInfo.dataDir;
    }
}
