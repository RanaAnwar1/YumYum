package com.example.yumyum.ui.firstactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yumyum.data.model.User
import com.example.yumyum.data.repository.MealsRepository
import com.example.yumyum.data.repository.UserRepository
import com.example.yumyum.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    val repo:UserRepository
):ViewModel() {

    fun isUserAvailable(inputUsername:String):Boolean{
        var actualUsername = ""
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getUsername() }
            actualUsername = result.await()
        }
        return actualUsername == inputUsername
    }

    fun isPasswordCorrect(inputPassword:String):Boolean{
        var actualPassword = ""
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getPassword() }
            actualPassword = result.await()
        }
        return actualPassword == inputPassword
    }

    fun getUsername():String {
        var username: String? = null
        viewModelScope.launch(Dispatchers.IO) {
            username = repo.getUsername()
        }
        return username ?: Constant.USER_UNKNOWN
    }
    fun getName():String{
        var name: String? = null
        viewModelScope.launch(Dispatchers.IO) {
            name = repo.getName()
        }
        return name ?: Constant.Name_UNKNOWN
    }

    fun insertUser(name: String, username: String, password: String) {
        val user = User(name = name, username = username, password = password)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.insertUser(user)
            }
        }
    }
}

class SignUpViewModelFactory(
    private val userRepository: UserRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModelFactory(userRepository) as T
    }
}