package xyz.ratapp.munionagent.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.chatsdk.core.dao.Thread;

/**
 * Created by timtim on 24/12/2017.
 */

public class PreferencesHelper {

    private static final String PREFS_NAME = "MUNION_APP_PREFS";
    private static final PreferencesHelper ourInstance = new PreferencesHelper();
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

    public void setAuthed(boolean authed) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(IS_USER_AUTHED, authed);
        editor.apply();
    }

    public boolean isAuthed() {
        return prefs.getBoolean(IS_USER_AUTHED, false);
    }

}
