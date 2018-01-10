package xyz.ratapp.munion.ui.fragments.hypothec

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_hypothec.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.ratapp.munion.ui.activities.CameraActivity
import xyz.ratapp.munion.R
import xyz.ratapp.munion.controllers.interfaces.DataCallback
import xyz.ratapp.munion.data.DataController
import xyz.ratapp.munion.data.pojo.HypothecData
import xyz.ratapp.munion.helpers.PreferencesHelper
import xyz.ratapp.munion.helpers.email.Sender

/**
 * <p>Date: 29.10.17</p>
 * @author Simon
 */
class HypothecFragment : Fragment() {

    companion object {
        val REQUEST_CODE_PHOTO: Int = 801
    }

    var photoUris: ArrayList<Uri> = ArrayList()
    var count = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_hypothec, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        if(PreferencesHelper.getInstance(activity).
                wasHypothecDataSet()) {
            hypothec_edit_name.visibility = View.INVISIBLE
            hypothec_edit_phone.visibility = View.INVISIBLE
            hypothec_btn_order.visibility = View.INVISIBLE
        }

        val instance = DataController.getInstance(activity)
        instance.loadHypothec(object: DataCallback<HypothecData> {
            override fun onSuccess(data: HypothecData) {
                if(data.comments != null &&
                        data.comments.isNotBlank()) {
                    hypothec_text.text = data.comments
                }
                else {
                    if(data.name != null) {
                        hypothec_edit_name!!.setText(data.name)
                    }
                    if(data.phone != null) {
                        hypothec_edit_phone!!.setText(data.phone)
                    }
                }
            }

            override fun onFailed(thr: Throwable?) {
                PreferencesHelper.getInstance(activity).
                        saveHypothecDataWasSet(false)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        hypothec_btn_order.apply {
            hypothec_btn_order.setOnClickListener {
                val name = hypothec_edit_name!!.text.toString()
                val phone = hypothec_edit_phone!!.text.toString()

                if(dataIsValid(name, phone)) {
                    createContact(name, phone)
                    runCamera("Сделайте фото первой страницы паспорта", R.drawable.passport_first_page, 125, 88 * 2)
                }
                else {
                    Toast.makeText(context,
                            R.string.fill_hypothec_data,
                            Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun createContact(name: String, phone: String) {
        val instance = DataController.getInstance(activity)
        instance.createContact(name, phone, object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                val data = response!!.body()

                if(data != null) {
                    val id = data.get("result").asInt.toString()
                    PreferencesHelper.getInstance(activity).saveHypothecId(id)
                    instance.setHypothecId(id + "")
                }
                else {
                    createContact(name, phone)
                }
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                createContact(name, phone)
            }
        })
    }

    private fun dataIsValid(name: String, phone: String): Boolean {
        return phone.matches(Regex("^\\+\\d{11}$")) &&
                name.isNotBlank()
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
                    runCamera("Сделайте фото второй страницы паспорта", R.drawable.passport_second_page, 125, 88 * 2)
                }

                2 -> {
                    runCamera("Сделайте фото ИНН", R.drawable.inn, 210, 297)
                }

                3 -> {
                    runCamera("Сделайте фото СНИЛС", R.drawable.snils, 115, 80)
                }

                4 -> {
                    PreferencesHelper.getInstance(activity).
                            saveHypothecDataWasSet(true)
                    Sender.getInstance()
                            .sendHypothecMessage(activity,
                                    hypothec_edit_name.text.toString(),
                                    hypothec_edit_phone.text.toString(),
                                    photoUris)
                    hypothec_text.text = activity.getString(R.string.hypothec_start_status)
                    hypothec_edit_name.visibility = View.INVISIBLE
                    hypothec_edit_phone.visibility = View.INVISIBLE
                    hypothec_btn_order.visibility = View.INVISIBLE
                }

            }

        }
    }

    fun runCamera(text: String, @DrawableRes mask: Int, width: Int, height: Int) {
        val i = CameraActivity.newIntent(activity, text, mask, width, height)
        startActivityForResult(i, REQUEST_CODE_PHOTO)
        count++
    }

}