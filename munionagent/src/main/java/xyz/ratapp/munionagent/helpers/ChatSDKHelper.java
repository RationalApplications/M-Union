package xyz.ratapp.munionagent.helpers;


import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.lang3.StringUtils;

import co.chatsdk.core.dao.User;
import co.chatsdk.core.session.NM;
import co.chatsdk.core.session.StorageManager;
import co.chatsdk.firebase.FirebaseAuthenticationHandler;
import co.chatsdk.ui.utils.AppBackgroundMonitor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import xyz.ratapp.munionagent.data.pojo.BitrixUser;
import xyz.ratapp.munionagent.ui.activities.SplashActivity;
import xyz.ratapp.munionagent.ui.activities.auth.AuthActivity;

/**
 * Created by timtim on 24/12/2017.
 */

public class ChatSDKHelper {

    public static void firstAuth(AuthActivity activity,
                                 FirebaseUser firebaseUser,
                                 BitrixUser bitrixUser) {
        PreferencesHelper.getInstance(activity).setAuthed(true);
        authWithUser(activity, firebaseUser, bitrixUser);
    }

    private static void authWithUser(AuthActivity activity,
                                     FirebaseUser user,
                                     BitrixUser bitrixUser) {
        FirebaseAuthenticationHandler auth =
                ((FirebaseAuthenticationHandler) NM.auth());
        auth.authenticateWithUser(user).
                observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            AppBackgroundMonitor.shared().setEnabled(true);
            initChatUser(activity, bitrixUser);
        });
    }

    public static void authWithUser(SplashActivity activity, FirebaseUser user) {
        FirebaseAuthenticationHandler auth =
                ((FirebaseAuthenticationHandler) NM.auth());
        auth.authenticateWithUser(user).
                observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            AppBackgroundMonitor.shared().setEnabled(true);
            activity.next();
        });
    }

    public static void initChatUser(AuthActivity activity,
                                    BitrixUser bitrixUser) {
        User user = NM.currentUser();

        String name = bitrixUser.getNAME() + " " + bitrixUser.getLAST_NAME();
        String phoneNumber = bitrixUser.getWORK_PHONE();
        String email = bitrixUser.getEMAIL();
        String avatarUrl = bitrixUser.getPERSONAL_PHOTO().toString();
        int id = bitrixUser.getID() * 10 + 1; //format idx, where x = 1 if agent, 0 else

        if(!StringUtils.isEmpty(name)) {
            user.setName(name);
        }
        if(!StringUtils.isEmpty(phoneNumber)) {
            user.setPhoneNumber(phoneNumber);
        }
        if(!StringUtils.isEmpty(email)) {
            user.setEmail(email);
        }
        if(!StringUtils.isEmpty(avatarUrl)) {
            user.setAvatarURL(avatarUrl);
        }
        user.setStatus(id + "");

        NM.core().pushUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(activity::sendResult);
    }

}
