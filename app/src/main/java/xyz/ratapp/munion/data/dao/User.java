package xyz.ratapp.munion.data.dao;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.firebase.auth.UserInfo;

import co.chatsdk.ui.chat.MessageListItem;

/**
 * Created by timtim on 20/12/2017.
 */

public class User implements UserInfo {

    private String uid;
    private String providerId;
    @Nullable
    private String displayName;
    @Nullable
    private Uri photoUrl;
    @Nullable
    private String email;
    @Nullable
    private String phoneNumber;
    private boolean emailVerified;

    public User(String uid, String providerId,
                @Nullable String displayName,
                @Nullable Uri photoUrl,
                @Nullable String email,
                @Nullable String phoneNumber,
                boolean emailVerified) {
        this.uid = uid;
        this.providerId = providerId;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.emailVerified = emailVerified;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    @Override
    @Nullable
    public Uri getPhotoUrl() {
        return photoUrl;
    }

    @Override
    @Nullable
    public String getEmail() {
        return email;
    }

    @Override
    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getEntityID() {
        return uid;
    }
}
