package org.kiimo.me.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.toolbar_back_button_title.view.*
import org.kiimo.me.R
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentMenuPaymentTypeBinding
import org.kiimo.me.main.menu.model.CreditCardModel
import org.kiimo.me.models.payment.PreferredPaymentUser
import org.kiimo.me.util.PreferenceUtils

class MenuPaymentTypeFragment : BaseMainFragment() {


    lateinit var binding: FragmentMenuPaymentTypeBinding

    private val paymentType: Int by lazy { PreferenceUtils.getPaymentTypeForUser(requireContext()) }
    private val cardDetails: CreditCardModel? by lazy {
        PreferenceUtils.getCreditCardDetails(
            requireContext()
        )
    }

    var selectedPaymentType = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMenuPaymentTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.toolbar_title_text_view.text = getString(R.string.payment_toolber_title)

        binding.fragmentPaymentCardTxt.setOnClickListener {
            onCreditCardClicked()
        }

        binding.fragmentPaymentCashTxt.setOnClickListener {
            onCashTypeClicked()
        }

        binding.paymentSaveButton.setOnClickListener {
            onSaveClicked()

            mainDeliveryViewModel().savePreferredPaymentType(PreferredPaymentUser("CASH"))
        }

        when (paymentType) {
            0 -> onCashTypeClicked()
            1 -> onCreditCardClicked().also {
                cardDetails?.let {
                    fillCardDetails(it)
                }
            }
        }

    }

    private fun onSaveClicked() {

        PreferenceUtils.savePaymentTypeForUser(requireContext(), selectedPaymentType)
        if (selectedPaymentType == 1) {
            PreferenceUtils.saveCreditCard(requireContext(), getCardFromView())
        }
    }

    fun getCardFromView(): CreditCardModel {
        return CreditCardModel(
            binding.paymentCardholderUsername.text.toString(),
            binding.paymentCardholderNumber.text.toString(),
            binding.paymentCardDateValue.text.toString(),
            binding.paymentCardCCValue.text.toString()
        )
    }

    fun fillCardDetails(cardModel: CreditCardModel) {
        binding.paymentCardholderUsername.setText(cardModel.cardholderName)
        binding.paymentCardholderNumber.setText(cardModel.cardNumber)
        binding.paymentCardDateValue.setText(cardModel.expiryDate)
        binding.paymentCardCCValue.setText(cardModel.ccvCode)
    }

    fun onCreditCardClicked() {
        binding.paymentCardDetailsLayout.visibility = View.VISIBLE
        binding.paymentImageViewCardChecked.setImageResource(R.drawable.ic_check_circle_outlined)
        binding.paymentCashImageViewChecked.setImageResource(R.drawable.ic_radio_button_unchecked_black)
        selectedPaymentType = 1
    }


    fun onCashTypeClicked() {
        binding.paymentCardDetailsLayout.visibility = View.GONE
        binding.paymentCashImageViewChecked.setImageResource(R.drawable.ic_check_circle_outlined)
        binding.paymentImageViewCardChecked.setImageResource(R.drawable.ic_radio_button_unchecked_black)
        selectedPaymentType = 0

    }
}