package xyz.ratapp.munion.data;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
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
        ourInstance.retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BitrixAPI.getBaseUrl(context))
                .build();
        return ourInstance;
    }


    private Retrofit retrofit;
    private BitrixAPI api = retrofit.create(BitrixAPI.class);
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

    public void setUserPhotoUri(@NotNull String imagePath) {
        user.setPhotoUri(imagePath);
        saveUser();
    }

    private Statistics loadStatistics() {
        user = loadUser();

        if(user != null && user.getStatistics() != null) {
            return user.getStatistics();
        }
        else {
            return loadStatisticsFromExcel();
        }
    }

    private Statistics loadStatisticsFromExcel() {
        /*
        try {
            InputStream input = context.getAssets().open("stats.xlsx");
            Workbook wb = WorkbookFactory.create(input);
            Sheet mySheet = wb.getSheetAt(0);
            Iterator<Row> rowIter = mySheet.rowIterator();
            Log.e("MyTag", mySheet.getRow(19).getCell(0).getStringCellValue());

        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        return null;
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
                //detect app folder
                PackageManager m = context.getPackageManager();
                String t = context.getPackageName();
                PackageInfo p = m.getPackageInfo(t, 0);
                String dir = p.applicationInfo.dataDir;

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
            PackageManager m = context.getPackageManager();
            String t = context.getPackageName();
            PackageInfo p = m.getPackageInfo(t, 0);
            String dir = p.applicationInfo.dataDir;

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

}
