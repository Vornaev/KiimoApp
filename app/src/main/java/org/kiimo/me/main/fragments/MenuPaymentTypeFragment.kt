package org.kiimo.me.main.fragments

import android.graphics.Color.red
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_menu_payment_type.view.*
import kotlinx.android.synthetic.main.toolbar_back_button_title.view.*
import org.kiimo.me.R
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentMenuPaymentTypeBinding
import org.kiimo.me.main.menu.model.CreditCardModel
import org.kiimo.me.main.menu.model.CreditCardSaveRequest
import org.kiimo.me.models.delivery.BaseDeliveryResponse
import org.kiimo.me.models.payment.PreferredPaymentUser
import org.kiimo.me.util.DialogUtils
import org.kiimo.me.util.JsonUtil
import org.kiimo.me.util.PreferenceUtils
import retrofit2.HttpException

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

        attahListeners()

        when (paymentType) {
            0 -> onCashTypeClicked()
            1 -> onCreditCardClicked().also {
                cardDetails?.let {
                    fillCardDetails(it)
                }
            }
        }

//        mainDeliveryViewModel().preferredPayLiveData.observe(viewLifecycleOwner, Observer {
//            DialogUtils.showSuccessMessage(requireActivity(), "OK")
//        })


        mainDeliveryViewModel().creditCardLiveData.observe(viewLifecycleOwner, Observer {
            if (it.success) {
                mainDeliveryViewModel().savePreferredPaymentType(PreferredPaymentUser("CARD"))
                onCardSaved()
            } else {
                DialogUtils.showErrorMessage(requireActivity(), it.message)
            }
        })

        mainDeliveryViewModel().cardExceptionLiveData.observe(viewLifecycleOwner, Observer {

            val stringError = (it as HttpException).response().errorBody()?.string()
            if (stringError.isNullOrBlank()) {
                DialogUtils.showErrorMessage(
                    requireActivity(), "Грешка",
                    "Невалидна картичка"
                )
            } else {
                val res = JsonUtil.loadModelFromJson<BaseDeliveryResponse>(stringError)
                DialogUtils.showErrorMessage(requireActivity(), "Грешка", res.message)
            }
        })
    }

    private fun attahListeners() {
        atachListenerToField(binding.paymentCardholderUsername, ::isValidName)
        atachListenerToField(binding.paymentCardholderNumber, ::isValidCardNumber)
        atachListenerToField(binding.paymentCardDateValue, ::isValidMonth)
        atachListenerToField(binding.paymentCardYearValue, ::isValidYear)
        atachListenerToField(binding.paymentCardCCValue, ::isValidCCV)


        binding.fragmentPaymentCardTxt.setOnClickListener {
            onCreditCardClicked()
        }

        binding.fragmentPaymentCashTxt.setOnClickListener {
            onCashTypeClicked()
        }

        binding.paymentSaveButton.setOnClickListener {
            onSaveClicked()
        }

        binding.toolbar.arrow_back_image_view.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    fun isValidName(): Boolean {
        val valid =
            binding.paymentCardholderUsername.text.isNotBlank() && binding.paymentCardholderUsername.text.length > 4
        binding.paymentCardholderUsername.displayValidationStatus(valid)

        return valid
    }

    fun isValidCardNumber(): Boolean {
        val numberText = binding.paymentCardholderNumber
        val valid = numberText.text.isNotBlank() && numberText.text.length > 12
        binding.paymentCardholderNumber.displayValidationStatus(valid)

        return valid
    }

    fun isValidCCV(): Boolean {
        val valid =
            binding.paymentCardCCValue.text.isNotBlank() && binding.paymentCardCCValue.text.length == 3
        binding.paymentCardCCValue.displayValidationStatus(valid)

        return valid
    }

    fun isValidYear(): Boolean {
        val valid = binding.paymentCardYearValue.text.isNotBlank() &&
                binding.paymentCardYearValue.text.toString().toInt() >= 20 &&
                binding.paymentCardYearValue.text.toString().toInt() < 30

        binding.paymentCardYearValue.displayValidationStatus(valid)
        return valid
    }

    fun isValidMonth(): Boolean {
        val valid =
            binding.paymentCardDateValue.text.isNotBlank() &&
                    binding.paymentCardDateValue.text.length == 2 &&
                    binding.paymentCardDateValue.text.toString().toInt() >= 1 &&
                    binding.paymentCardDateValue.text.toString().toInt() <= 12
        binding.paymentCardDateValue.displayValidationStatus(valid)
        return valid
    }

    fun validateInput(): Boolean {
        val validCardNum = isValidCardNumber()
        val validName = isValidName()
        val validMnnth = isValidMonth()
        val validYear = isValidYear()
        val validCCv = isValidCCV()

        return validCardNum && validName && validMnnth && validYear && validCCv
    }

    private fun onSaveClicked() {

        if (selectedPaymentType == PAYMENT_TYPE.CARD) {
            if (validateInput()) {
                mainDeliveryViewModel().saveCreditCardFields(getCardForService())
            }
        } else {
            mainDeliveryViewModel().savePreferredPaymentType(PreferredPaymentUser("CASH"))
            onCashSaved()
        }
    }

    private fun onCardSaved() {
        PreferenceUtils.savePaymentTypeForUser(requireContext(), selectedPaymentType)
        if (selectedPaymentType == 1) {
            PreferenceUtils.saveCreditCard(requireContext(), getCardFromView())
        }
    }

    private fun onCashSaved() {
        PreferenceUtils.savePaymentTypeForUser(requireContext(), selectedPaymentType)
    }

    fun getCardFromView(): CreditCardModel {
        return CreditCardModel(
            binding.paymentCardholderUsername.text.toString(),
            binding.paymentCardholderNumber.text.toString(),
            binding.paymentCardDateValue.text.toString(),
            binding.paymentCardYearValue.text.toString(),
            binding.paymentCardCCValue.text.toString()
        )
    }

    fun getCardForService(): CreditCardSaveRequest {
        return CreditCardSaveRequest(
            binding.paymentCardholderNumber.text.toString(),
            binding.paymentCardDateValue.text.toString(),
            binding.paymentCardYearValue.text.toString(),
            binding.paymentCardCCValue.text.toString()
        )
    }

    fun fillCardDetails(cardModel: CreditCardModel) {
        binding.paymentCardholderUsername.setText(cardModel.cardholderName)
        binding.paymentCardholderNumber.setText(cardModel.cardNumber)
        binding.paymentCardDateValue.setText(cardModel.expMonth)
        binding.paymentCardYearValue.setText(cardModel.expYear)
        binding.paymentCardCCValue.setText(cardModel.ccvCode)
    }

    fun onCreditCardClicked() {
        binding.paymentCardDetailsLayout.visibility = View.VISIBLE
        binding.paymentImageViewCardChecked.setImageResource(R.drawable.ic_check_circle_outlined)
        binding.paymentCashImageViewChecked.setImageResource(R.drawable.ic_radio_button_unchecked_black)
        selectedPaymentType = PAYMENT_TYPE.CARD
    }


    fun onCashTypeClicked() {
        binding.paymentCardDetailsLayout.visibility = View.GONE
        binding.paymentCashImageViewChecked.setImageResource(R.drawable.ic_check_circle_outlined)
        binding.paymentImageViewCardChecked.setImageResource(R.drawable.ic_radio_button_unchecked_black)
        selectedPaymentType = PAYMENT_TYPE.CASH

    }

    protected fun atachListenerToField(field: EditText, listener: () -> Boolean) {
        field.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    listener()
                }

                override fun beforeTextChanged(
                        s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            }
        )
    }

    protected fun EditText.displayValidationStatus(stateValid: Boolean) {
        this.setBackgroundResource(if (stateValid) R.drawable.edit_text_cornered_background_gray else R.drawable.shape_validation_error)
    }

    object PAYMENT_TYPE {
        val CASH = 0
        val CARD = 1
    }
}