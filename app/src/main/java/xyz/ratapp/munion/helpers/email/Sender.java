package xyz.ratapp.munion.helpers.email;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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

    public void sendHypothecMessage(Context context, String name, String phone, List<Uri> photos) {
        try {
            String email = "temp@gmail.com";
            String sendTo = "admin@timtim.tech";

            SendTask sendTask;
            sendTask = new SendTask(context, "Заявка на ипотеку от " + name, name + "\n" + phone + "\n" + email + "\n" + email,
                    "AZINO777", sendTo, photos, "rational.app@gmail.com", "Dy4-SWx-vu8-Lna");
            sendTask.execute();

        } catch (Exception e) {
            if (e.getMessage() != null)
                Log.e("Exception", e.getMessage());
        }
    }

    public void sendMoneyMessage(Context context, String name,
                                 String cardNumber, String money) throws Exception {
        String sendTo = "admin@timtim.tech";

        SendTask sendTask;
        sendTask = new SendTask(context, "Заявка на получение денег от " + name,
                name + "\n" + money + " рублей, на карту " + cardNumber,
                "AZINO777", sendTo, null, "rational.app@gmail.com", "Dy4-SWx-vu8-Lna");

        sendTask.execute();
    }
}
