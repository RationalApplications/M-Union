package xyz.ratapp.munion

import android.support.v4.app.Fragment
import rx.subscriptions.CompositeSubscription

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
open class RxBaseFragment: Fragment() {
    protected var subscriptions = CompositeSubscription()

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeSubscription()
    }

    override fun onPause() {
        super.onPause()
        if (!subscriptions.isUnsubscribed){
            subscriptions.unsubscribe()
        }
        subscriptions.clear()
    }
}