package org.kiimo.me.main.sender.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentSenderWebviewPaymentBinding

class SenderWebViewCardPaymentFragment : BaseMainFragment() {

    lateinit var binding : FragmentSenderWebviewPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.webViewPayment.loadUrl("https://kiimo.xyz/?amount=44")


    }
}