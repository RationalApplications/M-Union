package xyz.ratapp.munion.common

import rx.Observable
import xyz.ratapp.munion.models.PostNews
import xyz.ratapp.munion.models.PostNewsItem

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
class NewsManager() {

    fun getNews(after: String, limit: String = "10"): Observable<PostNews> {
        return Observable.create { subscriber ->


            val news = ArrayList<PostNewsItem>()
            news.add(PostNewsItem("Центр вторичного жилья ", "text text", "https://pp.userapi.com/c639323/v639323758/27deb/L0SSSPfpEfA.jpg", "https://vk.com/wall-115518472_254", 1000 ))
            news.add(PostNewsItem("Центр вторичного жилья ", "text text text text", "https://pp.userapi.com/c639323/v639323758/27deb/L0SSSPfpEfA.jpg", "https://vk.com/wall-115518472_254", 1000 ))

            val redditNews = PostNews("", "", news)

            subscriber.onNext(redditNews)
            subscriber.onCompleted()

        }
    }

}