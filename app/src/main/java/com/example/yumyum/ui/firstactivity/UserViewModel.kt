package com.example.yumyum.ui.firstactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yumyum.data.repository.MealsRepository
import com.example.yumyum.data.repository.UserRepository
import com.example.yumyum.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
}