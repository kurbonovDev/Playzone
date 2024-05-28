package playzone.tj.ui.main.home.event_detail

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import playzone.tj.databinding.FragmentYouTubeBinding


class YouTubeFragment : Fragment() {

    private lateinit var binding: FragmentYouTubeBinding
    private val args: YouTubeFragmentArgs by navArgs()
    private var videoId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYouTubeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoId = args.videoUrl
        val url = "https://www.youtube.com/embed/$videoId"
        val videoHtml = """
    <html>
    <head>
        <style>
            body, html {
                margin: 0;
                padding: 0;
                height: 100%;
                overflow: hidden;
            }
            .custom-iframe {
                border: none;
                width: 100%;
                height: 100%;
            }
        </style>
    </head>
    <body>
        <iframe class="custom-iframe" src="$url" title="YouTube video player" 
        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
        allowfullscreen></iframe>
    </body>
    </html>
""".trimIndent()
        binding.webView.loadData(videoHtml,"text/html","utf-8")
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()

    }




}