package org.kiimo.me.main.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.toolbar_back_button_title.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kiimo.me.R
import org.kiimo.me.app.BaseFragment
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentProfileBinding
import org.kiimo.me.main.components.DaggerMainComponent
import org.kiimo.me.main.modules.ProfileModule
import org.kiimo.me.main.viewmodels.ProfileViewModel
import org.kiimo.me.main.viewmodels.ProfileViewModelFactory
import org.kiimo.me.models.Profile
import org.kiimo.me.models.events.ProfilePhotoEvent
import org.kiimo.me.register.model.UserProfileFragmentUpdateRequest
import org.kiimo.me.register.model.UserRegisterDataRequest
import org.kiimo.me.util.JsonUtil
import org.kiimo.me.util.MediaManager
import org.kiimo.me.util.PreferenceUtils
import org.kiimo.me.util.RxBus
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ProfileFragment : BaseMainFragment() {

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    val profileString: String by lazy { PreferenceUtils.getUserProfile(requireContext()) }
    val isSender:Boolean by lazy { PreferenceUtils.getAccountTypeIsSender(requireActivity())}
    var profilePhoto = ""

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


        setToolbarTitle()
        setListeners()
        subscribeUi()

        mainDeliveryViewModel().photoProfileLiveData.observe(viewLifecycleOwner, Observer {
            mainDeliveryViewModel().updateUserProfilePhoto(it.imageUrl)
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDummyData()
        binding.profileLayout.deliveryLayout.visibility =   if(isSender) View.GONE else View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val width = 450
            val height = 480

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

    fun uploadBitmap(bitmap: Bitmap) {

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        var byteArray = stream.toByteArray()

        val fileRequest = RequestBody.create(
            MediaType.parse("image/*"),
            byteArray
        )

        val body = MultipartBody.Part.createFormData(
            "media",
            "${System.currentTimeMillis()}.jpg",
            fileRequest
        );

        mainDeliveryViewModel().uploadPhotoForUser(body)

    }

    private fun subscribeUi() {
        profileViewModel.profileImageViewBitmap.observe(viewLifecycleOwner, Observer {

           Glide.with(this).load(it).override(300,0).centerCrop().into(binding.profileLayout.profileImageView)
            uploadBitmap(it)
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

    override fun onStop() {
        saveProfileData()
        super.onStop()
    }

    fun saveProfileData() {

        var shouldUpdate = false

        val updateRequest = UserProfileFragmentUpdateRequest(
            profileCache.email,
            profileCache.name,
            profileCache.lastName,
            PreferenceUtils.getUserToken(requireContext()),
            user = UserRegisterDataRequest()
        )

        val emailField = binding.profileLayout.emailAddressEditText.text.toString()
        val firstanmeField = binding.profileLayout.nameEditText.text.toString()
        val lastNameField = binding.profileLayout.lastNameEditText.text.toString()

        if (isEmailValid(emailField) && !emailField.equals(updateRequest.email, false)) {
            updateRequest.email = emailField
            shouldUpdate = true
        }

        if (firstanmeField.isNotBlank() && !firstanmeField.equals(updateRequest.firstName)) {
            shouldUpdate = true
            updateRequest.firstName = firstanmeField
        }

        if (lastNameField.isNotBlank() && !lastNameField.equals(updateRequest.lastName)) {
            shouldUpdate = true
            updateRequest.lastName = lastNameField
        }

        if (shouldUpdate) {
            getNavigationActivity().viewModel.updateUserNameProfile(
                updateRequest
            )
        }
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    var profileCache = Profile()

    private fun setDummyData() {

        if (profileString.isNotEmpty()) {
            profileCache = Profile().loadFromCache(
                JsonUtil.loadModelFromJson(profileString),
                PreferenceUtils.getUserPhoneNumber(requireContext())
            )

            if (profileCache.photoUserUrl.isNotEmpty()) {
                Glide.with(requireContext()).load(profileCache.photoUserUrl).override(300, 0)
                    .centerCrop()
                    .into(binding.profileLayout.profileImageView)
            }
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