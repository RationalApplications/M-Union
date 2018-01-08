package xyz.ratapp.munion.controllers.interfaces;

/**
 * Created by timtim on 08/01/2018.
 */

public interface CodeInputCallback {

    void onCodeTaken(String code);

    void onFailedTakeCode();


}
