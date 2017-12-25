package xyz.ratapp.munion.ui.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import xyz.ratapp.munion.R;
import xyz.ratapp.munion.helpers.ChatSDKHelper;
import xyz.ratapp.munion.helpers.PreferencesHelper;
import xyz.ratapp.munion.ui.activities.SplashActivity;

/**
 * Created by timtim on 24/12/2017.
 */

public class AuthActivity extends SplashActivity {

    public final static String ACTION_DO_AUTH =
            "xyz.ratapp.munion.ACTION_DO_AUTH";
    public static final String RESPONSE_EXTRA_CHAT_ENTITY_ID = "RESPONSE_EXTRA_CHAT_ENTITY_ID";
    public final static String EXTRA_PHONE_NUMBER = "phone_number";
    public final static String EXTRA_PASSWORD = "password";
    public final static int REQUEST_AUTH_CODE = 73;


    private FirebaseAuth auth;
    private String phone;
    private String password;
    private String verificationId;
    private boolean verificationInProgress = false;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    public static Intent getAuthIntent(String phone, String password) {
        Intent intent = new Intent(ACTION_DO_AUTH);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_PHONE_NUMBER, phone);
        extras.putString(EXTRA_PASSWORD, password);
        intent.putExtras(extras);

        return intent;
    }

    private boolean splashModeEnabled = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = setActivityMode(savedInstanceState);
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!splashModeEnabled) {
            FirebaseUser currentUser = auth.getCurrentUser();

            if (currentUser != null) {
                boolean isAuthed = PreferencesHelper.getInstance(this).isAuthed();
                String chatThread = PreferencesHelper.getInstance(this).getChatThreadEntityId();

                if(isAuthed && !chatThread.isEmpty()) {
                    ChatSDKHelper.authWithUser(this, currentUser);
                }
            }
            else {
                doModeTasks();
            }
        }
    }

    private Bundle setActivityMode(Bundle savedInstanceState) {
        Intent intent = getIntent();

        if (savedInstanceState == null) {
            savedInstanceState = new Bundle();
        }
        if(intent == null) {
            setTheme(R.style.SplashTheme);
            savedInstanceState.putBoolean(SplashActivity.EXTRA_SHOW_SPLASH, true);
            return savedInstanceState;
        }

        String action = intent.getAction();
        if(action != null && action.equals(ACTION_DO_AUTH)) {
            savedInstanceState.putBoolean(SplashActivity.EXTRA_SHOW_SPLASH, false);
            splashModeEnabled = false;
        }
        else {
            savedInstanceState.putBoolean(SplashActivity.EXTRA_SHOW_SPLASH, true);
            setTheme(R.style.SplashTheme);
        }

        return savedInstanceState;
    }

    private void doModeTasks() {
        if(!splashModeEnabled) {
            setContentView(R.layout.activity_auth);
            initAuthFields();
            ProgressBar pb = findViewById(R.id.pb_verif);
            AuthTask task = new AuthTask(pb, this);
            task.execute();
        }
    }

    private void initAuthFields() {
        phone = getIntent().getStringExtra(EXTRA_PHONE_NUMBER);
        password = getIntent().getStringExtra(EXTRA_PASSWORD);

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                verificationInProgress = false;
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                verificationInProgress = false;
                String throwable = "!";

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    throwable = getString(R.string.incorrect_phone_number);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    throwable = getString(R.string.too_many_requests);
                }

                String error = getString(R.string.error_auth);
                Toast.makeText(AuthActivity.this,
                        error + throwable,
                        Toast.LENGTH_LONG).show();
                AuthActivity.this.finish();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                AuthActivity.this.verificationId = verificationId;
                resendToken = token;
            }
        };
    }

    void startPhoneNumberVerification() {
        PhoneAuthProvider.getInstance().
                verifyPhoneNumber(phone,
                        60, TimeUnit.SECONDS,
                        this, callbacks);

        verificationInProgress = true;
    }

    void verifyPhoneNumberWithCode(String code) {
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().
                verifyPhoneNumber(phone,
                        60, TimeUnit.SECONDS,
                        this, callbacks,
                        token);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        ChatSDKHelper.firstAuth(AuthActivity.this, user);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                R.string.incorrect_password,
                                Toast.LENGTH_LONG).show();
                        resendVerificationCode(resendToken);
                        AuthTask.showCodeDialog(AuthActivity.this);
                    }
                });
    }

    private void sendResult(int result, String chatEntityId) {
        Intent intent = new Intent();
        intent.putExtra(RESPONSE_EXTRA_CHAT_ENTITY_ID, chatEntityId);
        setResult(result, intent);
        finish();
    }

    public void setChatEntityId(String entityID) {
        sendResult(RESULT_OK, entityID);
    }
}
