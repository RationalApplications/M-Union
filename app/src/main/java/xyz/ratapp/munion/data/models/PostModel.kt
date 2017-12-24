package xyz.ratapp.munion.data.models

import android.os.Parcel
import android.os.Parcelable
import xyz.ratapp.munion.ui.adapters.AdapterConstants
import xyz.ratapp.munion.ui.adapters.ViewType
import xyz.ratapp.munion.extensions.createParcel

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */

data class PostNews(
        val after: String,
        val before: String,
        val news: List<PostNewsItem>) : Parcelable {

    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = createParcel { PostNews(it) }
    }

    protected constructor(parcelIn: Parcel) : this(
            parcelIn.readString(),
            parcelIn.readString(),
            mutableListOf<PostNewsItem>().apply {
                parcelIn.readTypedList(this, PostNewsItem.CREATOR)
            }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(after)
        dest.writeString(before)
        dest.writeTypedList(news)
    }

    override fun describeContents() = 0

}

data class PostNewsItem(
        val author: String,
        val text: String,
        val thumbnail: String,
        val url: String,
        val image: String,
        val created: Long
) : ViewType, Parcelable {

    override fun getViewType(): Int {
        return AdapterConstants.NEWS
    }

    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = createParcel { PostNewsItem(it) }
    }

    protected constructor(parcelIn: Parcel) : this(
            parcelIn.readString(),
            parcelIn.readString(),
            parcelIn.readString(),
            parcelIn.readString(),
            parcelIn.readString(),
            parcelIn.readLong()

    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(author)
        dest.writeString(text)
        dest.writeString(thumbnail)
        dest.writeString(url)
        dest.writeString(image)
        dest.writeLong(created)
    }

    override fun describeContents() = 0
}