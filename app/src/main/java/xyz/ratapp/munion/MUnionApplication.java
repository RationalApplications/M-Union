package xyz.ratapp.munion;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.vk.sdk.VKSdk;

import java.util.HashMap;
import java.util.Map;

import co.chatsdk.core.dao.User;
import co.chatsdk.core.handlers.AuthenticationHandler;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.session.Configuration;
import co.chatsdk.core.session.NM;
import co.chatsdk.core.session.NetworkManager;
import co.chatsdk.core.types.AccountDetails;
import co.chatsdk.core.types.AuthKeys;
import co.chatsdk.firebase.FirebaseModule;
import co.chatsdk.firebase.file_storage.FirebaseFileStorageModule;
import co.chatsdk.firebase.push.FirebasePushModule;
import co.chatsdk.ui.manager.UserInterfaceModule;
import co.chatsdk.ui.utils.AppBackgroundMonitor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


/**
 * <p>Date: 05.11.17</p>
 *
 * @author Simon
 */

public class MUnionApplication extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(getApplicationContext());
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
        builder.threadDetailsEnabled(false);
        builder.debugModeEnabled(true);

        ChatSDK.initialize(builder.build());
        FirebaseModule.activate();
        UserInterfaceModule.activate(context);
        FirebaseFileStorageModule.activate();
        FirebasePushModule.activateForFirebase();
    }

}
