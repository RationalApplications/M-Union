package xyz.ratapp.munion.views.common

import rx.subscriptions.CompositeSubscription

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
abstract class RxBaseFragment: FragmentBase() {
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

    abstract override fun getFragmentName(): String
}