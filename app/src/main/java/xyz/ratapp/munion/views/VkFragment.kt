package xyz.ratapp.munion.views

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_vk.*
import kotlinx.android.synthetic.main.fragment_vk.view.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import xyz.ratapp.munion.R
import xyz.ratapp.munion.views.common.RxBaseFragment
import xyz.ratapp.munion.adapters.news.NewsAdapter
import xyz.ratapp.munion.common.NewsManager
import xyz.ratapp.munion.extensions.inflate
import xyz.ratapp.munion.models.PostNews
import android.util.Base64.NO_WRAP
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import java.io.IOException
import java.io.InputStream


/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */

class VkFragment : RxBaseFragment() {
//    companion object {
//        private val KEY_VK_NEWS = "vkNews"
//    }
//
//    private var postNews: PostNews? = null
//
//    private val newsManager by lazy { NewsManager() }


    override fun getFragmentName(context: Context): String {
        return context.resources.getString(R.string.title_news_feed)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_vk, false)
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

//        news_list.apply {
//            news_list.setHasFixedSize(true)
//            val linearLayout = LinearLayoutManager(context)
//            news_list.layoutManager = linearLayout
//            news_list.clearOnScrollListeners()
//            news_list.addOnScrollListener(InfiniteScrollListener({ requestNews() }, linearLayout))
//        }
//
//        initAdapter()
//
//        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_VK_NEWS)) {
//            postNews = savedInstanceState.get(KEY_VK_NEWS) as PostNews
//            (news_list.adapter as NewsAdapter).clearAndAddNews(postNews!!.news)
//        } else {
//            requestNews()
//        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
//        val news = (news_list.adapter as NewsAdapter).getNews()
//        if (postNews != null && news.size > 0) {
//            outState?.putParcelable(KEY_VK_NEWS, postNews?.copy(news = news))
//        }
    }

//    private fun requestNews() {
//        val subscription = newsManager.getNews(postNews?.after ?: "")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        { retrievedNews ->
//                            postNews = retrievedNews
//                            (news_list.adapter as NewsAdapter).addNews(retrievedNews.news)
//                        },
//                        { e ->
//                            Snackbar.make(news_list, e.message ?: "", Snackbar.LENGTH_SHORT).show()
//                        })
//
//        subscriptions.add(subscription)
//    }

//    private fun initAdapter() {
//        if (news_list.adapter == null) {
//            news_list.adapter = NewsAdapter()
//        }
//    }

}