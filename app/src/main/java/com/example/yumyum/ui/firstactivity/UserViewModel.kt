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
    private val repo:UserRepository
):ViewModel() {

    fun isUserAvailable(inputUsername:String):Boolean{
        var actualUsername = ""
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getUserByUsername(inputUsername) }
            actualUsername = result.await()?.username ?: Constant.USER_UNKNOWN
        }
        return actualUsername == inputUsername
    }

    fun isPasswordCorrect(username: String,inputPassword:String):Boolean{
        var actualPassword = ""
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getUserByUsername(username) }
            actualPassword = result.await()?.password ?: Constant.DEFAULT_PASSWORD
        }
        return actualPassword == inputPassword
    }

    fun getName(username: String):String{
        var name: String = ""
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getName(username) }
            name = result.await()
        }
        return name
    }

    fun insertUser(name: String, username: String, password: String) {
        val user = User(name = name, username = username, password = password)
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertUser(user)
        }
    }
}

class UserViewModelFactory(
    private val repo: UserRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModelFactory(repo) as T
    }
}