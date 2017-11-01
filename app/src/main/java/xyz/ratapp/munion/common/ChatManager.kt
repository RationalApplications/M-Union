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

            messages.add(ChatModelItem("Напоминаю, завтра назначен просмотр на квартиру в 10:00", 1))
            messages.add(ChatModelItem("Хорошо, спасибо за напоминание! ", 0))
            messages.add(ChatModelItem("А не могли бы вы мне скинуть информацию о том, сколько человек звонило по поводу квартиры?", 0))
            messages.add(ChatModelItem("Данную информацию вы можете посмотреть самостоятельно во вкладке статистики. Так же там вы можете найти детализацию звонков.", 1))


            val chatModel = ChatModel(messages)
            subscriber.onNext(chatModel)
            subscriber.onCompleted()
        }
    }

}