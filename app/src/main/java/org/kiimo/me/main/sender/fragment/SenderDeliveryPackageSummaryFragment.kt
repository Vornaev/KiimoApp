package org.kiimo.me.main.sender.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentSenderPackageDeliverySummaryBinding
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModel
import org.kiimo.me.util.ChoosePaymentMethodDialog
import org.kiimo.me.util.PreferenceUtils
import java.text.SimpleDateFormat
import java.util.*

class SenderDeliveryPackageSummaryFragment : BaseMainFragment() {

    val viewModel: MainMenuViewModel by lazy { getNavigationActivity().viewModel }

    var paymentType: Int = 0

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

        paymentType = PreferenceUtils.getPaymentTypeForUser(requireContext())
        setText(paymentType)
        //    binding.toolbar.toolbar_title_text_view.text = getString(R.string.summuty_title)

        binding.senderPackageSummaryPin.pickUpText =
            viewModel.senderProperties.pickUpAddressPoint.address
        binding.senderPackageSummaryPin.dropOffText =
            viewModel.senderProperties.destinationAddressPoint.address

        val date = SimpleDateFormat("EEEE dd, yyyy 'at' HH:mm").format(Calendar.getInstance().time)
        binding.summaryCreatePackagePaymentDateCreated.text = date



        binding.summaryCreatePackageTextViewPaymentType.setOnClickListener {
            val dialg = ChoosePaymentMethodDialog(::onItemChoosed)
            dialg.show(childFragmentManager, "PaymentTypeChoose")
        }


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

    private fun onItemChoosed(item: Int) {
        setText(condition = item)
        paymentType = item

    }

    private fun setText(condition: Int) {
        val append = if (condition == 0) "CASH" else "CARD"
        binding.summaryCreatePackageTextViewPaymentType.text = "PAYMENT TYPE: ${append}"
        viewModel.senderProperties.prefeeredPaymentType = condition
    }
}