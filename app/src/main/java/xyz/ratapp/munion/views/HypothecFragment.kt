package xyz.ratapp.munion.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.ratapp.munion.views.common.FragmentBase
import xyz.ratapp.munion.R

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
class HypothecFragment : FragmentBase() {

    override fun getFragmentName(): String {
        return getString(R.string.title_credit)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}