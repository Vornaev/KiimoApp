package org.kiimo.me.main.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.kiimo.me.main.fragments.ProfileFragment
import org.kiimo.me.main.repositories.ProfileRepository

/**
 * The ViewModel for [ProfileFragment].
 */
class ProfileViewModel internal constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val profileImageViewBitmap = MutableLiveData<Bitmap>()

    val profileImageViewWidth = MutableLiveData<Int>()
    val profileImageViewHeight = MutableLiveData<Int>()

    fun bitmapChanged(bitmap: Bitmap) {
        profileImageViewBitmap.value = bitmap
    }

    fun profileImageViewWidthHeightChanged(
        profileImageViewWidth: Int,
        profileImageViewHeight: Int
    ) {
        this.profileImageViewWidth.value = profileImageViewWidth
        this.profileImageViewHeight.value = profileImageViewHeight
    }
}