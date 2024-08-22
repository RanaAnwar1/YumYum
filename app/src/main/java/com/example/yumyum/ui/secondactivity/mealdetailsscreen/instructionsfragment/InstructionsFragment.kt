package com.example.yumyum.ui.secondactivity.mealdetailsscreen.instructionsfragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.yumyum.R

class InstructionsFragment : Fragment() {

    private lateinit var videoDialog: Dialog
    private lateinit var video:ImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_instructions, container, false)

        val youtubeLink = "https://www.youtube.com/watch?v=1IszT_guI08"
        val embedLink = convertToEmbedLink(youtubeLink)
        val thumbnailUrl = getYoutubeThumbnailUrl(youtubeLink)
        videoDialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        videoDialog.setContentView(R.layout.dialog_video_player)

        val videoPreview: ImageView = view.findViewById(R.id.videoPreview)
        video = view.findViewById(R.id.playButton)
        Glide.with(this)
            .load(thumbnailUrl)
            .placeholder(R.drawable.empty) // Optional placeholder
            .into(videoPreview)

        video.setOnClickListener {
            showVideoDialog(embedLink)
        }

        return view
    }

    private fun convertToEmbedLink(youtubeLink: String): String {
        val url = """${youtubeLink}"""
        return url.replace("watch?v=", "embed/")
    }
    private fun getYoutubeThumbnailUrl(youtubeLink: String): String {
        val videoId = youtubeLink.split("v=")[1]
        return "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
    }

    private fun showVideoDialog(embedLink: String) {
        val webView: WebView = videoDialog.findViewById(R.id.videoWebView)
        val btnClose: ImageButton = videoDialog.findViewById(R.id.btnClose)

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(embedLink)

        btnClose.setOnClickListener {
            videoDialog.dismiss()
        }

        videoDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (videoDialog.isShowing) {
            videoDialog.dismiss()
        }
    }
}

