package xyz.ratapp.munion.ui.activities.auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import xyz.ratapp.munion.R;
import xyz.ratapp.munion.ui.activities.SplashActivity;

/**
 * Created by timtim on 24/12/2017.
 */

public class VerificationDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_verif, null))
                .setPositiveButton(R.string.signin, (dialog, id) -> {
                    AuthActivity activity = (AuthActivity) getActivity();
                    EditText editText = getDialog().findViewById(R.id.pass_verif);
                    String code = editText.getText().toString();
                    activity.verifyPhoneNumberWithCode(code);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> getActivity().finish());

        return builder.create();
    }
}
