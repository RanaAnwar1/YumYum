package com.example.yumyum.ui.firstactivity.signupscreen

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.yumyum.R
import com.example.yumyum.data.repository.UserRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.databinding.FragmentLoginBinding
import com.example.yumyum.databinding.FragmentSignUpBinding
import com.example.yumyum.ui.firstactivity.UserViewModel
import com.example.yumyum.ui.firstactivity.UserViewModelFactory
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepositoryImpl(ApplicationDatabase.getInstance(requireContext())))
    }
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var name: String
    private lateinit var username: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.signupBt.setOnClickListener {
            name = binding.signupNameTv.editText?.text.toString().trim()
            username = binding.signupUsernameTv.editText?.text.toString().trim()
            password = binding.signupPasswordTv.editText?.text.toString().trim()

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "All fields must be filled out", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.isUserAvailable(username)
            }
        }
        viewModel.isAvailable.observe(viewLifecycleOwner){ actualUsername ->
            if (actualUsername == username) {
                Toast.makeText(requireContext(), "Username already taken", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.insertUser(name, username, password)
                Toast.makeText(
                    requireContext(),
                    "Account created successfully",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }
    }


}