package xyz.ratapp.munion

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_chat.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import xyz.ratapp.munion.adapters.chat.ChatAdapter
import xyz.ratapp.munion.common.ChatManager
import xyz.ratapp.munion.common.NewsManager
import xyz.ratapp.munion.extensions.inflate
import xyz.ratapp.munion.models.ChatModel
import xyz.ratapp.munion.models.ChatModelItem
import xyz.ratapp.munion.models.PostNews

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
class ChatFragment : RxBaseFragment() {

    companion object {
        private val KEY_CHAT = "chatMessages"
    }

    private var chatList: ChatModel? = null
    private val chatManager by lazy { ChatManager() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_chat, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        chat_list.apply {
            chat_list.setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            chat_list.layoutManager = linearLayout
        }

        initAdapter()

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CHAT)) {
            chatList = savedInstanceState.get(KEY_CHAT) as ChatModel
            (chat_list.adapter as ChatAdapter).clearAndAddMessages(chatList!!.chat)
        } else {
            requestChat()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val messages = (chat_list.adapter as ChatAdapter).getMessages()
        if (chatList != null && messages.size > 0) {
            outState?.putParcelable(KEY_CHAT, chatList?.copy(chat = messages))
        }
    }

    private fun requestChat() {
        val subscription = chatManager.getMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { retrieved ->
                            chatList = retrieved
                            (chat_list.adapter as ChatAdapter).addMessages(retrieved.chat)
                        },
                        { e ->
                            Snackbar.make(chat_list, e.message ?: "", Snackbar.LENGTH_SHORT).show()
                        })

        subscriptions.add(subscription)
    }

    private fun initAdapter() {
        if (chat_list.adapter == null) {
            chat_list.adapter = ChatAdapter()
        }
    }

}