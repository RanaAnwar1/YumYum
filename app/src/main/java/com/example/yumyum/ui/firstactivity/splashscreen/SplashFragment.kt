package com.example.yumyum.ui.firstactivity.splashscreen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.navigation.fragment.findNavController
import com.example.yumyum.R
import com.example.yumyum.databinding.FragmentSplashBinding
import com.example.yumyum.util.Constant
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay


class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater,container,false)

        Handler(Looper.getMainLooper()).postDelayed({
            navigation() }, 2000)

        return binding.root
    }

    private fun navigation(){
        val sharedPref = activity?.getSharedPreferences(Constant.SHARED_PREF_KEY, MODE_PRIVATE)
        val loggedUser = sharedPref?.getBoolean(Constant.IS_USER_LOGGED,false)
        if(loggedUser == true ){
            findNavController().navigate(R.id.action_splashFragment_to_activity_meal_navigation)
        }else{
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }

    }


}