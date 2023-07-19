package org.evcs.android.features.shared

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.base.core.presenter.BasePresenter
import org.evcs.android.R
import org.evcs.android.databinding.WebViewBinding
import org.evcs.android.ui.fragment.ErrorFragment
import org.evcs.android.util.Extras


class WebViewFragment : ErrorFragment<BasePresenter<*>>() {

    private lateinit var mSubtitle: TextView
    private lateinit var mTitle: TextView
    private lateinit var mWebView: WebView
    private lateinit var mUrl: String

    companion object {
        fun newInstance(url: String?): WebViewFragment {
            val args = Bundle()
            args.putSerializable(Extras.WebView.URL, url)
            val fragment = WebViewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layout(): Int {
        return R.layout.web_view
    }

    override fun setUi(v: View) {
        super.setUi(v)
        val binding = WebViewBinding.bind(v)
        mWebView = binding.webViewView
        mTitle = binding.webViewTitle
        mSubtitle = binding.webViewSubtitle
        binding.webViewClose.setOnClickListener { requireActivity().onBackPressed() }
    }

    override fun createPresenter(): BasePresenter<*> {
        return BasePresenter<Any?>(this)
    }

    override fun init() {
        mTitle.text = requireArguments().getString(Extras.WebView.TITLE)
        mSubtitle.text = requireArguments().getString(Extras.WebView.URL)
        mUrl = requireArguments().getString(Extras.WebView.URL)!!

        mWebView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                showProgressDialog()
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                hideProgressDialog()
                super.onPageFinished(view, url)
            }
        }

        mWebView.loadUrl(mUrl)
        mWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
    }

}