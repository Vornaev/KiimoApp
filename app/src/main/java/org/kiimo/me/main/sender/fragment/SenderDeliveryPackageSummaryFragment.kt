package org.kiimo.me.main.sender.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.toolbar_back_button_title.view.*
import org.kiimo.me.R
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentSenderPackageDeliverySummaryBinding
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModel
import org.kiimo.me.main.sender.dialog.SendItemDescriptionDialog
import org.kiimo.me.main.sender.model.request.CreateDeliveryRequest
import org.kiimo.me.main.sender.model.request.Destination
import org.kiimo.me.main.sender.model.request.Origin
import org.kiimo.me.main.sender.model.request.Packages
import org.kiimo.me.models.delivery.CalculateDeliveryRequest
import org.kiimo.me.util.AppConstants
import org.kiimo.me.util.PreferenceUtils
import java.text.SimpleDateFormat
import java.util.*

class SenderDeliveryPackageSummaryFragment : BaseMainFragment() {

    val viewModel: MainMenuViewModel by lazy { getNavigationActivity().viewModel }

    lateinit var binding: FragmentSenderPackageDeliverySummaryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSenderPackageDeliverySummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.arrowBackImageView.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

    //    binding.toolbar.toolbar_title_text_view.text = getString(R.string.summuty_title)

        binding.senderPackageSummaryPin.pickUpText =
            viewModel.senderProperties.pickUpAddressPoint.address
        binding.senderPackageSummaryPin.dropOffText =
            viewModel.senderProperties.destinationAddressPoint.address

        val date = SimpleDateFormat("EEEE dd, yyyy 'at' HH:mm").format(Calendar.getInstance().time)
        binding.summaryCreatePackagePaymentDateCreated.text = date
        val paymentType = PreferenceUtils.getPaymentTypeForUser(requireContext())

        val append = if (paymentType == 0) "CASH" else "CARD"
        binding.summaryCreatePackageTextViewPaymentType.text = "PAYMENT TYPE: ${append}"


        viewModel.calculateDelivery(
            deliveryRequest = viewModel.senderProperties.getCalculateRequest()
        )

        viewModel.calculateDeliveryLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                binding.priceDeliveryPackage.text = "${it.getBruttoPrice()} MKD"
                viewModel.senderProperties.caluclatedPrice = "${it.getBruttoPrice()} MKD"

            })


        binding.createPackgeButtonConfirm.setOnClickListener {

            viewModel.createPackageForDelivery(
                request = viewModel.senderProperties.getCreatePackageRequest()
            )
        }

    }
}