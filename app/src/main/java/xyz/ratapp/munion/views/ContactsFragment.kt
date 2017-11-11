package xyz.ratapp.munion.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.ratapp.munion.extensions.inflate
import android.content.Intent
import android.net.Uri
import android.util.Log
import kotlinx.android.synthetic.main.fragment_contacts.*
import xyz.ratapp.munion.views.common.FragmentBase
import xyz.ratapp.munion.R
import xyz.ratapp.munion.extensions.openItLink


/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
class ContactsFragment : FragmentBase() {
    override fun getFragmentName(context: Context): String {
        return context.resources.getString(R.string.title_contacts)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return container?.inflate(R.layout.fragment_contacts, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        contacts_telephone_body.apply {
            contacts_telephone_body.setOnClickListener({
                val phone = resources.getText(R.string.contacts_phone)
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:" + phone)

                try {
                    context.startActivity(intent)
                } catch (e: SecurityException) {
                    Log.e("ContactsTag", "Ошибка звонка")
                }
            })
        }

        contacts_vk_body.apply {
            contacts_vk_body.setOnClickListener({
                val link = resources.getText(R.string.contacts_vk) as String
                link.openItLink(activity)
            })
        }

        contacts_site_body.apply {
            contacts_site_body.setOnClickListener(View.OnClickListener {
                val link = "http://www.m-union.one"
                link.openItLink(activity)
            })
        }

        contacts_address_body.apply {
            contacts_address_body.setOnClickListener(View.OnClickListener {
                //TODO: Я не смог перенести это в ресурсы, потому что лагала студия(
                val address = "0,0?q=Выборгское+ш.,+36,+Санкт-Петербург,+194214"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("geo:" + address)

                try {
                    context.startActivity(intent)
                } catch (e: SecurityException) {
                    Log.e("ContactsTag", "Ошибка открытия карты")
                }
            })
        }

    }



}