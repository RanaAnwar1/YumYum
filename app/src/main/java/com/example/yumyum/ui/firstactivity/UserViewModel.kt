package com.example.yumyum.ui.firstactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    suspend fun isUserAvailable(inputUsername: String): Boolean {
        val result = repo.getUserByUsername(inputUsername)
        return result?.username == inputUsername
    }
    suspend fun isPasswordCorrect(username: String, inputPassword: String): Boolean {
        val result = repo.getUserByUsername(username)
        val actualPassword = result?.password ?: Constant.DEFAULT_PASSWORD
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
        return UserViewModel(repo) as T
    }
}

