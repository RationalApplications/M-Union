package xyz.ratapp.munion.ui.fragments.tour

import agency.tango.materialintroscreen.SlideFragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.ratapp.munion.R
import xyz.ratapp.munion.extensions.inflate

/**
 * Created by timtim on 07/01/2018.
 */
class MunionTourFragment : SlideFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_tour_munion, false)
    }

    override fun backgroundColor(): Int {
        return R.color.colorPrimary
    }

    override fun buttonsColor(): Int {
        return R.color.colorAccent
    }

    override fun canMoveFurther(): Boolean {
        return true
    }

    override fun cantMoveFurtherErrorMessage(): String {
        return getString(R.string.error_message)
    }

}