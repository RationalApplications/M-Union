package xyz.ratapp.munion.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import xyz.ratapp.munion.R
import xyz.ratapp.munion.extensions.inflate

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
class LoadingDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup) = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
    }

    class TurnsViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_news_loading)){
    }
}