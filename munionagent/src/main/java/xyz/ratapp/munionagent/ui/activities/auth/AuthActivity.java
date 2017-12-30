package xyz.ratapp.munionagent.ui.activities.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import xyz.ratapp.munionagent.R;
import xyz.ratapp.munionagent.data.pojo.BitrixUser;
import xyz.ratapp.munionagent.helpers.ChatSDKHelper;
import xyz.ratapp.munionagent.helpers.PreferencesHelper;
import xyz.ratapp.munionagent.ui.activities.SplashActivity;
import xyz.ratapp.munionagent.ui.views.BitrixAuthWebView;

/**
 * Created by timtim on 28/12/2017.
 */

public class AuthActivity extends SplashActivity {

    public final static String ACTION_DO_AUTH =
            "xyz.ratapp.munionagent.ACTION_DO_AUTH";
    public final static String EXTRA_EMAIL = "email";
    public final static String EXTRA_PASSWORD = "password";
    public final static int REQUEST_AUTH_CODE = 73;

    private static final String AUTH_URL_MASK =
            "https://m-union.bitrix24.ru/oauth/authorize/?client_id=%s&response_type=%d&redirect_uri=%s";
    private static final int responseType = 13;

    private FirebaseAuth auth;
    private String phone;
    private String email;
    private String password;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private ProgressBar pb;
    private BitrixUser bitrixUser;
    //oauth data
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scopes;

    public static Intent getAuthIntent(String email, String password) {
        Intent intent = new Intent(ACTION_DO_AUTH);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_EMAIL, email);
        extras.putString(EXTRA_PASSWORD, password);
        intent.putExtras(extras);

        return intent;
    }

    private boolean splashModeEnabled = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = setActivityMode(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        loadStrings();
        super.onCreate(savedInstanceState);
    }

    private void loadStrings() {
        clientId = getString(R.string.client_id);
        clientSecret = getString(R.string.client_secret);
        redirectUri = getString(R.string.redirect_uri);
        scopes = getString(R.string.scopes);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            boolean isAuthed = PreferencesHelper.getInstance(this).isAuthed();
            if(isAuthed) {
                ChatSDKHelper.authWithUser(currentUser);
            }
        }
        else {
            doModeTasks();
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
            initAuthData();
            pb = findViewById(R.id.pb_verif);
            pb.setVisibility(View.VISIBLE);

            BitrixAuthWebView wv = findViewById(R.id.wv_auth);
            String url = String.format(Locale.getDefault(), AUTH_URL_MASK,
                    clientId, responseType, redirectUri);
            wv.login(url, email, password, new BitrixAuthWebView.
                    BitrixAuthCallback() {

                @Override
                public void onComplete(String code) {
                    doOAuthLogin(code);
                }

                @Override
                public void onFailed() {
                    Toast.makeText(AuthActivity.this,
                            "Введите верные данные!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void doOAuthLogin(String code) {
        try {
            BitrixOAuthTask task = new BitrixOAuthTask();
            bitrixUser = task.execute(clientId,
                    clientSecret,
                    redirectUri,
                    code,
                    scopes).get();

            if(!bitrixUser.getWORK_PHONE().isEmpty()) {
                phone = bitrixUser.getWORK_PHONE();
                pb.setVisibility(View.GONE);
                startPhoneNumberVerification();
                showCodeDialog(AuthActivity.this, phone);
            }
            else {
                Toast.makeText(AuthActivity.this,
                        "Введите свой номер телефона в данных битрикса!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(AuthActivity.this,
                    "Ой, произошла ошибка :c",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    static void showCodeDialog(Activity activity, String phone) {
        VerificationDialog dialog = new VerificationDialog();
        dialog.setPhone(phone);
        dialog.show(activity.getFragmentManager(), "dialog");
    }

    private void initAuthData() {
        email = getIntent().getStringExtra(EXTRA_EMAIL);
        password = getIntent().getStringExtra(EXTRA_PASSWORD);

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
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

    private void startPhoneNumberVerification() {
        PhoneAuthProvider.getInstance().
                verifyPhoneNumber(phone,
                        60, TimeUnit.SECONDS,
                        this, callbacks);
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
                        ChatSDKHelper.firstAuth(AuthActivity.this, user, bitrixUser);
                        pb.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                R.string.incorrect_password,
                                Toast.LENGTH_LONG).show();
                        resendVerificationCode(resendToken);
                        showCodeDialog(AuthActivity.this, phone);
                    }
                });
    }

    public void sendResult() {
        setResult(RESULT_OK);
        finish();
    }

}
