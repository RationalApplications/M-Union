package xyz.ratapp.munion;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * <p>Date: 05.11.17</p>
 *
 * @author Simon
 */

public class MUnionApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(getApplicationContext());
    }
}
