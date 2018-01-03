package xyz.ratapp.munion.controllers.interfaces;

/**
 * Created by timtim on 27/12/2017.
 */

public interface DataCallback<T> {

    void onSuccess(T data);

    void onFailed(Throwable thr);
}
