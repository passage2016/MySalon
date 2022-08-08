package com.example.mysalon.model.remote

import com.example.mysalon.model.remote.data.albumList.AlbumListResponse
import com.example.mysalon.model.remote.data.albumPhotos.AlbumPhotosResponse
import com.example.mysalon.model.remote.data.alert.AlertResponse
import com.example.mysalon.model.remote.data.contacts.ContactsResponse
import com.example.mysalon.model.remote.data.getService.GetServiceResponse
import com.example.mysalon.model.remote.data.getServiceCategory.GetServiceCategoryResopnse
import com.example.mysalon.model.remote.data.getServicesByCategory.GetServicesByCategoryResponse
import com.example.mysalon.model.remote.data.product.ProductsResponse
import com.example.mysalon.model.remote.data.workingHour.WorkingHourResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface BaseApiService {
    @Headers("Content-type: application/json")
    @GET("service/getServiceCategory")
    fun getServiceCategory(): Call<GetServiceCategoryResopnse>

    @Headers("Content-type: application/json")
    @GET
    fun getServicesByCategory(@Url url: String): Call<GetServicesByCategoryResponse>

    @Headers("Content-type: application/json")
    @GET("albums/getList")
    fun getAlbumList(): Call<AlbumListResponse>

    @Headers("Content-type: application/json")
    @GET
    fun getAlbumPhotos(@Url url: String): Call<AlbumPhotosResponse>

    @Headers("Content-type: application/json")
    @POST("offers/getProducts")
    fun getProducts(@Body getProducts: RequestBody): Call<ProductsResponse>

    @Headers("Content-type: application/json")
    @GET("shopContacts/getList")
    suspend fun getContacts(): Response<ContactsResponse>

    @Headers("Content-type: application/json")
    @GET("workingHours/getList")
    fun getWorkingHours(): Single<WorkingHourResponse>

    @Headers("Content-type: application/json")
    @GET("alert/getList")
    fun getAlert(): Single<AlertResponse>



}