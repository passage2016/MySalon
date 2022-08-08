package com.example.mysalon.model.remote

import com.example.mysalon.model.remote.data.dashboard.DashboardResponse
import com.example.mysalon.model.remote.data.login.LoginResponse
import com.example.mysalon.model.remote.data.logout.LogoutResponse
import com.example.mysalon.model.remote.data.product.ProductsResponse
import com.example.mysalon.model.remote.data.review.addReview.AddReviewResponse
import com.example.mysalon.model.remote.data.review.getReview.GetReviewResponse
import com.example.mysalon.model.remote.data.signUp.SignUpResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AppUserApiService {
    @Headers("Content-type: application/json")
    @POST("appUser/login")
    fun login(@Body loginReq: RequestBody): Call<LoginResponse>

    @Headers("Content-type: application/json")
    @POST("appUser/signup")
    fun signUp(@Body signUpReq: RequestBody): Call<SignUpResponse>

    @Headers("Content-type: application/json")
    @GET("appUser/dashboard")
    fun dashboard(): Call<DashboardResponse>

    @Headers("Content-type: application/json")
    @POST("appUser/reviews")
    fun getReviews(@Body getProducts: RequestBody): Call<GetReviewResponse>

    @Headers("Content-type: application/json")
    @POST("appUser/logout")
    fun logout(@Header("ps_auth_token") ps_auth_token: String, @Body logoutReq: RequestBody): Single<LogoutResponse>

    @Headers("Content-type: application/json")
    @POST("appUser/addReviews")
    fun addReview(@Header("ps_auth_token") ps_auth_token: String, @Body addReviewReq: RequestBody): Single<AddReviewResponse>
}