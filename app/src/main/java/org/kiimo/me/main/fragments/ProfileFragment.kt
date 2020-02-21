package org.kiimo.me.main.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.toolbar_back_button_title.view.*
import org.kiimo.me.R
import org.kiimo.me.app.BaseFragment
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentProfileBinding
import org.kiimo.me.main.components.DaggerMainComponent
import org.kiimo.me.main.modules.ProfileModule
import org.kiimo.me.main.viewmodels.ProfileViewModel
import org.kiimo.me.main.viewmodels.ProfileViewModelFactory
import org.kiimo.me.models.Profile
import org.kiimo.me.util.JsonUtil
import org.kiimo.me.util.MediaManager
import org.kiimo.me.util.PreferenceUtils
import javax.inject.Inject

class ProfileFragment : BaseMainFragment() {

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val profileViewModel: ProfileViewModel by viewModels {
        profileViewModelFactory
    }

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        DaggerMainComponent.builder().profileModule(ProfileModule()).build().inject(this)

        setDummyData()
        setToolbarTitle()
        setListeners()
        subscribeUi()

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val width = profileViewModel.profileImageViewWidth.value
            val height = profileViewModel.profileImageViewHeight.value
            if (width != null && height != null && width > 0 && height > 0) {
                if (requestCode == MediaManager.REQUEST_IMAGE_CAPTURE) {
                    MediaManager.getBitmap(
                        width.toFloat(), height.toFloat()
                    )?.apply {
                        profileViewModel.bitmapChanged(this)
                    }
                } else if (requestCode == MediaManager.REQUEST_IMAGE_PICK) {
                    val uri = data?.data
                    MediaManager.getBitmapGallery(
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, uri),
                        width, height
                    )?.apply {
                        profileViewModel.bitmapChanged(this)
                    }
                }
            }
        }
    }

    private fun subscribeUi() {
        profileViewModel.profileImageViewBitmap.observe(viewLifecycleOwner, Observer {
            binding.profileLayout.profileImageView.setImageBitmap(it)
        })
    }

    private fun getUnderlineSpannableString(): SpannableString {
        val underlineData = getString(R.string.logout)
        val content = SpannableString(underlineData)
        content.setSpan(UnderlineSpan(), 0, underlineData.length, 0)
        return content
    }

    private fun setToolbarTitle() {
        binding.toolbar.toolbar_title_text_view.text = getString(R.string.my_profile)
    }

    private fun setListeners() {
        binding.profileLayout.profileImageView.setOnClickListener {
            profileViewModel.profileImageViewWidthHeightChanged(
                binding.profileLayout.profileImageView.width,
                binding.profileLayout.profileImageView.height
            )
            MediaManager.showMediaOptionsDialog(this)
        }

        binding.toolbar?.arrow_back_image_view?.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.profileLayout.logout.setOnClickListener {
            getNavigationActivity()?.logout()
        }
    }

    private fun setDummyData() {

        val profileString = PreferenceUtils.getUserProfile(requireContext())
        var profileCache = Profile()

        if (profileString.isNotEmpty()) {
            profileCache = Profile().loadFromCache(
                JsonUtil.loadModelFromJson(profileString),
                PreferenceUtils.getUserPhoneNumber(requireContext())
            )
        }

        binding.profileLayout.profile = profileCache
        binding.profileLayout.joinedText = ""
        binding.profileLayout.underlineText = getUnderlineSpannableString()
    }

    companion object {
        @JvmStatic
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}