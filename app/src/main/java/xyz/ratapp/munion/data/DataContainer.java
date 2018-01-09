package xyz.ratapp.munion.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import xyz.ratapp.munion.controllers.interfaces.DataCallback;
import xyz.ratapp.munion.data.pojo.Lead;
import xyz.ratapp.munion.data.pojo.Statistics;

/**
 * Created by timtim on 02/01/2018.
 */

abstract class DataContainer {

    public static final String USER_DATA = "USER_DATA.dat";

    protected Context context;
    protected String phone;
    protected Lead user = null;
    protected Statistics statistics = null;


    protected Statistics getStatistics() {
        if(statistics == null &&
                getUser() != null) {
            statistics = user.getStatistics();
        }

        return statistics;
    }

    protected Lead getUser() {
        if (user == null) {
            user = loadUserFromDisk();
        }

        return user;
    }

    protected abstract void getUser(DataCallback<Lead> callback);

    protected abstract void getStatistics(DataCallback<Statistics> callback);

    abstract void loadStatistics(String phone,
                                 AlertDialog dialog,
                                 DataCallback<Statistics> callback);

    public abstract void loadUser(String phone,
                                  DataCallback<Lead> callback);

    public abstract void loadStatistics(AlertDialog dialog,
                                        DataCallback<Statistics> callback);


    protected void saveUserToDisk() {
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

    protected Lead loadUserFromDisk() {
        try {
            //detect app folder
            String dir = getAppFolder();

            //deserialize user
            File f = new File(dir, USER_DATA);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream oin = new ObjectInputStream(fis);
            Lead result = (Lead) oin.readObject();
            this.phone = result.getPhones().get(0).getPhone();
            oin.close();

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    protected String getAppFolder() throws PackageManager.NameNotFoundException {
        //detect app folder
        PackageManager m = context.getPackageManager();
        String t = context.getPackageName();
        PackageInfo p = m.getPackageInfo(t, 0);
        return p.applicationInfo.dataDir;
    }
}
