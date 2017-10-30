package xyz.ratapp.munion.adapters.chat


import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_chat_left.view.*
import xyz.ratapp.munion.R
import xyz.ratapp.munion.adapters.ViewType
import xyz.ratapp.munion.adapters.ViewTypeDelegateAdapter
import xyz.ratapp.munion.extensions.inflate
import xyz.ratapp.munion.models.ChatModelItem

/**
 * <p>Date: 30.10.17</p>
 * @author Simon
 */
class ModeratorChatDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as TurnsViewHolder
        holder.bind(item as ChatModelItem)
    }

    class TurnsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_chat_left)) {
        fun bind(item: ChatModelItem) = with(itemView){
            chat_text.text = item.text
        }
    }

}