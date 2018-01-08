package xyz.ratapp.munion.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.net.Uri
import android.util.Log
import kotlinx.android.synthetic.main.fragment_contacts.*
import xyz.ratapp.munion.ui.fragments.common.BaseFragment
import xyz.ratapp.munion.R


/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
class ContactsFragment : BaseFragment() {
    override fun getFragmentName(context: Context): String {
        return context.resources.getString(R.string.title_contacts)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        contacts_telephone_body.apply {
            contacts_telephone_body.setOnClickListener({
                val phone = resources.getText(R.string.contacts_phone)
                val intent = Intent(Intent.ACTION_DIAL)
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
            contacts_site_body.setOnClickListener({
                val link = "http://www.m-union.one"
                link.openItLink(activity)
            })
        }

        contacts_address_body.apply {
            contacts_address_body.setOnClickListener({
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

    private fun String.openItLink(context: Context) {
        //TODO: в ресурсы
        val VK_APP_PACKAGE_ID = getString(R.string.vk_package)

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(this))
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)

        if (resInfo.isEmpty()) return

        for (info in resInfo) {
            if (info.activityInfo == null) continue
            if (VK_APP_PACKAGE_ID == info.activityInfo.packageName) {
                intent.`package` = info.activityInfo.packageName
                break
            }
        }
        context.startActivity(intent)
    }
}