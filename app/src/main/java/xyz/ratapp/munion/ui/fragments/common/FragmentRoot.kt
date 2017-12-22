package xyz.ratapp.munion.ui.fragments.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.ratapp.munion.R
import xyz.ratapp.munion.extensions.inflate

/**
 *
 * Date: 05.11.17
 *
 * @author Simon
 */

abstract class FragmentRoot : FragmentBase() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_root, false)
    }

    fun changeFragment(fragment : Fragment){
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.child_container, fragment)
        ft.commit()
    }
}
