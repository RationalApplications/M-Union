package xyz.ratapp.munion.views.hypothec

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_hypothec.*
import xyz.ratapp.munion.CameraActivity
import xyz.ratapp.munion.views.common.FragmentBase
import xyz.ratapp.munion.R
import xyz.ratapp.munion.email.Sender
import xyz.ratapp.munion.extensions.inflate
import xyz.ratapp.munion.views.CameraFragment
import xyz.ratapp.munion.views.common.FragmentRoot

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
class HypothecFragment : Fragment() {

    companion object {
        val REQUEST_CODE_PHOTO: Int = 801;
    }

    //TODO: убрать счетчик

    var photoUris: ArrayList<Uri> = ArrayList()
    var count = 0;

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_hypothec, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        hypothec_btn_order.apply {
            hypothec_btn_order.setOnClickListener {
                runCamera("Сделайте фото паспорта", 88 * 7, 125 * 7)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_PHOTO) {
            if (data == null) {
                return
            }
            photoUris.add(CameraActivity.getPhotoUri(data))

            when (count) {
                1 -> {
                    runCamera("Сделайте фото ИНН", 210 * 3, 297 * 3)
                }

                2 -> {
                    runCamera("Сделайте фото СНИЛС", 115 * 6, 80 * 6)
                }

                3 -> {
                    Sender.getInstance()
                            .sendMessage(activity,
                                    hypothec_edit_name.text.toString(),
                                    hypothec_edit_phone.text.toString(),
                                    photoUris)
                    hypothec_text.text = "Заявка отправлена на рассмотрение"
                    hypothec_edit_name.visibility = View.INVISIBLE
                    hypothec_edit_phone.visibility = View.INVISIBLE
                    hypothec_btn_order.visibility = View.INVISIBLE
                }

            }

        }
    }

    fun runCamera(text: String, width: Int, height: Int) {
        val i = CameraActivity.newIntent(activity, text, width, height)
        startActivityForResult(i, REQUEST_CODE_PHOTO)
        count++
    }

}