package xyz.ratapp.munion.ui.adapters.news

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import xyz.ratapp.munion.R
import xyz.ratapp.munion.ui.adapters.ViewType
import xyz.ratapp.munion.ui.adapters.ViewTypeDelegateAdapter
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