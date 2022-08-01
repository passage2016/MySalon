package com.example.mysalon.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysalon.model.remote.ApiClient
import com.example.mysalon.model.remote.AppUserApiService
import com.example.mysalon.model.remote.data.login.LoginResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginViewModel: ViewModel() {
    val retrofit: Retrofit = ApiClient.getRetrofit()
    val apiService: AppUserApiService= retrofit.create(AppUserApiService::class.java)
    val userLiveData = MutableLiveData<LoginResponse>()

    fun login(mobileNo: String, password: String) {
        val map = HashMap<String, String>()
        map["mobileNo"] = mobileNo
        map["password"] = password

        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        val call: Call<LoginResponse> = apiService.login(body)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.status == 0){
                        userLiveData.postValue(response.body())
                    } else {
                        Log.e("response error", response.body()!!.message)
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }
}