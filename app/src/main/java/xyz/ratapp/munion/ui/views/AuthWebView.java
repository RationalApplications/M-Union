package xyz.ratapp.munion.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
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


    public AuthWebView(Context context) {
        super(context);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(false);
        getSettings().setAppCacheEnabled(false);
        getSettings().setLoadsImagesAutomatically(false);
        getSettings().setBlockNetworkImage(true);
        //getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= 19) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }

    public void setPrivateMode(boolean privateMode) {
        if(privateMode) {
            CookieSyncManager.createInstance(getContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();

            WebSettings ws = getSettings();
            ws.setSaveFormData(false);
            ws.setSavePassword(false);
            clearCache(true);
            clearHistory();
        }
        else {
            WebSettings ws = getSettings();
            ws.setSaveFormData(true);
            ws.setSavePassword(true);
        }
    }

    public void executeJsAfterLoadingPage(String url, String jsCode,
                                          String jsResultCode,
                                          JSInterfaceCallback callback) {
        addJavascriptInterface(callback, "HtmlViewer");

        setWebViewClient(new WebViewClient() {

            boolean isFirstLoading = true;

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                callback.sendResult(null);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                            WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                callback.sendResult(null);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

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
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                callback.sendResult(null);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                            WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                callback.sendResult(null);
            }

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
