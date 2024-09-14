package com.example.yumyum.ui.firstactivity

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yumyum.data.model.User
import com.example.yumyum.data.repository.UserRepository
import com.example.yumyum.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserViewModel(
    private val repo:UserRepository
):ViewModel() {
    private val _usernameAvailable = MutableLiveData<Boolean>()
    val usernameAvailable:LiveData<Boolean> get() = _usernameAvailable

    private val _rightPassword = MutableLiveData<Boolean>()
    val rightPassword:LiveData<Boolean> get() = _rightPassword

    private val _name = MutableLiveData<String>()
    val name:LiveData<String> get() = _name

    private val _isEmptyLogin = MutableLiveData<Boolean>()
    val isEmptyLogin:LiveData<Boolean> get() = _isEmptyLogin

    fun getUsername(inputUsername: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {  repo.getUserByUsername(inputUsername) }
            val actualUser = result.await()?.username ?: Constant.USER_UNKNOWN
            if(actualUser == inputUsername)
                _usernameAvailable.postValue(true)
            else
                _usernameAvailable.postValue(false)
        }
    }

    fun checkPassword(username: String, inputPassword: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getUserByUsername(username) }
            val actualPassword = result.await()?.password ?: Constant.DEFAULT_PASSWORD
            if(actualPassword == inputPassword)
                _rightPassword.postValue(true)
            else
                _rightPassword.postValue(false)
        }
    }

    fun checkLoginCredintials(username: String,password: String){
        if(username.isNotEmpty() && password.isNotEmpty()) {
            getUsername(username)
            checkPassword(username, password)
            _isEmptyLogin.postValue(false)
        }else
            _isEmptyLogin.postValue(true)
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

