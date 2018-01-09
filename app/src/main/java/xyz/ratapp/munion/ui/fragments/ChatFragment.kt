package xyz.ratapp.munion.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import co.chatsdk.core.audio.Recording
import co.chatsdk.core.dao.Message
import co.chatsdk.core.dao.Thread
import co.chatsdk.core.dao.User
import co.chatsdk.core.handlers.TypingIndicatorHandler
import co.chatsdk.core.interfaces.ChatOption
import co.chatsdk.core.interfaces.ChatOptionsDelegate
import co.chatsdk.core.interfaces.ChatOptionsHandler
import co.chatsdk.core.interfaces.ThreadType
import co.chatsdk.core.session.ChatSDK
import co.chatsdk.core.session.NM
import co.chatsdk.core.session.StorageManager
import co.chatsdk.core.types.ChatOptionType
import co.chatsdk.core.types.MessageSendProgress
import co.chatsdk.core.utils.ActivityResult
import co.chatsdk.core.utils.DisposableList
import co.chatsdk.core.utils.PermissionRequestHandler
import co.chatsdk.core.utils.Strings
import co.chatsdk.ui.chat.ChatActivity
import co.chatsdk.ui.chat.MessagesListAdapter
import co.chatsdk.ui.main.MainActivity
import co.chatsdk.ui.manager.BaseInterfaceAdapter.THREAD_ENTITY_ID
import co.chatsdk.ui.manager.InterfaceManager
import co.chatsdk.ui.threads.ThreadImageBuilder
import co.chatsdk.ui.utils.ToastHelper
import com.facebook.drawee.view.SimpleDraweeView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.apache.commons.lang3.StringUtils
import timber.log.Timber
import xyz.ratapp.munion.R
import xyz.ratapp.munion.ui.fragments.common.BaseFragment
import xyz.ratapp.munion.ui.views.TextInputDelegate
import xyz.ratapp.munion.ui.views.TextInputView
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.prefs.Preferences

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
class ChatFragment : BaseFragment(), TextInputDelegate, ChatOptionsDelegate {

    private lateinit var optionsHandler: ChatOptionsHandler
    private val activityResultPublishSubject = PublishSubject.create<ActivityResult>()

    enum class ListPosition {
        Top, Current, Bottom
    }

    private val enableTrace = false

    /**
     * The key to get the thread long id.
     */
    val LIST_POS = "list_pos"

    /**
     * Pass true if you want slide down animation for this context exit.
     */
    val ANIMATE_EXIT = "animate_exit"

    private lateinit var textInputView: TextInputView
    private lateinit var recyclerView: RecyclerView
    private var messageListAdapter: MessagesListAdapter? = null
    private var thread: Thread? = null
    private lateinit var subtitleTextView: TextView
    private var permissionHandler = PermissionRequestHandler()

    private val disposableList = DisposableList()
    private var typingTimerDisposable: Disposable? = null

    private lateinit var progressBar: ProgressBar
    private var listPos = -1


