package org.kiimo.me.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import org.kiimo.me.util.IMediaManagerImages

abstract class BaseFragment : Fragment(), IMediaManagerImages {

    companion object {
        const val MY_LOCATION_REQUEST_CODE = 987
        const val AUTOCOMPLETE_REQUEST_CODE = 988
    }


    override fun getMediaContext(): Context {
        return requireContext()
    }

    override fun startMediaActivity(createChooser: Intent?, requestImagePick: Int) {
        this.startActivityForResult(createChooser, requestImagePick)
    }

    override fun getMediaActivity(): Activity {
        return this.requireActivity()
    }
}

