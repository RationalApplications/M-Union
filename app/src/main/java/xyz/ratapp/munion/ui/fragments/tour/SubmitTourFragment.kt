package xyz.ratapp.munion.ui.fragments.tour

import agency.tango.materialintroscreen.SlideFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jkb.vcedittext.VerificationAction
import kotlinx.android.synthetic.main.fragment_tour_submit.*
import xyz.ratapp.munion.R
import xyz.ratapp.munion.controllers.interfaces.SmsCodeCallback

/**
 * Created by timtim on 07/01/2018.
 */
class SubmitTourFragment : SlideFragment() {

    lateinit var smsCallback: SmsCodeCallback
    private var phone: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_tour_submit, container, false)
    }

    override fun backgroundColor(): Int {
        return R.color.colorPrimary
    }

    override fun buttonsColor(): Int {
        return R.color.colorAccent
    }

    override fun canMoveFurther(): Boolean {
        return vcet_sms_code.text.toString().length == 6
    }

    override fun cantMoveFurtherErrorMessage(): String {
        return getString(R.string.fill_sms_code)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vcet_sms_code.setOnVerificationCodeChangedListener(object:
                VerificationAction.OnVerificationCodeChangedListener {

            override fun onInputCompleted(s: CharSequence) {
                if(s.length == 6) {
                    smsCallback.onSmsCodeTaken(s.toString())
                }
            }

            override fun onVerCodeChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        tv_head_text.text = getString(R.string.fill_code_sent_on_phone, phone!!)
    }

    fun setPhoneNumber(phone: String) {
        this.phone = phone
    }
}