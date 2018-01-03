package xyz.ratapp.munion.data;

import android.content.Context;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.ratapp.munion.R;
import xyz.ratapp.munion.controllers.interfaces.DataCallback;
import xyz.ratapp.munion.controllers.interfaces.ListCallback;
import xyz.ratapp.munion.data.audio.BitrixOAuthAudiosTask;
import xyz.ratapp.munion.data.pojo.Lead;
import xyz.ratapp.munion.data.pojo.LeadListResponse;
import xyz.ratapp.munion.data.pojo.Statistics;
import xyz.ratapp.munion.data.retrofit.BitrixAPI;
import xyz.ratapp.munion.data.statistic.StatisticLoader;
import xyz.ratapp.munion.data.statistic.StatisticParser;
import xyz.ratapp.munion.helpers.FileHelper;


/**
 * Created by timtim on 27/12/2017.
 */

public class DataController extends DataContainer {

    private static final DataController ourInstance = new DataController();

    public static DataController getInstance(Context context) {
        ourInstance.context = context;

        if(ourInstance.retrofit == null ||
                ourInstance.api == null) {
            ourInstance.retrofit = new Retrofit.Builder().
                    addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BitrixAPI.getBaseUrl(context))
                    .build();
            ourInstance.api = ourInstance.retrofit.create(BitrixAPI.class);
        }

        return ourInstance;
    }

    private Retrofit retrofit = null;
    private BitrixAPI api = null;


    private DataController() {

    }

    @Override
    public void getUser(DataCallback<Lead> callback) {
        if(getUser() != null) {
            callback.onSuccess(getUser());
        }
        else if(phone != null) {
            loadUser(phone, callback);
        }
        else {
            callback.onFailed(new Throwable());
        }
    }

    @Override
    public void getStatistics(DataCallback<Statistics> callback) {
        if(getStatistics() != null) {
            callback.onSuccess(getStatistics());
        }
        else {
            callback.onFailed(new Throwable());
        }
    }

    @Override
    public void loadStatistics(DataCallback<Statistics> callback) {
        if(phone != null) {
            loadStatistics(phone, callback);
        }
        else {
            callback.onFailed(new Throwable());
        }
    }

    @Override
    void loadStatistics(String phone,
                        DataCallback<Statistics> callback) {

        this.phone = phone;

        loadUser(phone, new DataCallback<Lead>() {
            @Override
            public void onSuccess(Lead user) {
                boolean hasUserBefore = getUser() != null &&
                                DataController.this.user.getStatistics() != null;
                boolean loadRecords = !hasUserBefore ||
                        !DataController.this.user.getTalksRecords()
                                .equals(user.getTalksRecords());

                String[] urls = user.getComments().
                        replace("<pre>", "").
                        replace("</pre>", "").split("<br>");

                new StatisticLoader(
                        context,
                        user.getTitle(),
                        Arrays.asList(urls),
                        user.getTalksRecords(),
                        user.getCallsCount(), user.getLooksCount(),
                        loadRecords, new DataCallback<Statistics>() {
                    @Override
                    public void onSuccess(Statistics data) {
                        callback.onSuccess(data);
                    }

                    @Override
                    public void onFailed(Throwable thr) {
                        callback.onFailed(thr);
                    }
                }).run();

            }

            @Override
            public void onFailed(Throwable thr) {
                callback.onFailed(thr);
            }
        });

    }

    @Override
    public void loadUser(String phone, DataCallback<Lead> callback) {

        this.phone = phone;

        api.loadLeadByPhone(phone).enqueue(new Callback<LeadListResponse>() {
            @Override
            public void onResponse(Call<LeadListResponse> call,
                                   Response<LeadListResponse> response) {

                LeadListResponse data = response.body();
                if(data == null) {
                    callback.onFailed(new Throwable("Server connection error"));
                    return;
                }

                if(data.getLeads() != null && data.getLeads().size() >= 1) {
                    user = data.getLeads().get(0);
                    saveUserToDisk();
                    callback.onSuccess(user);
                }
                else {
                    callback.onFailed(new Throwable("Server not found you :c"));
                }
            }

            @Override
            public void onFailure(Call<LeadListResponse> call, Throwable t) {
                callback.onFailed(t);
            }
        });
    }

    public void setUserPhotoUri(@NotNull Uri imageUri) throws Exception {
        String path = FileHelper.saveUriToFile(imageUri, context,
                getAppFolder(), "avatar.png");
        user.setPhotoUri(path);
        saveUserToDisk();
    }

}
