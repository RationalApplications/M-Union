package xyz.ratapp.munion.controllers.interfaces;

import xyz.ratapp.munion.data.pojo.Lead;

/**
 * Created by timtim on 27/12/2017.
 */

public interface UserCallback {

    void onSuccess(Lead user);

    void onFailed(Throwable thr);
}
