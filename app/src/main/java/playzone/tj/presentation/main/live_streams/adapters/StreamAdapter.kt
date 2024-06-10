package playzone.tj.presentation.main.live_streams.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView
import playzone.tj.databinding.StreamItemLiveBinding
import playzone.tj.presentation.main.live_streams.model.StreamDTO

class StreamAdapter(private val streamList: List<StreamDTO>,private val listener:(videoId:String)->Unit)
    :RecyclerView.Adapter<StreamAdapter.StreamViewHolder>() {

    class StreamViewHolder(val binding:StreamItemLiveBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        val binding = StreamItemLiveBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StreamViewHolder(binding)
    }

    override fun getItemCount(): Int = streamList.size

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) {
        with(holder.binding){
            tvWatcherCount.text = streamList[position].countViewer.toString()
            setData(webView,streamList[position].link)
            imageLive.setOnClickListener {
                listener(streamList[position].link)
            }
        }
    }

    private fun setData(webView:WebView,videoId: String){
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
        webView.loadData(videoHtml, "text/html", "utf-8")
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
    }
}