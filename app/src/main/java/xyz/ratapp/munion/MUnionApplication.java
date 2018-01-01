package xyz.ratapp.munion;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.vk.sdk.VKSdk;

import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.session.Configuration;
import co.chatsdk.firebase.FirebaseModule;
import co.chatsdk.firebase.file_storage.FirebaseFileStorageModule;
import co.chatsdk.firebase.push.FirebasePushModule;
import co.chatsdk.ui.manager.UserInterfaceModule;


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

        builder.firebase("prod", getString(R.string.cloud_messaging_server_key));
        builder.googleMaps(getString(R.string.google_maps_api_key));
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
