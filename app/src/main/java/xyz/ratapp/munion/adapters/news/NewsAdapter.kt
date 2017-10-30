package xyz.ratapp.munion.adapters.news

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView

import android.view.ViewGroup
import xyz.ratapp.munion.adapters.AdapterConstants
import xyz.ratapp.munion.adapters.ViewType
import xyz.ratapp.munion.adapters.ViewTypeDelegateAdapter
import xyz.ratapp.munion.models.PostNewsItem


/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */


class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    private val loadingItem = object : ViewType {
        override fun getViewType(): Int {
            return AdapterConstants.LOADING
        }
    }

    init {
        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(AdapterConstants.NEWS, NewsDelegateAdapter())
        items = ArrayList()
        items.add(loadingItem)
    }

    fun addNews(news: List<PostNewsItem>) {
        // first remove loading and notify
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)
        // insert news and the loading at the end of the list
        items.addAll(news)
        items.add(loadingItem)
        notifyItemRangeChanged(initPosition, items.size + 1 /* plus loading item */)
    }

    fun clearAndAddNews(news: List<PostNewsItem>){
        items.clear()
        notifyItemRangeRemoved(0, getLastPosition())
        items.addAll(news)
        items.add(loadingItem)
        notifyItemRangeInserted(0, items.size)
    }

    fun getNews(): List<PostNewsItem> = items
            .filter { it.getViewType() == AdapterConstants.NEWS }
            .map { it as PostNewsItem }

    private fun getLastPosition() = if (items.lastIndex == -1) 0 else items.lastIndex

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, this.items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return this.items.get(position).getViewType()
    }

}