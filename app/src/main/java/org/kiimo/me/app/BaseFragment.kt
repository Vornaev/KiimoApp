package org.kiimo.me.app

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    companion object {
        const val MY_LOCATION_REQUEST_CODE = 987
        const val AUTOCOMPLETE_REQUEST_CODE = 988
    }

}

