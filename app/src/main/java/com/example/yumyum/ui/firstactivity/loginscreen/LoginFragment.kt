package com.example.yumyum.ui.firstactivity.loginscreen

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.yumyum.R
import com.example.yumyum.data.repository.UserRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.databinding.FragmentLoginBinding
import com.example.yumyum.ui.firstactivity.UserViewModel
import com.example.yumyum.ui.firstactivity.UserViewModelFactory
import com.example.yumyum.util.Constant
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepositoryImpl(ApplicationDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.LoginBt.setOnClickListener {
            val username = binding.loginUsernameTv.editText?.text.toString().trim()
            val password = binding.loginPasswordTv.editText?.text.toString().trim()
            Log.d("viewModel_logging","btn click")
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                checkUserAvailability(username, password)
            }
        }

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }


    private fun checkUserAvailability(username: String, password: String) {
        lifecycleScope.launch {
            lifecycleScope.launch { viewModel.isUserAvailable(username) }.join()
            if (viewModel.username == username) {
                checkPassword(username, password)
            } else {
                Toast.makeText(
                    requireContext(),
                    "User is not available please sign up",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun checkPassword(username: String,password: String){
        lifecycleScope.launch {
            lifecycleScope.launch { viewModel.isPasswordCorrect(username,password) }.join()
            Log.d("viewModel_Logging",viewModel.password)
            if(viewModel.password == password){
                if(binding.loginRemembermeCb.isChecked) {
                    val sharedPref =
                        activity?.getSharedPreferences(Constant.SHARED_PREF_KEY, MODE_PRIVATE)
                    sharedPref?.edit()?.apply {
                        putBoolean(Constant.IS_USER_LOGGED, true)
                        putString(Constant.SAVED_USER_NAME_KEY,username)
                    }?.apply()
                    Constant.USER_NAME = username
                    Log.d("viewmodel_logging",Constant.USER_NAME)
                    findNavController().navigate(R.id.action_loginFragment_to_mealActivity)
                }
                else {
                    Constant.USER_NAME = username
                    Log.d("viewmodel_logging", Constant.USER_NAME)
                    findNavController().navigate(R.id.action_loginFragment_to_mealActivity)
                }
            }
            else
                Toast.makeText(requireContext(),"wrong password",Toast.LENGTH_SHORT).show()
        }
    }





//    private fun checkUserAvailability(username: String, password: String) {
//        viewModel.isUserAvailable(username)
//        Log.d("viewModel_logging", "user-check-function")
//        viewModel.isAvailable.observe(viewLifecycleOwner) { actualUsername ->
//            Log.d("viewModel_logging","observer")
//            if (actualUsername == username) {
//                checkPassword(username, password)
//            } else {
//                Toast.makeText(
//                    requireContext(),
//                    "User is not available please sign up",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//    }
//    private fun checkPassword(username: String,password: String){
//        viewModel.isPasswordCorrect(username,password)
//        Log.d("viewModel_logging","password check function")
//        viewModel.isPasswordCorrect.observe(viewLifecycleOwner){ actualPassword ->
//            if(actualPassword == password){
//                Log.d("viewModel_logging","logged in observer")
//                findNavController().navigate(R.id.action_loginFragment_to_mealActivity)
//                Toast.makeText(requireContext(),"logged in",Toast.LENGTH_SHORT).show()
//            }
//            else
//                Log.d("viewModel_logging","wrong password observer")
//                Toast.makeText(requireContext(),"sorry wrong password",Toast.LENGTH_SHORT).show()
//        }
//    }
}
