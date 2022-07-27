package com.example.mysalon.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysalon.model.remote.data.login.LoginResponse

class MainViewModel: ViewModel() {
    val userLiveData = MutableLiveData<LoginResponse>()

    fun setUserLiveData(loginResponse: LoginResponse){
        userLiveData.postValue(loginResponse)
    }
}