package xyz.ratapp.munion.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Locale;

import xyz.ratapp.munion.R;
import xyz.ratapp.munion.ui.activities.MainActivity;

/**
 * Created by timtim on 29/12/2017.
 */

public class BitrixAuthWebView extends WebView {

    public static final String BITRIX_AUTH_URL = "https://www.bitrix24.net/oauth/authorize/";

    private String email;
    private String password;
    private BitrixAuthCallback callback;

    public BitrixAuthWebView(Context context) {
        super(context);

        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        WebSettings ws = getSettings();
        ws.setSaveFormData(false);
        ws.setSavePassword(false);
        clearCache(true);
        clearHistory();
    }

    public BitrixAuthWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BitrixAuthWebView(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setupClient() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        String redirectUri = getContext().getString(R.string.redirect_uri);

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(url.startsWith(redirectUri)) {
                    //redirected -> take code and done
                    int codePosition = url.indexOf("code=") + 5;
                    String code = url.substring(codePosition);
                    code = code.substring(0, code.indexOf('&'));

                    if(callback != null) {
                        callback.onComplete(code);
                        callback = null;
                    }
                }
                else if(url.startsWith(BITRIX_AUTH_URL)){
                    //run js script to fill fields and push the button
                    String JS_MASK = //"closeMobilePopup();\n" +
                            "document.getElementsByName(\'USER_LOGIN\')[0].value=\'%s\';\n" +
                            "document.getElementsByName(\'USER_PASSWORD\')[0].value=\'%s\';\n" +
                            "document.forms[\'form_auth\'].submit();";
                    String js = String.format(Locale.getDefault(), JS_MASK, email, password);
                    view.loadUrl("javascript:(function() {" +
                            js
                            + "})()");
                }
            }
        });
    }

    public void login(String url,
                      String email,
                      String password,
                      BitrixAuthCallback callback) {
        setupLoginData(email, password, callback);
        loadUrl(url);
    }

    private void setupLoginData(String email,
                               String password,
                               BitrixAuthCallback callback) {
        this.email = email;
        this.password = password;
        this.callback = callback;
        setupClient();
    }



    public interface BitrixAuthCallback {

        void onComplete(String code);

        void onFailed();
    }
}
