package xyz.ratapp.munionagent;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.firebase.FirebaseApp;

import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.session.Configuration;
import co.chatsdk.firebase.FirebaseModule;
import co.chatsdk.firebase.file_storage.FirebaseFileStorageModule;
import co.chatsdk.firebase.push.FirebasePushModule;
import co.chatsdk.ui.manager.UserInterfaceModule;

/**
 * Created by timtim on 28/12/2017.
 */

public class MApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initChatSDK();
    }

    private void initChatSDK() {
        Context context = getApplicationContext();
        Configuration.Builder builder = new Configuration.Builder(context);

        builder.firebase("prod", "AAAAhsFBP2w:APA91bGzXpe5ylwm2fhIkhPIj6E7jBeiEcEYsJTdfSNvCZcSlCjQYsvbUCbGbrRxSBkuOwEOFYG7HVi78yTpNSFnNIDr3BjNGfutmcP9nwCOSyHYKnBY12150CA27_b36f4j1Gs3nxNu");
        builder.googleMaps("AIzaSyD-tOuSye6oPh33pACsD1S3W-RMEQmJWPs");
        builder.imageCroppingEnabled(true);
        builder.locationMessagesEnabled(true);
        builder.saveImagesToDirectoryEnabled(true);
        builder.threadDetailsEnabled(true);
        builder.debugModeEnabled(true);

        ChatSDK.initialize(builder.build());
        FirebaseModule.activate();
        UserInterfaceModule.activate(context);
        FirebaseFileStorageModule.activate();
        FirebasePushModule.activateForFirebase();
    }

}
