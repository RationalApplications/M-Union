package xyz.ratapp.munion.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Locale;

import xyz.ratapp.munion.R;
import xyz.ratapp.munion.ui.activities.MainActivity;

/**
 * Created by timtim on 29/12/2017.
 */

public class AuthWebView extends WebView {


    public AuthWebView(Context context, boolean privateMode,
                       boolean pcMode) {
        super(context);

        if(privateMode) {
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();

            WebSettings ws = getSettings();
            ws.setSaveFormData(false);
            ws.setSavePassword(false);
            clearCache(true);
            clearHistory();
        }
        if(pcMode) {
            String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
            getSettings().setUserAgentString(newUA);
        }

        getSettings().setJavaScriptEnabled(true);
        //((MainActivity) context).showDialog(this);
    }

    public void executeJsAfterLoadingPage(String url, String jsCode,
                                          String jsResultCode,
                                          JSInterfaceCallback callback) {
        addJavascriptInterface(callback, "HtmlViewer");

        setWebViewClient(new WebViewClient() {

            boolean isFirstLoading = true;

            @Override
            public void onPageFinished(WebView view, String url) {
                if (isFirstLoading && !jsCode.isEmpty()) {
                    isFirstLoading = false;
                    view.loadUrl("javascript:(function() {" +
                            jsCode
                            + "})()");
                }
                else {
                    loadUrl("javascript:HtmlViewer.sendResult" +
                            "(" + jsResultCode + ");");
                }
            }
        });

        loadUrl(url);
    }

    public void loginAndExecuteJs(String loginUrl, String loginJsCode,
                                  String urlToLoad, String jsCode,
                                  String jsResultCode,
                                  JSInterfaceCallback callback) {
        setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if(url.equals(loginUrl)) {
                    view.loadUrl("javascript:(function() {" +
                            loginJsCode
                            + "})()");
                }
                else {
                    executeJsAfterLoadingPage(urlToLoad, jsCode, jsResultCode, callback);
                }
            }
        });

        loadUrl(loginUrl);
    }


    public abstract static class JSInterfaceCallback {

        @JavascriptInterface
        public abstract void sendResult(String result);
    }

}
