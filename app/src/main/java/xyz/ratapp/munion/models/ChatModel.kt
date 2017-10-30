package xyz.ratapp.munion.models

import android.os.Parcel
import android.os.Parcelable
import xyz.ratapp.munion.adapters.AdapterConstants
import xyz.ratapp.munion.adapters.ViewType
import xyz.ratapp.munion.extensions.createParcel

/**
 * <p>Date: 30.10.17</p>
 * @author Simon
 */
data class ChatModel(val chat: List<ChatModelItem>) : Parcelable {
    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = createParcel { ChatModel(it) }
    }

    protected constructor(parcelIn: Parcel) : this(
            mutableListOf<ChatModelItem>().apply {
                parcelIn.readTypedList(this, ChatModelItem.CREATOR)
            }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(chat)
    }

    override fun describeContents() = 0
}

data class ChatModelItem(val text: String, val isUser: Int) : ViewType, Parcelable {
    override fun getViewType(): Int {
        if (isUser == 1) {
            return AdapterConstants.CHAT_USER
        } else {
            return AdapterConstants.CHAT_MODER
        }
    }

    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = createParcel { ChatModelItem(it) }
    }

    protected constructor(parcelIn: Parcel) : this(
            parcelIn.readString(),
            parcelIn.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(text)
        dest.writeInt(isUser)
    }

    override fun describeContents() = 0
}