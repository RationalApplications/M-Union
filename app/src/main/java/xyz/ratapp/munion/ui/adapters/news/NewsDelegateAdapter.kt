package xyz.ratapp.munion.ui.adapters.news

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_post.view.*
import xyz.ratapp.munion.R
import xyz.ratapp.munion.ui.adapters.ViewType
import xyz.ratapp.munion.ui.adapters.ViewTypeDelegateAdapter
import xyz.ratapp.munion.extensions.*
import xyz.ratapp.munion.data.models.PostNewsItem

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
class NewsDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as TurnsViewHolder
        holder.bind(item as PostNewsItem)
    }

    class TurnsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_post)) {
        fun bind(item: PostNewsItem) = with(itemView){

            itemView.setOnClickListener {
                var url = item.url
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;

                url.openItLink(context)

//                try {
//                    val UrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                    startActivity(context, UrlIntent, null)
//                } catch (e: ActivityNotFoundException) {
//                    Snackbar.make(post_text, "No application can handle this request." + " Please install a webbrowser", Snackbar.LENGTH_LONG).show()
//                    e.printStackTrace()
//                }

            }


            post_image.loadImg(item.image)

            post_icon.loadImg(item.thumbnail)
            post_author.text = item.author
            post_time.text = item.created.getFriendlyTime()
            post_text.text = item.text
        }
    }
}