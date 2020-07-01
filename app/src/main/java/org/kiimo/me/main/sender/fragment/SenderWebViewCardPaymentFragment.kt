package org.kiimo.me.main.sender.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentSenderWebviewPaymentBinding

class SenderWebViewCardPaymentFragment : BaseMainFragment() {

    lateinit var binding: FragmentSenderWebviewPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSenderWebviewPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val amount = mainDeliveryViewModel().senderProperties.deliveryPrice.getBrutoAmount()
        //binding.webViewPayment.webViewClient = WebViewClient()
        binding.webViewPayment.webViewClient = MyWebViewClient()
        binding.webViewPayment.settings.javaScriptEnabled = true
        binding.webViewPayment.loadUrl("https://kiimo.xyz/?amount=$amount")
        binding.webViewPayment.addJavascriptInterface(
            WebAppInterface(requireContext()),
            "Android"
        )

    }

    inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (Uri.parse(url).host == "www.example.com") {
                // This is my web site, so do not override; let my WebView load the page
                return false
            }

            if (url.equals("https://kiimo.xyz/3DPayResultPage.aspx", true)) {
              //  mainDeliveryViewModel().payForFackage()
                val p = 10000
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs11
            return false
        }
    }

    inner class WebAppInterface(private val mContext: Context) {

        /** Show a toast from the web page  */
        @JavascriptInterface
        fun OnSubmit() {
            Toast.makeText(mContext, "toasttest", Toast.LENGTH_SHORT).show()
        }

        fun onSubmit() {
            Toast.makeText(mContext, "toasttest", Toast.LENGTH_SHORT).show()
        }

        fun submit() {
            Toast.makeText(mContext, "toasttest", Toast.LENGTH_SHORT).show()
        }

        fun WebForm_OnSubmit() {
            Toast.makeText(mContext, "toasttest", Toast.LENGTH_SHORT).show()
        }

        fun WebForm_DoPostBackWithOptions() {
            Toast.makeText(mContext, "toasttest", Toast.LENGTH_SHORT).show()
        }

        fun __doPostBack(eventTarget: Any, eventArgs: Any) {
            Toast.makeText(mContext, "toasttest", Toast.LENGTH_SHORT).show()
        }

        fun payResult(status: Any?) {
            Toast.makeText(mContext, "toasttest", Toast.LENGTH_SHORT).show()
        }

        fun payResult() {
            Toast.makeText(mContext, "toasttest", Toast.LENGTH_SHORT).show()
        }


    }

}