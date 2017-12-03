package xyz.ratapp.munion.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_auth.*
import xyz.ratapp.munion.AuthActivity
import xyz.ratapp.munion.CabinetActivity
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
            auth_submit.setOnClickListener {

                val i = Intent(context, AuthActivity::class.java)
                if (edit_phone?.text.toString() != "" && (edit_phone?.text.toString()).startsWith('+') && edit_phone?.text.toString().length == 12)
                {
                        i.putExtra("tel", edit_phone?.text.toString())
                        if (edit_pass?.text.toString()!= "")
                        {
                            i.putExtra("pass", edit_pass?.text.toString())
                            startActivityForResult(i, 0)
                        }
                        else {
                            Toast.makeText(context, "Введите пароль", Toast.LENGTH_LONG).show()
                        }
                }
                else {
                    Toast.makeText(context, "Введите корректный номер телефона начиная с \"+\" " + (edit_phone?.text.toString() != ""), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}