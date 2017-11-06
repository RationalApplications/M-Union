package xyz.ratapp.munion.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_auth.*
import xyz.ratapp.munion.views.common.FragmentBase
import xyz.ratapp.munion.MainActivity
import xyz.ratapp.munion.R
import xyz.ratapp.munion.extensions.inflate

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */

class AuthFragment : FragmentBase() {

    override fun getFragmentName(context: Context): String {
        return context.resources.getString(R.string.title_account)
    }

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