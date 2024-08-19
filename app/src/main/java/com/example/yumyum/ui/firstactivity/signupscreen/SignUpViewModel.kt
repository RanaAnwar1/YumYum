package com.example.yumyum.ui.firstactivity.signupscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yumyum.data.model.User
import com.example.yumyum.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(private val userRepository:UserRepository) : ViewModel() {

    private val _User = MutableLiveData<User>()
    val user: LiveData<User> get() = _User

    fun insertUser(name: String, username: String, password: String) {
        val user = User(name = name, username = username, password = password)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.insertUser(user)
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