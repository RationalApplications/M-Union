package xyz.ratapp.munion

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_auth.*
import xyz.ratapp.munion.extensions.inflate

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */

class AuthFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_auth, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        auth_submit.apply {
            auth_submit.setOnClickListener { v ->
                (activity as MainActivity).initAuth()
            }
        }
    }

}