package com.example.yumyum.ui.firstactivity.loginscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.yumyum.R
import com.example.yumyum.data.repository.UserRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.databinding.FragmentLoginBinding
import com.example.yumyum.ui.firstactivity.UserViewModel
import com.example.yumyum.ui.firstactivity.UserViewModelFactory
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
                performLogin(username, password)
            }
        }

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    private fun performLogin(username: String, password: String) {
        lifecycleScope.launch {
            val isUserAvailable = viewModel.isUserAvailable(username)
            val isCorrect = viewModel.isPasswordCorrect(username,password)
            if (isUserAvailable) {
                if (isCorrect) {
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                    // TODO: Navigate to the home screen
                } else {
                    Toast.makeText(requireContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
