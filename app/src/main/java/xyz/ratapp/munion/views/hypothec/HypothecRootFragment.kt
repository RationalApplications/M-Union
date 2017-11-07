package xyz.ratapp.munion.views.hypothec

import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_hypothec.*
import xyz.ratapp.munion.R
import xyz.ratapp.munion.views.CameraFragment
import xyz.ratapp.munion.views.common.FragmentRoot

/**
 * <p>Date: 07.11.17</p>
 * @author Simon
 */
class HypothecRootFragment : FragmentRoot() {

    override fun getFragmentName(context: Context): String {
        return context.resources.getString(R.string.title_credit)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        changeFragment(HypothecFragment())
    }
}