    override fun getFragmentName(context: Context): String {
        return context.resources.getString(R.string.title_chat)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.chat_sdk_activity_chat, container, false)
    }

    override fun onStart() {
        super.onStart()
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        if (savedInstanceState != null) {
            listPos = savedInstanceState.getInt(LIST_POS, -1)
            savedInstanceState.remove(LIST_POS)
        }

        // If the context is just been created we load regularly, else we load and retain position
        loadMessages(true, -1, ListPosition.Bottom)

        setChatState(TypingIndicatorHandler.State.active)
    }

    fun setupThread(threadEntityID: String) {
        thread = StorageManager.shared().
                fetchThreadWithEntityID(threadEntityID)
    }

    fun loadMessages(showLoadingIndicator: Boolean, amountToLoad: Int, toPosition: ListPosition) {

        if (showLoadingIndicator) {
            recyclerView.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }

        NM.thread().loadMoreMessagesForThread(null, thread)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { messages, throwable ->
                    progressBar.visibility = View.INVISIBLE

                    messageListAdapter!!.setMessages(messages)

                    if (showLoadingIndicator) {
                        //animateListView();
                    }
                    recyclerView.visibility = View.VISIBLE

                    scrollListTo(toPosition, !showLoadingIndicator)
                }
    }

    private fun initViews() {
        textInputView = view!!.findViewById(R.id.chat_sdk_message_box)
        textInputView.setDelegate(this)
        textInputView.setAudioModeEnabled(NM.audioMessage() != null)

        progressBar = view!!.findViewById(co.chatsdk.ui.R.id.chat_sdk_progressbar)

        val mSwipeRefresh = view!!.findViewById<SwipeRefreshLayout>(co.chatsdk.ui.R.id.ptr_layout)

        mSwipeRefresh.setOnRefreshListener {
            val items = messageListAdapter!!.getMessageItems()
            var firstMessage: Message? = null
            if (items.size > 0) {
                firstMessage = items[0].message
            }

            disposableList.add(NM.thread().loadMoreMessagesForThread(firstMessage, thread)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { messages, throwable ->
                        if (throwable == null) {
                            if (messages.size < 2) {
                                showToast(getString(co.chatsdk.ui.R.string.chat_activity_no_more_messages_to_load_toast))
                            } else {
                                for (m in messages) {
                                    messageListAdapter!!.addRow(m, false, false)
                                }
                                messageListAdapter!!.sortAndNotify()
                                recyclerView.getLayoutManager().scrollToPosition(messages.size)
                            }
                        }
                        mSwipeRefresh.isRefreshing = false
                    })
        }

        recyclerView = view!!.findViewById<RecyclerView>(co.chatsdk.ui.R.id.list_chat)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        if (messageListAdapter == null) {
            messageListAdapter = MessagesListAdapter(activity as AppCompatActivity)
        }

        recyclerView.setAdapter(messageListAdapter)
    }

    private fun handleMessageSend(observable: Observable<MessageSendProgress>) {
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MessageSendProgress> {
                    override fun onSubscribe(d: Disposable) {
                        //disposableList.add(d);
                    }

                    override fun onNext(messageSendProgress: MessageSendProgress) {
                        Timber.v("Message Status: " + messageSendProgress.status)
                        messageListAdapter!!.addRow(messageSendProgress.message, true, true)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        ToastHelper.show(activity.applicationContext, co.chatsdk.ui.R.string.unable_to_send_image_message)
                    }

                    override fun onComplete() {
                        messageListAdapter!!.notifyDataSetChanged()
                        scrollListTo(ListPosition.Bottom, false)
                    }
                })
    }

    fun scrollListTo(position: Int, animated: Boolean) {
        listPos = position

        if (animated) {
            recyclerView.smoothScrollToPosition(listPos)
        } else {
            recyclerView.layoutManager.scrollToPosition(listPos)
        }
    }

    fun scrollListTo(position: ListPosition, animated: Boolean) {

        var pos = 0

        when (position) {
            ListPosition.Top -> pos = 0
            ListPosition.Current -> pos = if (listPos == -1) messageListAdapter!!.size() - 1 else listPos
            ListPosition.Bottom -> pos = messageListAdapter!!.size() - 1
        }

        scrollListTo(pos, animated)
    }

    /**
     * Send text message
     *
     * @param text to send.
     * @param clearEditText if true clear the message edit text.
     */
    fun sendMessage(text: String, clearEditText: Boolean) {

        if (StringUtils.isEmpty(text) || StringUtils.isBlank(text)) {
            return
        }

        handleMessageSend(NM.thread().sendMessageWithText(text.trim { it <= ' ' }, thread))

        if (clearEditText && textInputView != null) {
            textInputView.clearText()
        }

        stopTyping(false)
        scrollListTo(ListPosition.Bottom, false)
    }

    private fun setChatState(state: TypingIndicatorHandler.State) {
        if (NM.typingIndicator() != null) {
            disposableList.add(NM.typingIndicator().setChatState(state, thread)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe())
        }
    }

    private fun stopTyping(inactive: Boolean) {
        if (typingTimerDisposable != null) {
            typingTimerDisposable!!.dispose()
            typingTimerDisposable = null
        }
        if (inactive) {
            setChatState(TypingIndicatorHandler.State.inactive)
        } else {
            setChatState(TypingIndicatorHandler.State.active)
        }
    }

    override fun executeChatOption(option: ChatOption) {
        if (option.type == ChatOptionType.SendMessage) {
            handleMessageSend(option.execute(activity, activityResultPublishSubject, thread) as Observable<MessageSendProgress>)
        } else {
            option.execute(activity, activityResultPublishSubject, thread).subscribe()
        }
    }

    override fun showOptions() {
        optionsHandler = InterfaceManager.shared().a.getChatOptionsHandler(this)
        optionsHandler.show(activity)
    }

    override fun hideOptions() {
        if (optionsHandler != null) {
            optionsHandler.hide()
        }
    }

    override fun onSendPressed(text: String) {
        sendMessage(text, true)
    }

    override fun startTyping() {
        setChatState(TypingIndicatorHandler.State.composing)
        typingTimerDisposable = Observable.just(true).delay(5000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe { setChatState(TypingIndicatorHandler.State.active) }
    }

    override fun sendAudio(recording: Recording) {
        if (NM.audioMessage() != null) {
            handleMessageSend(NM.audioMessage().sendMessage(recording, thread))
        }
    }

    override fun stopTyping() {
        stopTyping(false)
    }

    override fun onKeyboardShow() {
        scrollListTo(ListPosition.Bottom, false)
    }

    override fun onKeyboardHide() {
        scrollListTo(ListPosition.Bottom, false)
    }

    private fun showToast(string: String) {
        Toast.makeText(activity, string, Toast.LENGTH_SHORT).show()
    }

}