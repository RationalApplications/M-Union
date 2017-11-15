package xyz.ratapp.munion.email;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timtim on 15/11/2017.
 */

public class Sender {
    private static final Sender ourInstance = new Sender();

    public static Sender getInstance() {
        return ourInstance;
    }

    private Sender() {

    }

    public void sendMessage(Context context, String name, String phone, List<Uri> photos) {
        try {
            String email = "temp@gmail.com";
            String sendTo = "semavar3@gmail.com";

            SendTask sendTask;
            sendTask = new SendTask(context, "Заявка на ипотеку от " + name, name + "\n" + phone + "\n" + email + "\n" + email,
                    "AZINO777", sendTo, photos, "rational.app@gmail.com", "Dy4-SWx-vu8-Lna");
            sendTask.execute();

        } catch (Exception e) {
            if (e.getMessage() != null)
                Log.e("Exception", e.getMessage());
        }
    }
}
