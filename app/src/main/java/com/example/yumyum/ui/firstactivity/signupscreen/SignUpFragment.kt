package com.example.yumyum.ui.firstactivity.signupscreen

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var name: String
    private lateinit var username: String
    private lateinit var password: String
    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepositoryImpl(ApplicationDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startObserver()
        binding.signupBt.isEnabled = false
        binding.btnLogin.setOnClickListener {
            navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.signupBt.setOnClickListener {
            bindTextFields()
//            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
//                Toast.makeText(requireContext(), "All fields must be filled out", Toast.LENGTH_SHORT).show()
            //} else {
                viewModel.getUsername(username)
            //}
        }
        binding.signupPasswordTv.editText?.addTextChangedListener {
            password = it.toString()
            validatePassword()
            checkFieldsForEmptyValues()
        }

        binding.signupNameTv.editText?.addTextChangedListener {
            checkFieldsForEmptyValues()
        }

        binding.signupUsernameTv.editText?.addTextChangedListener {
            checkFieldsForEmptyValues()
        }
        binding.signupPasswordTv.setErrorIconDrawable(null)
    }
    private fun validatePassword() {
        password = binding.signupPasswordTv.editText?.text.toString()

        var passwordError = ""
        if (password.length < 8) {
            passwordError += "Password must be at least 8 characters long.\n"
        }
        if (!password.any { it.isUpperCase() }) {
            passwordError += "Password must contain at least one uppercase letter.\n"
        }
        if (!password.any { !it.isDigit() }) {
            passwordError += "Password must contain at numbers.\n"
        }
        if (!password.any { it in listOf('.', '_', '-', '@') }) {
            passwordError += "Password must contain at least one special character (., _, -, @).\n"
        }


        binding.signupPasswordTv.error = if (passwordError.isEmpty()) null else passwordError
    }
    private fun checkFieldsForEmptyValues() {
        name = binding.signupNameTv.editText?.text.toString().trim()
        username = binding.signupUsernameTv.editText?.text.toString().trim()
        password = binding.signupPasswordTv.editText?.text.toString().trim()

        if (name.isEmpty()) {
            binding.signupNameTv.error = "Required"
        } else {
            binding.signupNameTv.error = null
        }

        if (username.isEmpty()) {
            binding.signupUsernameTv.error = "Required"
        } else {
            binding.signupUsernameTv.error = null
        }

        binding.signupBt.isEnabled =
            name.isNotEmpty() &&
            username.isNotEmpty() &&
            password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { !it.isDigit() } && password.any { it in listOf('.', '_', '-', '@') }
    }

    private fun startObserver(){
        viewModel.usernameAvailable.observe(viewLifecycleOwner){ availability ->
            if (availability)
                Toast.makeText(requireContext(), "Username already taken", Toast.LENGTH_SHORT).show()
            else {
                viewModel.insertUser(name, username, password)
                Toast.makeText(
                    requireContext(),
                    "Account created successfully",
                    Toast.LENGTH_SHORT
                ).show()
                navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }
    }

    private fun bindTextFields(){
        name = binding.signupNameTv.editText?.text.toString().trim()
        username = binding.signupUsernameTv.editText?.text.toString().trim()
        password = binding.signupPasswordTv.editText?.text.toString().trim()

    }

    private fun navigate(destination:Int){
        findNavController().navigate(destination)
    }


}