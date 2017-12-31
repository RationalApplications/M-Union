package xyz.ratapp.munion.helpers;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import co.chatsdk.core.dao.Keys;
import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.dao.User;
import co.chatsdk.core.session.NM;
import co.chatsdk.core.session.StorageManager;
import co.chatsdk.firebase.FirebaseAuthenticationHandler;
import co.chatsdk.ui.search.SearchActivity;
import co.chatsdk.ui.utils.AppBackgroundMonitor;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import xyz.ratapp.munion.data.pojo.Lead;
import xyz.ratapp.munion.ui.activities.SplashActivity;
import xyz.ratapp.munion.ui.activities.auth.AuthActivity;

/**
 * Created by timtim on 24/12/2017.
 */

public class ChatSDKHelper {

    private static final String DEFAULT_CHAT_NAME = "DEFAULT_CHAT";

    public static void createThread(final AuthActivity activity,
                                    List<User> users) {
        NM.thread().createThread(users.get(0).getName(), users)
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
                                 FirebaseUser user,
                                 Lead bitrixUser,
                                 int agentId) {
        FirebaseAuthenticationHandler auth =
                ((FirebaseAuthenticationHandler) NM.auth());
        auth.authenticateWithUser(user).
                observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            AppBackgroundMonitor.shared().setEnabled(true);

            NM.search().usersForIndex(Keys.Status, agentId + "")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<User>() {

                        User agent = null;

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull User user) {
                            agent = user;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(activity, "Ваш агент не установил приложение :c", Toast.LENGTH_SHORT).show();
                            ChatSDKHelper.createThread(activity,
                                    Collections.singletonList(NM.currentUser()));
                        }

                        @Override
                        public void onComplete() {
                            if(agent != null) {
                                ChatSDKHelper.createThread(activity,
                                        Arrays.asList(NM.currentUser(), agent));
                            }
                            else {
                                Toast.makeText(activity, "Ваш агент не установил приложение :c", Toast.LENGTH_SHORT).show();
                                ChatSDKHelper.createThread(activity,
                                        Collections.singletonList(NM.currentUser()));
                            }
                        }
                    });

            initChatUser(bitrixUser);

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

    public static void initChatUser(Lead lead) {
        User user = NM.currentUser();

        String name = lead.getName() + " " + lead.getLastName();
        String title = lead.getTitle();
        String phoneNumber = lead.getPhones().get(0).getPhone();
        String avatarUrl = lead.getPhotoUri();
        int id = lead.getId() * 10; //format idx, where x = 1 if agent, 0 else

        if(!StringUtils.isEmpty(name)) {
            user.setName(name);
        }
        if(!StringUtils.isEmpty(phoneNumber)) {
            user.setPhoneNumber("+7" + phoneNumber);
        }
        if(!StringUtils.isEmpty(avatarUrl)) {
            user.setAvatarURL(avatarUrl);
        }
        user.setEmail(title); //we will use email as title of object
        user.setStatus(id + ""); //we will use status as simple id

        NM.core().pushUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
