package xyz.ratapp.munion.common

import rx.Observable
import xyz.ratapp.munion.models.ChatModel
import xyz.ratapp.munion.models.ChatModelItem
import xyz.ratapp.munion.models.PostNews
import xyz.ratapp.munion.models.PostNewsItem

/**
 * <p>Date: 30.10.17</p>
 * @author Simon
 */
class ChatManager {

    fun getMessages(): Observable<ChatModel> {
        return Observable.create { subscriber ->
            val messages = ArrayList<ChatModelItem>()

            messages.add(ChatModelItem("hello", 1))
            messages.add(ChatModelItem("hello!", 0))
            messages.add(ChatModelItem("helllllo!\nhelllloe", 0))
            messages.add(ChatModelItem("okay", 1))


            val chatModel = ChatModel(messages)
            subscriber.onNext(chatModel)
            subscriber.onCompleted()
        }
    }

}