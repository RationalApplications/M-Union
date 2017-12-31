package xyz.ratapp.munion.data;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.ratapp.munion.controllers.interfaces.UserCallback;
import xyz.ratapp.munion.data.pojo.Statistics;
import xyz.ratapp.munion.data.retrofit.BitrixAPI;
import xyz.ratapp.munion.data.pojo.Lead;
import xyz.ratapp.munion.data.pojo.LeadListResponse;


/**
 * Created by timtim on 27/12/2017.
 */

public class DataController {

    private static final DataController ourInstance = new DataController();
    public static final String USER_DATA = "USER_DATA.dat";

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
    private Context context;
    private Lead user;
    private Statistics statistics;

    private DataController() {

    }

    public Statistics getStatistics() {
        if(statistics == null) {
            statistics = loadStatistics();
        }

        return statistics;
    }

    public Lead getUser() {
        if (user == null) {
            user = loadUser();
        }

        return user;
    }

    public void setUserPhotoUri(@NotNull Uri imageUri) throws Exception {
        InputStream input = context.getContentResolver().openInputStream(imageUri);

        try {
            File file = new File(getAppFolder(), "avatar.png");
            file.createNewFile();

            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            } finally {
                output.close();
                user.setPhotoUri(file.getAbsolutePath());
            }
        } finally {
            input.close();
        }


        saveUser();
    }

    private Statistics loadStatistics() {

        /*loadUser(user.getPhones().get(0).getPhone(),
                new UserCallback() {
            @Override
            public void onSuccess(Lead user) {
                DataController.this.user.setCallsCount(user.getCallsCount());
                DataController.this.user.setLooksCount(user.getLooksCount());
                DataController.this.user.setTalksRecords(user.getTalksRecords());
            }

            @Override
            public void onFailed(Throwable thr) {

            }
        });*/

        if(user == null || user.getStatistics() == null) {
            HashMap<String, Float> data = new HashMap<>();
            data.put("emls.ru", 615f);
            data.put("spb.rucountry.ru", 33f);
            data.put("mirkvartir.ru", 19f);
            data.put("realty.yandex.ru", 170f);
            data.put("avito.ru", 1588f);
            data.put("restate.ru", 42f);

            user.setStatistics(220, data);
        }

        return user.getStatistics();
    }


    public void loadUser(String phone, UserCallback callback) {
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
                    saveUser();
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

    private void saveUser() {
        if(user != null) {
            try {
                String dir = getAppFolder();


                //serialize user
                FileOutputStream fos =
                        new FileOutputStream(new File(dir, USER_DATA));
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(user);
                oos.flush();
                oos.close();
            } catch (Exception e) {

            }
        }
    }

    private Lead loadUser() {
        try {
            //detect app folder
            String dir = getAppFolder();

            //deserialize user
            File f = new File(dir, USER_DATA);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream oin = new ObjectInputStream(fis);
            Object result = oin.readObject();
            oin.close();

            return ((Lead) result);
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
