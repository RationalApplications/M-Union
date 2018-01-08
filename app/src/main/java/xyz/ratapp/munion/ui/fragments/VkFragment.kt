package xyz.ratapp.munion.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_vk.*
import kotlinx.android.synthetic.main.fragment_vk.view.*
import xyz.ratapp.munion.R
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import xyz.ratapp.munion.ui.fragments.common.BaseFragment
import java.io.IOException
import java.io.InputStream


/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */

class VkFragment : BaseFragment() {


    override fun getFragmentName(context: Context): String {
        return context.resources.getString(R.string.title_news_feed)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_vk, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        web_view_vk.apply {
            val settings = web_view_vk.settings
            settings.javaScriptEnabled = true
            settings.allowUniversalAccessFromFileURLs = true

            web_view_vk.webViewClient = (object : WebViewClient() {

                override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
                    return true
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return true
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    return true
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)

                    injectScriptFile(view, "js/vk.js")

                    // test if the script was loaded
                    view.loadUrl("javascript:setTimeout(test(), 500)")
                }

                private fun injectScriptFile(view: WebView, scriptFile: String) {
                    val input: InputStream
                    try {
                        if (activity == null){
                            return
                        }
                        input = activity.assets.open(scriptFile)
                        val buffer = ByteArray(input.available())
                        input.read(buffer)
                        input.close()

                        // String-ify the script byte-array using BASE64 encoding !!!
                        val encoded = Base64.encodeToString(buffer, Base64.NO_WRAP)
                        view.loadUrl("javascript:(function() {" +
                                "var parent = document.getElementsByTagName('head').item(0);" +
                                "var script = document.createElement('script');" +
                                "script.type = 'text/javascript';" +
                                // Tell the browser to BASE64-decode the string into your script !!!
                                "script.innerHTML = window.atob('" + encoded + "');" +
                                "parent.appendChild(script)" +
                                "})()")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            })

            web_view_vk.loadUrl("https://m.vk.com/m_union")

        }
    }


}