package xyz.ratapp.munion.helpers;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.List;

import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.dao.User;
import co.chatsdk.core.session.NM;
import co.chatsdk.firebase.FirebaseAuthenticationHandler;
import co.chatsdk.ui.utils.AppBackgroundMonitor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import xyz.ratapp.munion.ui.activities.SplashActivity;
import xyz.ratapp.munion.ui.activities.auth.AuthActivity;

/**
 * Created by timtim on 24/12/2017.
 */

public class ChatSDKHelper {

    private static final String DEFAULT_CHAT_NAME = "DEFAULT_CHAT";

    public static void createThread(final AuthActivity activity,
                                    List<User> users) {
        NM.thread().createThread(DEFAULT_CHAT_NAME, users)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(thread -> {
                            PreferencesHelper.
                                    getInstance(activity).
                                    saveChatThread(thread);
                            activity.setChatEntityId(thread.getEntityID());
                        }
                ).doOnError(throwable -> {
                    String str = activity.getString(co.chatsdk.ui.R.string.create_thread_with_users_fail_toast);
                    Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
                }).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public static void firstAuth(final AuthActivity activity,
                                    FirebaseUser user) {
        FirebaseAuthenticationHandler auth =
                ((FirebaseAuthenticationHandler) NM.auth());
        auth.authenticateWithUser(user).
                observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            AppBackgroundMonitor.shared().setEnabled(true);
            ChatSDKHelper.createThread(activity,
                    Collections.singletonList(NM.currentUser()));
            PreferencesHelper.getInstance(activity).setAuthed(true);
        });
    }

    public static void authWithUser(AuthActivity activity,
                                    FirebaseUser user) {
        FirebaseAuthenticationHandler auth =
                ((FirebaseAuthenticationHandler) NM.auth());
        auth.authenticateWithUser(user).
                observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                    AppBackgroundMonitor.shared().setEnabled(true);
            String threadEntityId = PreferencesHelper.getInstance(activity).
                    getChatThreadEntityId();
            activity.setChatEntityId(threadEntityId);
        });
    }
}
