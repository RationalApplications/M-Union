package xyz.ratapp.munion.ui.activities.auth;

import xyz.ratapp.munion.controllers.interfaces.UserCallback;
import xyz.ratapp.munion.data.pojo.Lead;

/**
 * Created by timtim on 27/12/2017.
 */

public abstract class AuthUserCallback implements UserCallback {

    private AuthActivity authActivity;

    public AuthUserCallback(AuthActivity authActivity) {
        this.authActivity = authActivity;
    }

    @Override
    public void onSuccess(Lead user) {
        doAfterPhoneChecked(user);
    }

    protected abstract void doAfterPhoneChecked(Lead user);

    @Override
    public void onFailed(Throwable thr) {
        authActivity.onFailedInAuthTask(thr);
    }
}
