package com.example.foodyapp.ui.fragments.instruction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.foodyapp.R
import com.example.foodyapp.models.Result
import com.example.foodyapp.utils.Constants

class InstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_instructions, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPES_RESULT)

        val webview = view.findViewById<WebView>(R.id.instruction_webView)
        webview.webViewClient = object : WebViewClient() {}
        val webViewURL = myBundle!!.sourceUrl
        if (webViewURL != null) {
            webview.loadUrl(webViewURL)
        }



        return view
    }


}