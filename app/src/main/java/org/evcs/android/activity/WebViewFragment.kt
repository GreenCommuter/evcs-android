package org.evcs.android.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import org.evcs.android.R
import org.evcs.android.databinding.WebViewBinding
import org.evcs.android.util.Extras
import org.evcs.android.util.SpinnerUtils


class WebViewFragment : BaseActivity2() {

    private lateinit var mProgressDialog: Dialog
    private lateinit var mSubtitle: TextView
    private lateinit var mTitle: TextView
    private lateinit var mWebView: WebView
    private lateinit var mUrl: String

    companion object {
        fun buildIntent(context: Context?, title: String, url: String, subtitle: String? = null): Intent {
            val intent = Intent(context, WebViewFragment::class.java)
            intent.putExtra(Extras.WebView.TITLE, title)
            intent.putExtra(Extras.WebView.URL, url)
            intent.putExtra(Extras.WebView.SUBTITLE, subtitle)
            return intent
        }
    }

    override fun inflate(layoutInflater: LayoutInflater): View {
        val binding = WebViewBinding.inflate(layoutInflater)
        mWebView = binding.webViewView
        mTitle = binding.webViewTitle
        mSubtitle = binding.webViewSubtitle
        binding.webViewClose.setOnClickListener { finish() }
        mProgressDialog = SpinnerUtils.getNewProgressDialog(this, R.layout.spinner_layout)
        mProgressDialog.show()
        return binding.root
    }

    override fun init() {
        mTitle.text = intent.getStringExtra(Extras.WebView.TITLE)
        mUrl = intent.getStringExtra(Extras.WebView.URL)!!
        mSubtitle.text = intent.getStringExtra(Extras.WebView.SUBTITLE) ?: mUrl

        mWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                mProgressDialog.dismiss()
                super.onPageFinished(view, url)
            }
        }

        mWebView.loadUrl(mUrl)
        mWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.domStorageEnabled = true
    }

}