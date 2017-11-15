package xyz.ratapp.munion.adapters.chat

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import xyz.ratapp.munion.adapters.AdapterConstants
import xyz.ratapp.munion.adapters.ViewType
import xyz.ratapp.munion.adapters.ViewTypeDelegateAdapter
import xyz.ratapp.munion.adapters.news.LoadingDelegateAdapter
import xyz.ratapp.munion.adapters.news.NewsDelegateAdapter
import xyz.ratapp.munion.models.ChatModelItem
import xyz.ratapp.munion.models.PostNewsItem

/**
 * <p>Date: 30.10.17</p>
 * @author Simon
 */
class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(AdapterConstants.CHAT_MODER, UserChatDelegateAdapter())
        delegateAdapters.put(AdapterConstants.CHAT_USER, ModeratorChatDelegateAdapter())
        items = ArrayList()
    }

    fun addMessages(news: List<ChatModelItem>) {
        // insert news and the loading at the end of the list
        items.addAll(news)
        notifyItemRangeInserted(0, items.size)
    }

    fun clearAndAddMessages(news: List<ChatModelItem>){
        items.clear()
        notifyItemRangeRemoved(0, getLastPosition())
        items.addAll(news)
        notifyItemRangeInserted(0, items.size)
    }

    fun getMessages(): List<ChatModelItem> = items
            .filter { it.getViewType() == AdapterConstants.NEWS }
            .map { it as ChatModelItem }

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