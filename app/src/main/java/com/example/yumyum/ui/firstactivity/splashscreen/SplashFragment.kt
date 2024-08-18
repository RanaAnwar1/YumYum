package com.example.yumyum.ui.firstactivity.splashscreen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    fun navigation(){
//        val sharedPref = activity?.getSharedPreferences(Constant.IS_USER_LOGGED, MODE_PRIVATE)
//        val loggedUser = sharedPref?.getBoolean(Constant.IS_USER_LOGGED,false)
//        if(loggedUser == true ){
//            TODO("move to second activity")
//        }else{
//            findNavController().apply {
//                popBackStack(R.id.splashFragment, true)
//                navigate(R.id.action_splashFragment_to_loginFragment)
//            }
//        }
        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }


}