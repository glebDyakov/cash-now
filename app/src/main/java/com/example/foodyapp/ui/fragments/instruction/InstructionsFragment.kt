package cash.now.cshnw.ui.fragments.instruction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import cash.now.cshnw.R
import cash.now.cshnw.models.Result
import cash.now.cshnw.utils.Constants

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