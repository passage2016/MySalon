package com.example.mysalon.viewModel

import androidx.lifecycle.ViewModel
import com.example.mysalon.model.remote.ApiClient
import com.example.mysalon.model.remote.LoginApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class loginViewModel: ViewModel() {
    lateinit var apiService: Retrofit
    fun login(language: String, apiKey: String) {
        lateinit var retrofit: Retrofit

        retrofit = ApiClient.getRetrofit()
        apiService = retrofit.create(LoginApiService::class.java)

        val movieCall: Call<NewsResponse> = apiService.getNews(language, apiKey)
        movieCall.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    newsLiveData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                error.value = t.message.toString()
                t.printStackTrace()
            }
        })
    }
}