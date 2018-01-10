package xyz.ratapp.munion.data;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import com.google.gson.JsonObject;

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
import xyz.ratapp.munion.data.pojo.HypothecData;
import xyz.ratapp.munion.data.pojo.Lead;
import xyz.ratapp.munion.data.pojo.LeadListResponse;
import xyz.ratapp.munion.data.pojo.Statistics;
import xyz.ratapp.munion.data.retrofit.BitrixAPI;
import xyz.ratapp.munion.data.statistic.StatisticLoader;
import xyz.ratapp.munion.helpers.FileHelper;
import xyz.ratapp.munion.helpers.PreferencesHelper;
import xyz.ratapp.munion.ui.views.LoadingDialog;


/**
 * Created by timtim on 27/12/2017.
 */

public class DataController extends DataContainer {

    private static final DataController ourInstance = new DataController();

    public static DataController getInstance(Context context) {
        ourInstance.context = context;

        if (ourInstance.retrofit == null ||
                ourInstance.api == null ||
                ourInstance.hypothecId == null) {
            ourInstance.retrofit = new Retrofit.Builder().
                    addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BitrixAPI.getBaseUrl(context))
                    .build();
            ourInstance.api = ourInstance.retrofit.create(BitrixAPI.class);
            ourInstance.hypothecId = PreferencesHelper.
                    getInstance(context).getHypothecId();
        }

        return ourInstance;
    }

    private Retrofit retrofit = null;
    private BitrixAPI api = null;


    private DataController() {

    }

    @Override
    public void getUser(DataCallback<Lead> callback) {
        if (getUser() != null) {
            callback.onSuccess(getUser());
        } else if (phone != null) {
            loadUser(phone, callback);
        } else {
            callback.onFailed(new Throwable());
        }
    }

    @Override
    public void refreshUser(DataCallback<Lead> callback) {
        if (phone != null) {
            loadUser(phone, callback);
        } else {
            callback.onFailed(new Throwable());
        }
    }

    @Override
    public void getStatistics(DataCallback<Statistics> callback) {
        if (getStatistics() != null) {
            callback.onSuccess(getStatistics());
        } else {
            callback.onFailed(new Throwable());
        }
    }

    @Override
    protected void setHypothec(String name, String phone) {
        HypothecData hypothec = getHypothec();

        if (hypothec != null) {
            hypothec.setName(name);
            hypothec.setPhone(phone);
            saveHypothecToDisk();
        } else {
            loadHypothec(new DataCallback<HypothecData>() {
                @Override
                public void onSuccess(HypothecData data) {
                    data.setName(name);
                    data.setPhone(phone);
                    saveHypothecToDisk();
                }

                @Override
                public void onFailed(Throwable thr) {
                    HypothecData hypothec = new HypothecData();
                    hypothec.setName(name);
                    hypothec.setPhone(phone);
                    DataController.this.hypothec = hypothec;
                    saveHypothecToDisk();
                }
            });
        }
    }

    @Override
    public void loadHypothec(DataCallback<HypothecData> callback) {
        if (hypothecId != null) {
            HypothecData result = new HypothecData();

            getUser(new DataCallback<Lead>() {
                @Override
                public void onSuccess(Lead data) {
                    result.setName(data.getName());
                    result.setPhone(data.getPhones().get(0).getPhone());
                    //load contact data
                    loadContactComments(new DataCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            result.setComments(data);
                            callback.onSuccess(result);
                        }

                        @Override
                        public void onFailed(Throwable thr) {
                            callback.onFailed(thr);
                        }
                    });

                }

                @Override
                public void onFailed(Throwable thr) {
                    //try to load hypothec data from disk
                    HypothecData hData = getHypothec();
                    if(hData != null) {
                        loadContactComments(new DataCallback<String>() {
                            @Override
                            public void onSuccess(String data) {
                                hData.setComments(data);
                                callback.onSuccess(hData);
                            }

                            @Override
                            public void onFailed(Throwable thr) {
                                callback.onFailed(thr);
                            }
                        });
                    }
                    else {
                        callback.onFailed(thr);
                    }
                }
            });
        } else {
            callback.onFailed(new Throwable());
        }
    }

    @Override
    public void loadContactComments(DataCallback<String> callback) {
        if(hypothecId == null) {
            callback.onFailed(new Throwable());
        }

        api.loadContactComments(hypothecId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call,
                                   Response<JsonObject> response) {
                JsonObject data = response.body();
                if (data == null) {
                    callback.onFailed(new Throwable("Server connection error"));
                    return;
                }

                callback.onSuccess(data.get("result").getAsJsonObject().
                        get("COMMENTS").getAsString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onFailed(t);
            }
        });
    }

    @Override
    public void loadStatistics(LoadingDialog dialog, DataCallback<Statistics> callback) {
        if (phone != null) {
            loadStatistics(phone, dialog, callback);
        } else {
            callback.onFailed(new Throwable());
        }
    }

    @Override
    void loadStatistics(String phone,
                        LoadingDialog dialog,
                        DataCallback<Statistics> callback) {

        this.phone = phone;

        loadUser(phone, new DataCallback<Lead>() {
            @Override
            public void onSuccess(Lead user) {
                boolean hasStatsBefore = DataController.this.user.getStatistics() != null;
                boolean loadRecords = !hasStatsBefore ||
                        !DataController.this.user.getTalksRecords()
                                .equals(user.getTalksRecords());

                String[] urls = Html.fromHtml(user.getComments()).
                        toString().split("\\s");

                new StatisticLoader(
                        dialog,
                        user.getTitle(),
                        Arrays.asList(urls),
                        user.getTalksRecords(),
                        user.getCallsCount(), user.getLooksCount(),
                        loadRecords,
                        new DataCallback<Statistics>() {
                            @Override
                            public void onSuccess(Statistics data) {
                                user.setStatistics(data);
                                saveUserToDisk();
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
                if (data == null) {
                    callback.onFailed(new Throwable("Server connection error"));
                    return;
                }

                if (data.getLeads() != null && data.getLeads().size() >= 1) {
                    String id = data.getLeads().get(0).getId() + "";
                    api.loadLeadByInvitedId(id).enqueue(new Callback<LeadListResponse>() {
                        @Override
                        public void onResponse(Call<LeadListResponse> call,
                                               Response<LeadListResponse> response) {
                            Lead user = data.getLeads().get(0);
                            if(response.body() != null) {
                                user.setCountOfInvitedFriends(response.body().getTotal());
                                setUser(user);
                                saveUserToDisk();
                                callback.onSuccess(DataController.this.user);
                            }
                            else {
                                callback.onFailed(new Throwable());
                            }
                        }

                        @Override
                        public void onFailure(Call<LeadListResponse> call, Throwable t) {
                            callback.onFailed(new Throwable());
                        }
                    });
                } else {
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

    public void setFbUserEntity(@NotNull String entityId) {
        user.setFirebaseEntity(entityId);
        saveUserToDisk();
    }

    public void createContact(@NotNull String name,
                              @NotNull String phone,
                              Callback<JsonObject> callback) {
        String status = context.getString(R.string.hypothec_start_status);
        api.createContact(name, phone.substring(2), status).enqueue(callback);
        setHypothec(name, phone);
    }
}
