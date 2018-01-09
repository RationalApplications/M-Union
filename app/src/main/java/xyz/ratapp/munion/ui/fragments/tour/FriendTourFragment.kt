package xyz.ratapp.munion.ui.fragments.tour

import agency.tango.materialintroscreen.SlideFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_tour_friend.*
import xyz.ratapp.munion.R
import xyz.ratapp.munion.controllers.interfaces.CodeInputCallback

/**
 * Created by timtim on 07/01/2018.
 */
class FriendTourFragment : SlideFragment() {

    lateinit var codeCallback: CodeInputCallback
    private var canMoveNext = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_tour_friend, container, false)
    }

    override fun backgroundColor(): Int {
        return R.color.colorPrimary
    }

    override fun buttonsColor(): Int {
        return R.color.colorAccent
    }

    override fun canMoveFurther(): Boolean {
        return /*canMoveNext*/ true;
    }

    override fun cantMoveFurtherErrorMessage(): String {
        return getString(R.string.error_message)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        btn_friend_submit.setOnClickListener {
            val code = et_friend_code.text.
                    toString()

            if(code.isNotBlank()) {
                codeCallback.onCodeTaken(code)
                canMoveNext = true
            }
            else {
                Toast.makeText(activity,
                        R.string.fill_friend_code,
                        Toast.LENGTH_SHORT).show()
            }
        }

        tv_have_no_friends.setOnClickListener {
            codeCallback.onFailedTakeCode()
            canMoveNext = true
        }*/
    }
}