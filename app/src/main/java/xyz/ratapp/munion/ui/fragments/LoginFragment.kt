package xyz.ratapp.munion.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_auth.*
import xyz.ratapp.munion.ui.fragments.common.BaseFragment
import xyz.ratapp.munion.R
import xyz.ratapp.munion.ui.activities.AuthActivity

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */

class LoginFragment : BaseFragment() {

    override fun getFragmentName(context: Context): String {
        return context.resources.getString(R.string.title_account)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        auth_submit.apply {
            auth_submit.setOnClickListener {

                val phone = edit_phone!!.text.toString()
                val password = edit_pass!!.text.toString()

                if(dataIsValid(phone, password)) {
                    val intent = AuthActivity.getAuthIntent(phone, password)
                    activity.startActivityForResult(intent, AuthActivity.REQUEST_AUTH_CODE)
                }
                else {
                    Toast.makeText(context, R.string.auth_validation_toast, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun dataIsValid(phone: String, password: String): Boolean {
        return phone.matches(Regex("^\\+\\d{11}$")) &&
                password.isNotBlank()
    }

}