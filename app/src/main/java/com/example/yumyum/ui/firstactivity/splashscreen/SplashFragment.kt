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
    ): View {
        if(Constant.REQUESTED_SIGN_OUT)
            editSharedPref()

        binding = FragmentSplashBinding.inflate(layoutInflater,container,false)

        Handler(Looper.getMainLooper()).postDelayed({
            navigation() }, 2000)

        return binding.root
    }

    private fun navigation(){
        val sharedPref = activity?.getSharedPreferences(Constant.SHARED_PREF_KEY, MODE_PRIVATE)
        val loggedUser = sharedPref?.getBoolean(Constant.IS_USER_LOGGED,false)
        Constant.USER_NAME = sharedPref?.getString(Constant.SAVED_USER_NAME_KEY,"non").toString()
        if(loggedUser == true ){
            navigate(R.id.action_splashFragment_to_mealActivity)
        }else{
            navigate(R.id.action_splashFragment_to_loginFragment)
        }

    }

    private fun editSharedPref(){
        val sharedPref =
            activity?.getSharedPreferences(Constant.SHARED_PREF_KEY, MODE_PRIVATE)
        sharedPref?.edit()?.apply {
            putBoolean(Constant.IS_USER_LOGGED, false)
            putString(Constant.SAVED_USER_NAME_KEY,"")
        }?.apply()
        Constant.USER_NAME = ""
    }

    private fun navigate(destination:Int){
        findNavController().navigate(destination)
    }


}