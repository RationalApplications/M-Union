package xyz.ratapp.munion.controllers.auth;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import agency.tango.materialintroscreen.SlideFragment;
import xyz.ratapp.munion.R;
import xyz.ratapp.munion.controllers.interfaces.CodeInputCallback;
import xyz.ratapp.munion.controllers.interfaces.SmsCodeCallback;
import xyz.ratapp.munion.data.DataController;
import xyz.ratapp.munion.data.pojo.Lead;
import xyz.ratapp.munion.helpers.ChatSDKHelper;
import xyz.ratapp.munion.helpers.PreferencesHelper;
import xyz.ratapp.munion.ui.activities.AuthActivity;
import xyz.ratapp.munion.ui.fragments.tour.CardTourFragment;
import xyz.ratapp.munion.ui.fragments.tour.ChatTourFragment;
import xyz.ratapp.munion.ui.fragments.tour.FriendTourFragment;
import xyz.ratapp.munion.ui.fragments.tour.MunionTourFragment;
import xyz.ratapp.munion.ui.fragments.tour.StatTourFragment;
import xyz.ratapp.munion.ui.fragments.tour.SubmitTourFragment;

import static android.app.Activity.RESULT_OK;
import static xyz.ratapp.munion.ui.activities.AuthActivity.EXTRA_PASSWORD;
import static xyz.ratapp.munion.ui.activities.AuthActivity.EXTRA_PHONE_NUMBER;
import static xyz.ratapp.munion.ui.activities.AuthActivity.RESPONSE_EXTRA_CHAT_ENTITY_ID;

/**
 * Created by timtim on 08/01/2018.
 */

public class AuthController implements
        CodeInputCallback,
        SmsCodeCallback {

    private AuthActivity activity;
    private List<SlideFragment> fragments;
    private FirebaseAuth auth;
    private String phone;
    private String password;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private int agentID;
    private Lead user;

    public AuthController(AuthActivity activity) {
        this.activity = activity;
    }

    public void onCreate() {
        auth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        initAuthFields();
        setupFragments();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            PreferencesHelper helper = PreferencesHelper.getInstance(activity);
            boolean isAuthed = helper.isAuthed();
            String chatThread = helper.getChatThreadEntityId();

            if(isAuthed && !chatThread.isEmpty()) {
                ChatSDKHelper.authWithUser(this, currentUser);
            }
        }
        else {
            DataController.getInstance(activity).loadUser(
                    phone.substring(2), //normalize phone number
                    new AuthDataCallback(this)
                    {
                        @Override
                        protected void doAfterPhoneChecked(Lead user) {
                            AuthController.this.user = user;
                            if(user.getPassword().equals(password)) {
                                agentID = user.getAssignedById() * 10 + 1;
                                startPhoneNumberVerification();
                            }
                            else {
                                onFailed(new Throwable("Password incorrect"));
                            }
                        }
                    });
        }
    }

    private void setupFragments() {
        if(fragments == null) {
            fragments = new ArrayList<>(6);
            fragments.add(new MunionTourFragment());
            fragments.add(new StatTourFragment());
            fragments.add(new ChatTourFragment());
            fragments.add(new CardTourFragment());

            FriendTourFragment friend = new FriendTourFragment();
            friend.setCodeCallback(this);
            fragments.add(friend);

            SubmitTourFragment submit = new SubmitTourFragment();
            submit.setPhoneNumber(phone);
            submit.setSmsCallback(this);
            fragments.add(submit);

            activity.setupFragments(fragments);
        }
    }

    private void initAuthFields() {
        phone = activity.getIntent().getStringExtra(EXTRA_PHONE_NUMBER);
        password = activity.getIntent().getStringExtra(EXTRA_PASSWORD);

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
                activity.showLongToast(error + throwable);
                activity.finish();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                AuthController.this.verificationId = verificationId;
                resendToken = token;
            }
        };
    }

    void onFailedInAuthTask(Throwable thr) {
        activity.showLongToast(thr.getMessage());
        activity.finish();
    }

    private void startPhoneNumberVerification() {
        PhoneAuthProvider.getInstance().
                verifyPhoneNumber(phone,
                        60, TimeUnit.SECONDS,
                        activity, callbacks);

    }

    private void resendVerificationCode(PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().
                verifyPhoneNumber(phone,
                        60, TimeUnit.SECONDS,
                        activity, callbacks,
                        token);
    }

    private void verifyPhoneNumberWithCode(String code) {
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        DataController instance = DataController.getInstance(activity);
                        instance.setFbUserEntity(user.getUid());
                        instance.setLoyaltyCode(this.user.getId() + "", user.getUid());
                        if(user.getPhotoUrl() != null) {
                            try {
                                instance.setUserPhotoUri(user.getPhotoUrl());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        ChatSDKHelper.firstAuth(this,
                                user, this.user, agentID);
                    }
                    else {
                        activity.showLongToast(R.string.incorrect_password);
                        resendVerificationCode(resendToken);
                    }
                });
    }

    public void setChatEntityId(String entityID) {
        sendResult(RESULT_OK, entityID);
    }

    private void sendResult(int result, String chatEntityId) {
        Intent intent = new Intent();
        intent.putExtra(RESPONSE_EXTRA_CHAT_ENTITY_ID, chatEntityId);
        activity.setResult(result, intent);
        activity.finish();
    }

    public AuthActivity getActivity() {
        return activity;
    }

    private String getString(@StringRes  int id) {
        return activity.getString(id);
    }


    //fragments interfaces
    //sms code interface
    @Override
    public void onSmsCodeTaken(String code) {
        verifyPhoneNumberWithCode(code);
    }

    @Override
    public void resendSmsCode() {
        startPhoneNumberVerification();
    }

    //friend code interface
    @Override
    public void onCodeTaken(String code) {
        //TODO: проверять верно ли введен код
        DataController instance = DataController.getInstance(activity);
        instance.addToInvitedFriendByLoyaltyCode(code);
    }

    @Override
    public void onFailedTakeCode() {
        //do nothing ¯\_(ツ)_/¯
    }

}
