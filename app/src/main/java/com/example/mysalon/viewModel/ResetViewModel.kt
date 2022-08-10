package com.example.mysalon.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysalon.model.remote.ApiClient
import com.example.mysalon.model.remote.AppUserApiService
import com.example.mysalon.model.remote.data.BaseResponse
import com.example.mysalon.model.remote.data.getPhoneVerificationCode.GetPhoneVerificationCodeResponse
import com.example.mysalon.model.remote.data.signUp.SignUpResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ResetViewModel: ViewModel() {
    val retrofit: Retrofit = ApiClient.getRetrofit()
    val apiService: AppUserApiService = retrofit.create(AppUserApiService::class.java)
    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()

    val verificationCodeResponse = MutableStateFlow<GetPhoneVerificationCodeResponse?>(null)
    val resetPasswordResponse = MutableStateFlow<BaseResponse?>(null)

    fun getPhoneVerificationCode(mobileNo: String) = viewModelScope.launch{
        val url = "appUser/getPhoneVerificationCode/" + mobileNo
        verificationCodeResponse.emit(apiService.getPhoneVerificationCode(url))
        Log.e("", verificationCodeResponse.value.toString())
        if(verificationCodeResponse.value!!.status == 1){
            errorMessage.postValue(verificationCodeResponse.value!!.message)
        }
    }

    fun resetPassword(mobileNo: String, password: String, phoneVerificationCode: String) = viewModelScope.launch{
        val map = HashMap<String, String>()
        map["mobileNo"] = mobileNo
        map["password"] = password
        map["phoneVerificationCode"] = phoneVerificationCode

        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        resetPasswordResponse.emit(apiService.resetPassword(body))
        Log.e("", resetPasswordResponse.value.toString())
        if(resetPasswordResponse.value!!.status == 1){
            errorMessage.postValue(resetPasswordResponse.value!!.message)
        } else {
            successMessage.postValue(resetPasswordResponse.value!!.message)
        }

    }
}