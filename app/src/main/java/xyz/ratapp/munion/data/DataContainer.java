package xyz.ratapp.munion.data;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import xyz.ratapp.munion.controllers.interfaces.DataCallback;
import xyz.ratapp.munion.data.pojo.HypothecData;
import xyz.ratapp.munion.data.pojo.Lead;
import xyz.ratapp.munion.data.pojo.Statistics;
import xyz.ratapp.munion.ui.views.LoadingDialog;

/**
 * Created by timtim on 02/01/2018.
 */

abstract class DataContainer {

    public static final String USER_DATA = "USER_DATA.dat";
    public static final String HYPOTHEC_DATA = "HYPOTHEC_DATA.dat";

    protected Context context;
    protected String phone;
    protected String hypothecId;
    protected Lead user = null;
    protected HypothecData hypothec = null;
    protected Statistics statistics = null;


    protected HypothecData getHypothec() {
        if(hypothec == null &&
                getUser() != null) {
            hypothec = user.getHypothec();
        }
        if(hypothec == null) {
            hypothec = loadHypothecFromDisk();
        }

        return hypothec;
    }

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

    public void setHypothecId(String hypothecId) {
        this.hypothecId = hypothecId;
    }

    protected void setUser(Lead user) {
        String photoUri = this.user.getPhotoUri();
        Statistics statistics = this.user.getStatistics();
        String firebaseEntry = this.user.getFirebaseEntity();
        HypothecData hypothec = this.user.getHypothec();

        if(photoUri != null) {
            user.setPhotoUri(photoUri);
        }
        if(statistics != null) {
            user.setStatistics(statistics);
        }
        if(firebaseEntry != null) {
            user.setFirebaseEntity(firebaseEntry);
        }
        if(hypothec != null) {
            user.setHypothec(hypothec);
        }

        this.user = user;
    }

    protected abstract void getUser(DataCallback<Lead> callback);

    protected abstract void refreshUser(DataCallback<Lead> callback);

    protected abstract void getStatistics(DataCallback<Statistics> callback);

    protected abstract void setHypothec(String name, String phone);

    public abstract void loadStatistics(LoadingDialog dialog, DataCallback<Statistics> callback);

    abstract void loadStatistics(String phone,
                                 LoadingDialog dialog,
                                 DataCallback<Statistics> callback);

    protected abstract void loadUser(String phone,
                                     DataCallback<Lead> callback);

    protected abstract void loadHypothec(DataCallback<HypothecData> callback);

    protected abstract void loadContactComments(DataCallback<String> callback);


    protected void saveHypothecToDisk() {
        if(hypothec != null) {
            try {
                String dir = getAppFolder();

                //serialize user
                FileOutputStream fos =
                        new FileOutputStream(new File(dir, HYPOTHEC_DATA));
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(hypothec);
                oos.flush();
                oos.close();
            } catch (Exception e) {

            }
        }
    }

    protected HypothecData loadHypothecFromDisk() {
        try {
            //detect app folder
            String dir = getAppFolder();

            //deserialize user
            File f = new File(dir, HYPOTHEC_DATA);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream oin = new ObjectInputStream(fis);
            HypothecData result = (HypothecData) oin.readObject();
            oin.close();

            return result;
        } catch (Exception e) {
            return null;
        }
    }

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
