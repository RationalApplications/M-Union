package xyz.ratapp.munion.controllers.auth;

import xyz.ratapp.munion.controllers.interfaces.DataCallback;
import xyz.ratapp.munion.data.pojo.Lead;
import xyz.ratapp.munion.ui.activities.AuthActivity;

/**
 * Created by timtim on 27/12/2017.
 */

public abstract class AuthDataCallback implements DataCallback<Lead> {

    private AuthController controller;

    public AuthDataCallback(AuthController controller) {
        this.controller = controller;
    }

    @Override
    public void onSuccess(Lead user) {
        doAfterPhoneChecked(user);
    }

    protected abstract void doAfterPhoneChecked(Lead user);

    @Override
    public void onFailed(Throwable thr) {
        controller.onFailedInAuthTask(thr);
    }
}
