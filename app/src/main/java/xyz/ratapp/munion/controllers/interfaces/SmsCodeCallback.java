package xyz.ratapp.munion.controllers.interfaces;

/**
 * Created by timtim on 08/01/2018.
 */

public interface SmsCodeCallback {

    void onSmsCodeTaken(String code);

    void resendSmsCode();

}
