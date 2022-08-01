package com.example.mysalon.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysalon.model.remote.ApiClient
import com.example.mysalon.model.remote.AppUserApiService
import com.example.mysalon.model.remote.data.signUp.SignUpResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RegisterViewModel: ViewModel() {
    val retrofit: Retrofit = ApiClient.getRetrofit()
    val apiService: AppUserApiService = retrofit.create(AppUserApiService::class.java)
    val userLiveData = MutableLiveData<SignUpResponse>()

    fun signUp(mobileNo: String, password: String, fcmToken: String) {
        val map = HashMap<String, String>()
        map["mobileNo"] = mobileNo
        map["password"] = password
        map["fcmToken"] = fcmToken

        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        val call: Call<SignUpResponse> = apiService.signUp(body)
        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.status == 0){
                        userLiveData.postValue(response.body())
                    } else {
                        Log.e("response error", response.body()!!.message)
                    }

                }
            }
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }
}