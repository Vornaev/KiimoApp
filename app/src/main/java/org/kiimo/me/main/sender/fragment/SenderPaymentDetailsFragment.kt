package org.kiimo.me.main.sender.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.toolbar_back_button_title.view.*
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentSenderPaymentDetailsBinding
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModel

class SenderPaymentDetailsFragment : BaseMainFragment() {

    val viewModel: MainMenuViewModel by lazy { getNavigationActivity().viewModel }
    lateinit var binding: FragmentSenderPaymentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSenderPaymentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.toolbar_title_text_view.text = "Payment Details"
        binding.pickUpLocation.text = viewModel.senderProperties.pickUpAddressPoint.address
        binding.priceDeliveryPackage.text = viewModel.senderProperties.caluclatedPrice
        binding.payPackgeButtonConfirm.setOnClickListener {
            viewModel.payForFackage()
        }

    }
}