package xyz.ratapp.munion.data.statistic;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import xyz.ratapp.munion.R;
import xyz.ratapp.munion.controllers.interfaces.DataCallback;
import xyz.ratapp.munion.controllers.interfaces.ListCallback;
import xyz.ratapp.munion.data.DataController;
import xyz.ratapp.munion.data.audio.BitrixOAuthAudiosTask;
import xyz.ratapp.munion.data.pojo.Lead;
import xyz.ratapp.munion.data.pojo.Statistics;
import xyz.ratapp.munion.ui.views.BitrixAuthWebView;

/**
 * Created by timtim on 03/01/2018.
 */

public class StatisticLoader implements Runnable {

    private static final String AUTH_URL_MASK =
            "https://m-union.bitrix24.ru/oauth/authorize/?client_id=%s&response_type=%d&redirect_uri=%s";
    private static final int RESPONSE_TYPE = 13;


    private Context context;
    private String objectName;
    private List<String> dataUrls;
    private List<Lead.Record> records;
    private int callsCount;
    private int looksCount;
    private boolean loadRecords;
    private DataCallback<Statistics> callback;
    Statistics result;


    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scopes;


    private volatile boolean wasTalksLoad = false;
    private volatile boolean wasStatisticLoad = false;
    private volatile int count;


    public StatisticLoader(Context context, String objectName,
                           List<String> dataUrls, List<Lead.Record> records,
                           int callsCount, int looksCount,
                           boolean loadRecords, DataCallback<Statistics> callback) {
        this.context = context;
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

        if(loadRecords) {
            loadTalks(0);
        }

        loadStatistic(0);

    }

    private void loadStatistic(int attempt) {
        if(attempt < 5) {
            count = 0;
            Map<String, Float> data = new HashMap<>();

            for (String url : dataUrls) {
                StatisticParser.parse(context, url, new DataCallback<Float>() {
                    @Override
                    public void onSuccess(Float result) {
                        count++;
                        data.put(url, result);
                        if (count == dataUrls.size()) {
                            float views = 0;
                            for (Map.Entry<String, Float> entry : data.entrySet()) {
                                views += entry.getValue();
                            }

                            StatisticLoader.this.result.setViewsCount((int) views);
                            StatisticLoader.this.result.setData(data);
                            sendResult();
                        }
                    }

                    @Override
                    public void onFailed(Throwable thr) {
                        Log.e("MyTag", thr.toString());
                        loadStatistic(attempt + 1);
                    }
                });
            }
        }
        else {
            callback.onFailed(new Throwable("cant load statistics"));
        }
    }

    private void loadTalks(int attempt) {
        if(attempt < 5) {
            loadTalksWithCallback(new ListCallback<String>() {
                @Override
                public void onSuccess(List<String> data) {
                    result.setTalksUrls(data);
                    wasStatisticLoad = true;
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
        if(wasStatisticLoad && wasTalksLoad
                && resultIsReady()) {
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
        String email = context.getString(R.string.email);
        String password = context.getString(R.string.password);

        BitrixAuthWebView wv = new BitrixAuthWebView(context);
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
