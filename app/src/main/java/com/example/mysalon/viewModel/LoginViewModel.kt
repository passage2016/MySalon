package com.example.mysalon.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysalon.model.remote.ApiClient
import com.example.mysalon.model.remote.LoginApiService
import com.example.mysalon.model.remote.data.login.LoginResponse
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginViewModel: ViewModel() {
    val retrofit: Retrofit = ApiClient.getRetrofit()
    val apiService: LoginApiService= retrofit.create(LoginApiService::class.java)
    val userLiveData = MutableLiveData<LoginResponse>()

    fun login(username: String, password: String) {
        val map = HashMap<String, String>()
        map["username"] = username
        map["password"] = password

        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        val movieCall: Call<LoginResponse> = apiService.login(body)
        movieCall.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    userLiveData.postValue(response.body())
                    Log.e("response.body()", response.body()!!.message)
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }
}