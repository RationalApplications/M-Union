package xyz.ratapp.munion

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_vk.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import xyz.ratapp.munion.adapters.NewsAdapter
import xyz.ratapp.munion.common.NewsManager
import xyz.ratapp.munion.extensions.inflate
import xyz.ratapp.munion.models.PostNews

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */

class VkFragment : RxBaseFragment() {

    companion object {
        private val KEY_VK_NEWS = "vkNews"
    }

    private var postNews: PostNews? = null
    private val newsManager by lazy { NewsManager() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_vk, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        news_list.apply {
            news_list.setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            news_list.layoutManager = linearLayout
            news_list.clearOnScrollListeners()
//            news_list.addOnScrollListener(InfiniteScrollListener({ requestNews() }, linearLayout))
        }

        initAdapter()

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_VK_NEWS)) {
            postNews = savedInstanceState.get(KEY_VK_NEWS) as PostNews
            (news_list.adapter as NewsAdapter).clearAndAddNews(postNews!!.news)
        } else {
            requestNews()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val news = (news_list.adapter as NewsAdapter).getNews()
        if (postNews != null && news.size > 0) {
            outState?.putParcelable(KEY_VK_NEWS, postNews?.copy(news = news))
        }
    }

    private fun requestNews() {
        val subscription = newsManager.getNews(postNews?.after ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { retrievedNews ->
                            postNews = retrievedNews
                            (news_list.adapter as NewsAdapter).addNews(retrievedNews.news)
                        },
                        { e ->
                            Snackbar.make(news_list, e.message ?: "", Snackbar.LENGTH_SHORT).show()
                        })

        subscriptions.add(subscription)
    }

    private fun initAdapter() {
        if (news_list.adapter == null) {
            news_list.adapter = NewsAdapter()
        }
    }

}