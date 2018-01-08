package xyz.ratapp.munion.ui.fragments.hypothec

import android.content.Context
import android.os.Bundle
import xyz.ratapp.munion.R
import xyz.ratapp.munion.ui.fragments.common.RootFragment

/**
 * <p>Date: 07.11.17</p>
 * @author Simon
 */
class HypothecRootFragment : RootFragment() {

    override fun getFragmentName(context: Context): String {
        return context.resources.getString(R.string.title_credit)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        changeFragment(HypothecFragment())
    }
}