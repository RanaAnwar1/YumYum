package com.example.yumyum.ui.firstactivity.loginscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.yumyum.R
import com.example.yumyum.data.repository.UserRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.databinding.FragmentLoginBinding
import com.example.yumyum.ui.firstactivity.UserViewModel
import com.example.yumyum.ui.firstactivity.UserViewModelFactory
import com.example.yumyum.util.Constant

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var username: String
    private lateinit var password: String
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
        handleLoginButtonClick()
        startObservers()
        handleSignUpButtonClick()
    }

    private fun handleLoginButtonClick(){
        binding.LoginBt.setOnClickListener {
            username = binding.loginUsernameTv.editText?.text.toString().trim()
            password = binding.loginPasswordTv.editText?.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                checkUserAvailability(username, password)
                checkPassword(username, password)
            }
        }
    }

    private fun startObservers(){
        viewModel.username.observe(viewLifecycleOwner) { actualUsername ->
            if (actualUsername != username)
                Toast.makeText(requireContext(), "User is not available please sign up", Toast.LENGTH_LONG).show()
        }


        viewModel.password.observe(viewLifecycleOwner){ actualPassword ->
            if(actualPassword == password){
                if(binding.loginRemembermeCb.isChecked) {
                    editSharedPref()
                    navigate(R.id.action_loginFragment_to_mealActivity)
                }
                else {
                    Constant.USER_NAME = username
                    navigate(R.id.action_loginFragment_to_mealActivity)
                }
            }
            else
                Toast.makeText(requireContext(),"sorry wrong password",Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleSignUpButtonClick(){
        binding.btnSignup.setOnClickListener {
            navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    private fun checkUserAvailability(username: String, password: String) {
        viewModel.getUsername(username)

    }
    private fun checkPassword(username: String,password: String){
        viewModel.getPassword(username,password)
    }

    private fun editSharedPref(){
        val sharedPref =
            activity?.getSharedPreferences(Constant.SHARED_PREF_KEY, MODE_PRIVATE)
        sharedPref?.edit()?.apply {
            putBoolean(Constant.IS_USER_LOGGED, true)
            putString(Constant.SAVED_USER_NAME_KEY,username)
        }?.apply()
        Constant.USER_NAME = username
    }

    private fun navigate(destination:Int){
        findNavController().navigate(destination)
    }
}
