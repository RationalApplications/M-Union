package xyz.ratapp.munion.views.hypothec

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_hypothec.*
import xyz.ratapp.munion.views.common.FragmentBase
import xyz.ratapp.munion.R
import xyz.ratapp.munion.extensions.inflate
import xyz.ratapp.munion.views.CameraFragment
import xyz.ratapp.munion.views.common.FragmentRoot

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
class HypothecFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_hypothec, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_order.apply {
            btn_order.setOnClickListener {
                (parentFragment as FragmentRoot).changeFragment(CameraFragment())
            }
        }
    }

}