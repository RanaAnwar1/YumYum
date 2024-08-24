package com.example.yumyum.ui.secondactivity.mealdetailsscreen.instructionsfragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.yumyum.R
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.databinding.FragmentInstructionsBinding
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory

class InstructionsFragment : Fragment() {
    private lateinit var videoPreview: ImageView
    private lateinit var playButton: ImageButton
    private lateinit var instructionDescription: TextView
    private lateinit var videoDialog: Dialog
    private val instructionsViewModel: MealViewModel by viewModels({requireParentFragment()}) {
        MealViewModelFactory(
            MealsRepositoryImpl(RetrofitClient, ApplicationDatabase.getInstance(requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_instructions, container, false)

        videoPreview = view.findViewById(R.id.videoPreview)
        playButton = view.findViewById(R.id.playButton)
        instructionDescription = view.findViewById(R.id.instructionDescription)
        videoDialog = Dialog(requireContext(), android.R.style.Theme_Dialog)
        videoDialog.setContentView(R.layout.dialog_video_player)

        instructionsViewModel.mealDetails.observe(viewLifecycleOwner) { meal ->
            val youtubeLink = meal.meals[0].strYoutube ?: ""
            Log.d("InstructionsFragment", "instructionViewModel youtube: $youtubeLink")
            val embedLink = convertToEmbedLink(youtubeLink)
            val thumbnailUrl = getYoutubeThumbnailUrl(youtubeLink)

            Glide.with(this)
                .load(thumbnailUrl)
                .placeholder(R.drawable.empty)
                .into(videoPreview)

            playButton.setOnClickListener {
                showVideoDialog(embedLink)
            }
            instructionDescription.text = meal.meals[0].strInstructions
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

