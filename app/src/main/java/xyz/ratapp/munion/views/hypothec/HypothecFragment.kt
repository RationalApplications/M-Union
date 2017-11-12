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

    var photoUris: ArrayList<Uri> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_hypothec, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        hypothec_btn_order.apply {
            hypothec_btn_order.setOnClickListener {
                val i = CameraActivity.newIntent(activity, "Новое фото", 350, 560)
                startActivityForResult(i, REQUEST_CODE_PHOTO)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK){
            return
        }

        if (requestCode  == REQUEST_CODE_PHOTO){
            if (data == null){
                return
            }
            photoUris.add(CameraActivity.getPhotoUri(data))

            Log.d("TAG", photoUris[0].toString())

            hypothec_text.text = "Заявка отправлена на обработку"
            hypothec_edit_name.visibility = View.INVISIBLE
            hypothec_edit_phone.visibility = View.INVISIBLE
            hypothec_btn_order.visibility = View.INVISIBLE
        }
    }

}