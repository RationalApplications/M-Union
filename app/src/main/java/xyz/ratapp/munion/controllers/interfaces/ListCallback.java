package xyz.ratapp.munion.controllers.interfaces;

import java.util.List;

/**
 * Created by timtim on 02/01/2018.
 */

public interface ListCallback<T> {

    void onSuccess(List<T> data);

    void onFailed(Throwable thr);
}
