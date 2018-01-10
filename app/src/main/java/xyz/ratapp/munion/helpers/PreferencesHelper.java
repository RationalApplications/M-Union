package xyz.ratapp.munion.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

import co.chatsdk.core.dao.Thread;

/**
 * Created by timtim on 24/12/2017.
 */

public class PreferencesHelper {

    private static final String PREFS_NAME = "MUNION_APP_PREFS";
    private static final PreferencesHelper ourInstance = new PreferencesHelper();
    public static final String CHAT_THREAD_ENTITY_ID = "chat_thread_entity_id";
    public static final String HYPOTHEC_CONTACT_ID = "hypothec_contact_id";
    private static final String HYPOTHEC_DATA_WAS_SET = "hypothec_data_was_set";
    public static final String IS_USER_AUTHED = "is_user_authed";

    private SharedPreferences prefs;

    public static PreferencesHelper getInstance(Context context) {

        if(context != null) {
            ourInstance.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return ourInstance;
        }
        else {
            return null;
        }
    }

    private PreferencesHelper() {

    }

    public void saveChatThread(Thread thread) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CHAT_THREAD_ENTITY_ID, thread.getEntityID());
        editor.apply();
    }

    public void saveHypothecId(@NotNull String id) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(HYPOTHEC_CONTACT_ID, id);
        editor.apply();
    }

    public void saveHypothecDataWasSet(@NotNull boolean wasSet) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(HYPOTHEC_DATA_WAS_SET, wasSet);
        editor.apply();
    }

    public void setAuthed(boolean authed) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(IS_USER_AUTHED, authed);
        editor.apply();
    }

    public boolean isAuthed() {
        return prefs.getBoolean(IS_USER_AUTHED, false);
    }

    public boolean wasHypothecDataSet() {
        return prefs.getBoolean(HYPOTHEC_DATA_WAS_SET, false);
    }

    public String getHypothecId() {
        return prefs.getString(HYPOTHEC_CONTACT_ID, "");
    }

    public String getChatThreadEntityId() {
        return prefs.getString(CHAT_THREAD_ENTITY_ID, "");
    }
}
