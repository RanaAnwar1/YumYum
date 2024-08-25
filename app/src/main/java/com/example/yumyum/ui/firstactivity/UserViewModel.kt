package com.example.yumyum.ui.firstactivity

import android.util.Log
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
    private val _username = MutableLiveData<String>()
    val username:LiveData<String> get() = _username

    private val _password = MutableLiveData<String>()
    val password:LiveData<String> get() = _password

    private val _name = MutableLiveData<String>()
    val name:LiveData<String> get() = _name

    fun getUsername(inputUsername: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {  repo.getUserByUsername(inputUsername) }
            val actualUser = result.await()?.username ?: Constant.USER_UNKNOWN
            _username.postValue(actualUser)
        }
    }

    fun getPassword(username: String, inputPassword: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getUserByUsername(username) }
            val actualPassword = result.await()?.password ?: Constant.DEFAULT_PASSWORD
            _password.postValue(actualPassword)
        }
    }

//    fun getName(username: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = async { repo.getName(username) }
//            _name.postValue(result.await())
//        }
//    }

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

