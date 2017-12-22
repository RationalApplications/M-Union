package xyz.ratapp.munion.ui.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import co.chatsdk.core.base.AbstractAuthenticationHandler;
import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.dao.User;
import co.chatsdk.core.handlers.AuthenticationHandler;
import co.chatsdk.core.session.NM;
import co.chatsdk.core.types.AccountDetails;
import co.chatsdk.firebase.FirebaseAuthenticationHandler;
import co.chatsdk.firebase.wrappers.UserWrapper;
import co.chatsdk.ui.utils.AppBackgroundMonitor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import xyz.ratapp.munion.R;

import static co.chatsdk.firebase.wrappers.UserWrapper.initWithAuthData;


public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;


    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String phone;
    private String password;

    //TODO: Наверное не должно быть static
    static String mVerificationId;

    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        pb = findViewById(R.id.pb_verif);
        phone = getIntent().getStringExtra("tel");
        password = getIntent().getStringExtra("pass");

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Toast.makeText(getApplicationContext(), "Invalid phone number.", Toast.LENGTH_LONG).show();
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


            }
        };

        MyTask task = new MyTask();
        task.execute(phone);
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber(phone)) {
            startPhoneNumberVerification(phone);
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]



    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                            FirebaseUser user = task.getResult().getUser();
                            FirebaseAuthenticationHandler auth =
                                    ((FirebaseAuthenticationHandler) NM.auth());
                            auth.authenticateWithUser(user).
                                    observeOn(AndroidSchedulers.mainThread()).subscribe(new Action() {
                                @Override
                                public void run() throws Exception {
                                    AppBackgroundMonitor.shared().setEnabled(true);
                                    createThread("CHAT", Arrays.asList(NM.currentUser()));
                                }
                            });
                            sendResult(-1);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Неверный пароль, Выслан новый.", Toast.LENGTH_LONG).show();
                                resendVerificationCode(phone, mResendToken);
                                VerificationDialog dialog = new VerificationDialog();
                                dialog.show(getFragmentManager(), "dialog");
                            }
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void createThread(String name, List<User> users) {
        NM.thread().createThread(name, users)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(new Consumer<Thread>() {
                    @Override
                    public void accept(Thread thread) throws Exception {
                        saveThread(thread);
                    }
                }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                String str = getString(co.chatsdk.ui.R.string.create_thread_with_users_fail_toast);
                Toast.makeText(AuthActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private void saveThread(Thread thread) {
        SharedPreferences prefs = getSharedPreferences("THREAD_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("thread_entity_id", thread.getEntityID());
        editor.apply();
    }

    private void signOut() {
        mAuth.signOut();
    }


    void sendResult(int result){
        Intent intent = new Intent();
        setResult(result, intent);
        finish();
    }



    private boolean validatePhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Invalid phone number.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }




    public static class VerificationDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            final LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_verif, null))
                    // Add action buttons
                    .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            ((AuthActivity)getActivity()).verifyPhoneNumberWithCode(mVerificationId, ((EditText) getDialog().findViewById(R.id.pass_verif)).getText().toString());
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().finish();
                        }
                    });
            return builder.create();
        }


    }

    class MyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        String phone;
        @Override
        protected Void doInBackground(String... params) {
            try {
                setupChatSDK();
                TimeUnit.SECONDS.sleep(3);
                //TODO: Здесь проверяем наличие в базе клиента
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phone = params[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pb.setVisibility(View.GONE);
            startPhoneNumberVerification(phone);
            VerificationDialog dialog = new VerificationDialog();
            dialog.show(getFragmentManager(), "dialog");
        }

        private void setupChatSDK() {
            /*Map<String, Object> data = AbstractNetworkAdapter.getMap(
                    new String[]{BDefines.Prefs.LoginTypeKey, BDefines.Prefs.LoginEmailKey, BDefines.Prefs.LoginPasswordKey},
                    BDefines.BAccountType.Password, etEmail.getText().toString(), etPass.getText().toString());

            BNetworkManager.sharedManager().getNetworkAdapter()
                    .authenticateWithMap(data).done(new DoneCallback<Object>() {
                @Override
                public void onDone(Object o) {
                    afterLogin();
                }
            }).fail(new FailCallback<BError>() {
                @Override
                public void onFail(BError bError) {
                    toastErrorMessage(bError, true);
                    dismissProgDialog();
                }
            });*/
        }
    }
    /*start startPhoneNumberVerification(mPhoneNumberField.getText().toString());
      verifWithCode verifyPhoneNumberWithCode(mVerificationId, code);
      resend resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
      signOut signOut(); */


}
