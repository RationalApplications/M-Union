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
import co.chatsdk.ui.manager.UserInterfaceModule;
import co.chatsdk.ui.utils.AppBackgroundMonitor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
/*import co.chatsdk.firebase.FirebaseModule;
import co.chatsdk.firebase.file_storage.FirebaseFileStorageModule;
import co.chatsdk.firebase.push.FirebasePushModule;
import co.chatsdk.ui.manager.UserInterfaceModule;*/


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

        builder.firebase("prod", "rootPath");
        builder.threadDetailsEnabled(false);
        builder.anonymousLoginEnabled(true);
        builder.debugModeEnabled(true);

        ChatSDK.initialize(builder.build());
        FirebaseModule.activate();
        UserInterfaceModule.activate(context);

        //auth();

        /*FirebaseFileStorageModule.activate();
        FirebasePushModule.activateForFirebase();*/
    }

    private void setUserData() {
        User user = NM.currentUser();
        user.setName("Tim Kuzmin");
        user.setAvatarURL("https://pp.userapi.com/c841639/v841639899/3a2a2/5Olfu5GddnM.jpg");
        user.setCountryCode("RU");
        user.setDateOfBirth("15.07.1998");
        user.setEmail("admin@timtim.tech");
        user.setPhoneNumber("+79111696442");
    }

    private void auth() {
        AccountDetails details = new AccountDetails();
        details.type = AccountDetails.Type.Anonymous;
        NM.auth().authenticate(details)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        setUserData();
                    }
                })
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        AppBackgroundMonitor.shared().setEnabled(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        e.printStackTrace();
                    }
                });
    }
}
