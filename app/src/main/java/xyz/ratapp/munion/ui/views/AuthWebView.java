package xyz.ratapp.munion.ui.views;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Locale;

import xyz.ratapp.munion.R;

/**
 * Created by timtim on 29/12/2017.
 */

public class AuthWebView extends WebView {


    public AuthWebView(Context context) {
        super(context);
        getSettings().setJavaScriptEnabled(true);
    }

    public void executeJsAfterLoadingPage(String url, String jsCode,
                                          JSInterfaceCallback callback) {
        addJavascriptInterface(callback, "HtmlViewer");

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                loadUrl("javascript:window.HtmlViewer.sendResult" +
                        "(" + jsCode + ");");
            }
        });

        loadUrl(url);
    }

    public void loginAndExecuteJs(String loginUrl, String loginJsCode,
                                  String urlToLoad, String jsCode,
                                  JSInterfaceCallback callback) {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, urlToLoad);

                if(url.equals(loginUrl)) {
                    view.loadUrl("javascript:(function() {" +
                            loginJsCode
                            + "})()");
                }
                else {
                    executeJsAfterLoadingPage(url, jsCode, callback);
                }
            }
        });
    }


    public abstract static class JSInterfaceCallback {

        @JavascriptInterface
        public abstract void sendResult(String result);
    }

}
