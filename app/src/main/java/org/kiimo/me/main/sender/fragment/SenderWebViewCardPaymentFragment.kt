package org.kiimo.me.main.sender.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentSenderWebviewPaymentBinding
import org.kiimo.me.util.DialogUtils

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

        binding.webViewPayment.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                if (consoleMessage?.message()?.startsWith("Payment succesful") == true) {
                    val p = "ahaha"
                }

                if (consoleMessage?.message()?.startsWith("Payment succesful") == true) {
                    DialogUtils.showSuccessMessage(
                        requireActivity(), "Payment Success", onClicked = {
                            mainDeliveryViewModel().payForFackage()
                        })
                }

                else if( consoleMessage?.message()?.startsWith("3D authentication and payment unsuccesful.",false)==true){
                    DialogUtils.showDialogWithTitleAndMessage(
                        requireActivity(),"Error", "Payment error check your input that is valid and try again",
                        onClicked = {
                            requireActivity().supportFragmentManager.popBackStack()
                        })
                }
                return super.onConsoleMessage(consoleMessage)
            }
        }

        val amount = mainDeliveryViewModel().senderProperties.deliveryPrice.getBrutoAmount()
        //binding.webViewPayment.webViewClient = WebViewClient()
        binding.webViewPayment.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (Uri.parse(url).host == "www.example.com") {
                    // This is my web site, so do not override; let my WebView load the page
                    return false
                }

                if (url?.startsWith("https://kiimo.xyz/3DPayResultPage.aspx", true) == true) {
                    //  mainDeliveryViewModel().payForFackage()
                    val p = 10000
                }

                return false
            }


            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {

                if (request?.url?.toString()
                        ?.startsWith("https://kiimo.xyz/3DPayResultPage.aspx") == true
                ) {
                    val z = 100
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        binding.webViewPayment.settings.javaScriptEnabled = true
//        binding.webViewPayment.addJavascriptInterface(
//            WebAppInterface(requireContext()),
//            "pay_result_page"
//        )
        binding.webViewPayment.loadUrl("https://kiimo.xyz/?amount=$amount")

    }


    inner class WebAppInterface(private val mContext: Context) {

        /** Show a toast from the web page  */
        @JavascriptInterface
        fun payResult(status: Any) {

        }

        @JavascriptInterface
        fun payResult(status: String) {

        }


        @JavascriptInterface
        fun payResult() {

        }


    }

}