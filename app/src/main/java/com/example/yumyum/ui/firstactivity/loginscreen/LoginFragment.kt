package com.example.yumyum.ui.firstactivity.loginscreen

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
                val sharedPref = activity?.getSharedPreferences(Constant.SHARED_PREF_KEY, MODE_PRIVATE)
                sharedPref?.edit()?.putBoolean(Constant.IS_USER_LOGGED,true)?.apply()
                findNavController().navigate(R.id.action_loginFragment_to_activity_meal_navigation)
            }
            else
                Toast.makeText(requireContext(),"wrong password",Toast.LENGTH_SHORT).show()
        }
    }





















//    private fun checkUserAvailability(username: String, password: String) {
//        lifecycleScope.launch {
//            lifecycleScope.launch { viewModel.isUserAvailable(username) }.join()
//            viewModel.isAvailable.observe(viewLifecycleOwner) { actualUsername ->
//                if (actualUsername == username) {
//                    checkPassword(username, password)
//                } else {
//                    Log.d("viewModel_logging", "actualUsername")
//                    Toast.makeText(
//                        requireContext(),
//                        "User is not available please sign up",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
//    }
//    private fun checkPassword(username: String,password: String){
//        viewModel.isPasswordCorrect(username,password)
//        viewModel.isPasswordCorrect.observe(viewLifecycleOwner){ actualPassword ->
//            if(actualPassword == password){
//                Toast.makeText(requireContext(),"logged in",Toast.LENGTH_SHORT).show()
//            }
//            else
//                Log.d("viewModel_logging",actualPassword)
//                Toast.makeText(requireContext(),"sorry wrong password",Toast.LENGTH_SHORT).show()
//        }
//    }
}
