package xyz.ratapp.munion.ui.fragments.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.ratapp.munion.R

/**
 *
 * Date: 05.11.17
 *
 * @author Simon
 */

abstract class RootFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_root, container, false)
    }

    fun changeFragment(fragment : Fragment){
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.child_container, fragment)
        ft.commit()
    }
}
