package org.kiimo.me.main.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import org.kiimo.me.app.BaseFragment
import org.kiimo.me.databinding.FragmentWebViewBinding
import org.kiimo.me.util.AppConstants

class WebViewFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWebViewBinding.inflate(inflater, container, false)

        binding.webView.webViewClient = WebViewClient()
        @SuppressLint("SetJavaScriptEnabled")
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(arguments?.getString(AppConstants.INTENT_EXTRA_WEB_VIEW_URL))

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String): WebViewFragment = WebViewFragment().apply {
            arguments = Bundle().apply {
                putString(AppConstants.INTENT_EXTRA_WEB_VIEW_URL, url)
            }
        }
    }
}