package xyz.ratapp.munion.ui.activities.auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import xyz.ratapp.munion.R;

/**
 * Created by timtim on 24/12/2017.
 */

public class VerificationDialog extends DialogFragment {

    private String phone = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_verif, null))
                .setPositiveButton(R.string.signin, (dialog, id) -> {
                    AuthActivity activity = (AuthActivity) getActivity();
                    EditText editText = getDialog().findViewById(R.id.et_phone_pass);
                    String code = editText.getText().toString();
                    activity.verifyPhoneNumberWithCode(code);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> getActivity().finish());

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView info = getDialog().findViewById(R.id.atv_auth_info);
        info.setText("Сообщение с кодом отправлено на " + phone);
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }
}
